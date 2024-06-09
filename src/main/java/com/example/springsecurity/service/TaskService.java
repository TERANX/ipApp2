package com.example.springsecurity.service;

import com.example.springsecurity.model.Option;
import com.example.springsecurity.model.Task;
import com.example.springsecurity.repository.OptionsRepository;
import com.example.springsecurity.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private final TaskRepository trepo;

    @Autowired
    private OptionsRepository optRep;

    public List<Task> findAllTasks() {
        return trepo.findAll();
    }

    public Task getById(Long id) {
        return trepo.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("no task with id=%d founded ", id)));
    }

    public void save(Task task) {
        final List<Option> options = task.getOptions();
        task.setOptions(new ArrayList<>());
        final Task dbTask = trepo.save(task);
        options.forEach(opt -> opt.setTaskId(dbTask));
        optRep.saveAll(options);
    }

    public Task delete(Long id) {
        Task task = getById(id);
        trepo.delete(task);
        return task;
    }
}
