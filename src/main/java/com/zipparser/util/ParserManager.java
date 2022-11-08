package com.zipparser.util;

import com.zipparser.config.ParserProperties;
import com.zipparser.entity.Request;
import com.zipparser.entity.Result;
import com.zipparser.entity.Status;
import com.zipparser.service.RequestService;
import com.zipparser.service.ResultService;
import com.zipparser.service.StatusService;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@Setter
@Scope("prototype")
public class ParserManager implements Runnable {

    private final RequestService requestService;
    private final StatusService statusService;
    private final ResultService resultService;
    private final ParserProperties parserProperties;
    private final ParserFactory parserFactory;

    private Request request;
    private byte[] fileContent;

    public ParserManager(RequestService requestService, StatusService statusService, ResultService resultService, ParserProperties parserProperties, ParserFactory parserFactory) {
        this.requestService = requestService;
        this.statusService = statusService;
        this.resultService = resultService;
        this.parserProperties = parserProperties;
        this.parserFactory = parserFactory;
    }

    @Override
    public void run() {
        setRequestStatus(2);
        saveResult(readZipFile());
        setRequestStatus(1);
    }

    private void setRequestStatus(Integer statusId) {
        Status status = statusService.getById(statusId);
        request.setStatus(status);
        requestService.save(request);
    }

    private void saveResult(Map<String, ArrayList<String>> tempResult) {
        for (Map.Entry<String, ArrayList<String>> entry : tempResult.entrySet()) {
            if (entry.getValue().size() > 1) {
                Result result = new Result();
                result.setContent(entry.getKey());
                result.setFiles(entry.getValue());
                result.setRequest(request);
                resultService.save(result);
            }
        }
    }

    private Map<String, ArrayList<String>> readZipFile() {

        Map<String, ArrayList<String>> tmpResult = new HashMap<>();

        try (InputStream fis = new ByteArrayInputStream(fileContent);
             ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry zipEntry;
            Parser parser;
            String pattern = parserProperties.getRegexp();
            byte[] buffer = new byte[1024];
            int bufferSize = 1024;
            File tempFile;

            while ((zipEntry = zis.getNextEntry()) != null) {

                if (zipEntry.isDirectory())
                    continue;

                // Temp file from zip entry
                tempFile = File.createTempFile(zipEntry.getName(), "tmp");
                tempFile.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos,
                        bufferSize);
                int count;
                while ((count = zis.read(buffer, 0, bufferSize)) != -1) {
                    bos.write(buffer, 0, count);
                }
                bos.flush();
                bos.close();

                // Parsing
                parser = parserFactory.getParser(zipEntry.getName());
                Set<String> parseResult = parser.parse(tempFile, pattern);

                // Add to temporal result
                for (String content : parseResult) {
                    if (tmpResult.get(content) == null) {
                        ArrayList<String> files = new ArrayList<>();
                        files.add(zipEntry.getName());
                        tmpResult.put(content, files);
                    } else {
                        ArrayList<String> files = tmpResult.get(content);
                        files.add(zipEntry.getName());
                        tmpResult.put(content, files);
                    }
                }
            }

        } catch (IOException ex) {
            setRequestStatus(3);
            throw new RuntimeException(ex);
        }

        return tmpResult;
    }

}
