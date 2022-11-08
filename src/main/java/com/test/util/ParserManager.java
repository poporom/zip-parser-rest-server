package com.test.util;

import com.test.config.ParserProperties;
import com.test.entity.Request;
import com.test.entity.Result;
import com.test.entity.Status;
import com.test.service.RequestService;
import com.test.service.ResultService;
import com.test.service.StatusService;
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
public class ParserManager implements Runnable{

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

        Parser parser;
        Map<String, ArrayList<String>> result = new HashMap<>();

        // New status to request
        Status status = statusService.getById(2);
        request.setStatus(status);
        requestService.save(request);

        // Opening zip + iterate
        File tempFile = null;
        byte[] buffer = new byte[1024];
        int bufferSize = 1024;

        try (InputStream fis = new ByteArrayInputStream(fileContent); ZipInputStream zis = new ZipInputStream(fis)) {

            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {

                if (entry.isDirectory())
                    continue;

                // Making File from entry
                tempFile = File.createTempFile(entry.getName(), "tmp");
                tempFile.deleteOnExit();
                FileOutputStream fos = new FileOutputStream(tempFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos, bufferSize);
                int count;
                while ((count = zis.read(buffer, 0, bufferSize)) != -1) {
                    bos.write(buffer, 0, count);
                }
                bos.flush();
                bos.close();

                // Parsing
                String pattern = parserProperties.getRegexp();
                parser = parserFactory.getParser(entry.getName());
                Set<String> parse = parser.parse(tempFile, pattern);

                // Add to final result
                for (String entry1: parse) {
                    if (result.get(entry1) == null) {
                        ArrayList<String> values = new ArrayList<>();
                        values.add(entry.getName());
                        result.put(entry1,values);
                    } else {
                        ArrayList<String> strings = result.get(entry1);
                        strings.add(entry.getName());
                        result.put(entry1,strings);
                    }
                }
            }

        } catch (IOException ex) {
            // New status to request
            status = statusService.getById(3);
            request.setStatus(status);
            requestService.save(request);

            throw new RuntimeException(ex);
        }

        // Analise
        for (Map.Entry<String, ArrayList<String>> res: result.entrySet()) {
            // Add to table
            if (res.getValue().size() > 1) {
                String cont = res.getKey();
                for (String str: res.getValue()) {
                    Result resul = new Result();
                    resul.setContent(cont);
                    resul.setFile(str);
                    resul.setRequest(request);
                    resultService.save(resul);
                }
            }
        }

        // New status to request
        status = statusService.getById(1);
        request.setStatus(status);
        requestService.save(request);

    }
}
