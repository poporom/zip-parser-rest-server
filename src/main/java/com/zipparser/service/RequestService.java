package com.zipparser.service;

import com.zipparser.entity.Request;
import com.zipparser.repo.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;

    }

    public Request getById(Integer id) {
        return requestRepository.findById(id).get();
    }

    public void save(Request req) {
        requestRepository.save(req);
    }


}
