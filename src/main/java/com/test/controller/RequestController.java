package com.test.controller;

import com.test.config.ApplicationContextProvider;
import com.test.entity.Request;
import com.test.service.RequestService;
import com.test.util.ParserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/requests")
@Slf4j
public class RequestController {

    private final RequestService requestService;
    private final ApplicationContextProvider applicationContextProvider;

    @Autowired
    public RequestController(RequestService requestService, ApplicationContextProvider applicationContextProvider) {
        this.requestService = requestService;
        this.applicationContextProvider = applicationContextProvider;
    }

    @PostMapping(path = "/load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Integer load(@RequestParam("file")MultipartFile file) {
        Request req = new Request();
        requestService.save(req);

        byte[] fileContent;
        try {
            fileContent = file.getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ParserManager parserManager = applicationContextProvider.getApplicationContext().getBean(ParserManager.class);
        parserManager.setRequest(req);
        parserManager.setFileContent(fileContent);

        Thread t = new Thread(parserManager);
        t.start();

        return req.getId();
    }

    @GetMapping("/result/{id}")
    public Request result(@PathVariable Integer id) {
        return requestService.getById(id);
    }
}