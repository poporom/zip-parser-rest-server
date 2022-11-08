package com.test.service;

import com.test.entity.Result;
import com.test.repo.ResultRepository;
import org.springframework.stereotype.Service;

@Service
public class ResultService {
    private final ResultRepository resultRepository;

    public ResultService(ResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void save(Result res) {
        resultRepository.save(res);
    }


}
