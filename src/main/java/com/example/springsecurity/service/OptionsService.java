package com.example.springsecurity.service;

import com.example.springsecurity.model.Options;
import com.example.springsecurity.repository.OptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OptionsService {

    @Autowired
    private OptionsRepository or;

    public Options getById(Long id) {
        return or.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no options with id=%d founded ", id)));
    }

    public List<Options> getAll() {
        return or.findAll();
    }

    public Options save(Options options) {
        return or.save(options);
    }

    public Options delete (Long id) {
        Options options = getById(id);
        or.delete(options);
        return options;
    }

}
