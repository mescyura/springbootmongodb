package com.example.springbootmongodb.controller;

import com.example.springbootmongodb.model.TodoDTO;
import com.example.springbootmongodb.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos() {
        log.info("getting all todos");
        List<TodoDTO> todos = todoRepository.findAll();
        if (todos.size() > 0) {
            log.info("all todos - {}", todos);
            return new ResponseEntity<>(todos, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("no todos available", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping(value = "/todos", consumes = "application/json")
    public ResponseEntity<?> createTodo(@RequestBody TodoDTO todoDTO) {
        log.info("created todo - {}", todoDTO);
        try {
            todoDTO.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todoDTO);
            return new ResponseEntity<>(todoDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable("id") String id) {
        log.info("Searching for todo with id - {}", id);
        Optional<TodoDTO> todoDTOOptional = todoRepository.findById(id);
        if (todoDTOOptional.isPresent()) {
            log.info("todo found - {}", todoDTOOptional.get());
            return new ResponseEntity<>(todoDTOOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Todo not found with id" + id, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/todos/{id}")
    public ResponseEntity<?> updateTodoById(@PathVariable("id") String id, @RequestBody TodoDTO todoDTO) {
        log.info("Searching for todo with id - {}", id);
        Optional<TodoDTO> todoDTOOptional = todoRepository.findById(id);
        if (todoDTOOptional.isPresent()) {
            log.info("todo found - {}", todoDTOOptional.get());
            TodoDTO todoDTOtoSave = todoDTOOptional.get();
            todoDTOtoSave.setCompleted(todoDTO.getCompleted() != null ? todoDTO.getCompleted() : todoDTOtoSave.getCompleted());
            todoDTOtoSave.setTodo(todoDTO.getTodo() != null ? todoDTO.getTodo() : todoDTOtoSave.getTodo());
            todoDTOtoSave.setDescription(todoDTO.getDescription() != null ? todoDTO.getDescription() : todoDTOtoSave.getDescription());
            todoDTOtoSave.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todoDTOtoSave);
            log.info("todo updated - {}", todoDTOtoSave);
            return new ResponseEntity<>(todoDTOtoSave, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Todo not found with id" + id, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteById(@PathVariable("id") String id) {
        log.info("Trying to delete todo with id - {}", id);
        try {
            todoRepository.deleteById(id);
            return new ResponseEntity<>("Successfully deleted todo by id - " + id, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}