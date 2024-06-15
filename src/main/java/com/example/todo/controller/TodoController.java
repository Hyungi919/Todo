package com.example.todo.controller;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.example.todo.dto.ResponseDTO;
import com.example.todo.service.TodoService;
import com.example.todo.model.TodoEntity;
import com.example.todo.dto.TodoDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "http://http://localhost:3000")
@RestController
@RequestMapping("todo")
public class TodoController {

    @Autowired
    private TodoService service;

    @PostMapping
    public ResponseEntity<?>createTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
        try {
            log.info("Log:createTodo entrance");

            TodoEntity entity = TodoDTO.toEntity(dto);
            log.info("Log:dto => entity ok!");
            entity.setId(null);
            entity.setUserId(userId);

            List<TodoEntity> entities = service.create(entity);
            log.info("Log:service.create ok!");

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            log.info("Log:entities => dtos ok!");

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
            log.info("Log:response dto ok!");

            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping
    public ResponseEntity<?> retrieveTodo(@AuthenticationPrincipal String userId){
        List<TodoEntity> entities = service.retrieve(userId);
        List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
        ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/update")
    public ResponseEntity<?> update(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
        try {
            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            List<TodoEntity> entities = service.update(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @PutMapping
    public ResponseEntity<?> updateTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
        try {
            TodoEntity entity = TodoDTO.toEntity(dto);

            entity.setUserId(userId);

            List<TodoEntity> entities = service.updateTodo(entity);

            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());

            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @DeleteMapping
    public ResponseEntity<?> deleteTodo(@AuthenticationPrincipal String userId,@RequestBody TodoDTO dto){
        try {
            TodoEntity entity = TodoDTO.toEntity(dto);
            entity.setUserId(userId);
            List<TodoEntity> entities = service.delete(entity);
            List<TodoDTO> dtos = entities.stream().map(TodoDTO::new).collect(Collectors.toList());
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().data(dtos).build();

            return ResponseEntity.ok().body(response);
        }catch (Exception e) {
            String error = e.getMessage();
            ResponseDTO<TodoDTO> response = ResponseDTO.<TodoDTO>builder().error(error).build();
            return ResponseEntity.badRequest().body(response);
        }
    }
    @DeleteMapping("/batch-delete")
    public ResponseEntity<Void> batchDeleteTodos(@RequestBody List<Long> todoIds) {
        try {
            service.batchDeleteTodos(todoIds);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}