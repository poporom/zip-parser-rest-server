package com.zipparser.service;

import com.zipparser.entity.Status;
import com.zipparser.repo.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status getById(Integer id) {
        return statusRepository.findById(id).get();
    }

}
