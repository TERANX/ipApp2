package com.example.springsecurity.service;

import com.example.springsecurity.model.Option;
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

    public Option getById(Long id) {
        return or.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no options with id=%d founded ", id)));
    }

    public List<Option> getAll() {
        return or.findAll();
    }

    public void save(List<Option> options) {
        or.saveAll(options);
    }

    public Option delete (Long id) {
        Option option = getById(id);
        or.delete(option);
        return option;
    }

}
