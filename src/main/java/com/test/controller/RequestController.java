package com.test.controller;

import com.test.config.ApplicationContextProvider;
import com.test.entity.Request;
import com.test.entity.Result;
import com.test.service.RequestService;
import com.test.util.ParserManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public Request load(@RequestParam("file")MultipartFile file) {
        Request req = new Request();
        requestService.save(req);

        ParserManager parserManager = applicationContextProvider.getApplicationContext().getBean(ParserManager.class);
        parserManager.setRequest(req);
        byte[] fileContent = new byte[0];
        try {
            fileContent = file.getBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        parserManager.setFileContent(fileContent);
        Thread t = new Thread(parserManager);
        t.start();

        return req;
    }

    @GetMapping("/result/{id}")
    public List<Result> result(@PathVariable Integer id) {
        Request req = requestService.getById(id);
        return req.getResultList();
    }
}