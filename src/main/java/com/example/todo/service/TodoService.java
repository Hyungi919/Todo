package com.example.todo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.todo.model.TodoEntity;
import com.example.todo.persistence.TodoRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class TodoService {

    @Autowired
    private TodoRepository repository;

    public List<TodoEntity>create(final TodoEntity entity){
        validate(entity);
        repository.save(entity);
        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> retrieve(final String userId){
        return repository.findByUserId(userId);
    }

    public List<TodoEntity> update(final TodoEntity entity){
        validate(entity);
        if (repository.existsById(entity.getId())){
            repository.save(entity);
        }
        else
            throw new RuntimeException("Unknown id");
        return repository.findByUserId(entity.getUserId());
    }

    public List<TodoEntity> updateTodo(final TodoEntity entity){
        validate(entity);
        final Optional<TodoEntity> original = repository.findById(entity.getId());
        original.ifPresent(todo ->{
            todo.setTitle(entity.getTitle());
            todo.setDone(entity.isDone());
            repository.save(todo);
        });
        return repository.findByUserId(entity.getId());
    }

    public List<TodoEntity> delete(final TodoEntity entity) {
        if (repository.existsById(entity.getId()))
            repository.deleteById(entity.getId());
        else
            throw new RuntimeException("id does not exist");
        return repository.findByUserId(entity.getUserId());
    }

    public void validate(final TodoEntity entity){
        if (entity == null){
            log.warn("Entity cannot be null");
            throw new RuntimeException("Entity cannot be null");
        }
        if (entity.getUserId() == null){
            log.warn("Unknown error");
            throw new RuntimeException("Unknown error");
        }
    }
    @Transactional
    public void batchDeleteTodos(List<Long> todoIds) {
        for (Long id : todoIds) {
            repository.deleteById(String.valueOf(id));
        }
    }
}