package com.test.service;

import com.test.entity.Status;
import com.test.repo.StatusRepository;
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
