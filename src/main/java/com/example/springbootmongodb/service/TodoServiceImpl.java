package com.example.springbootmongodb.service;

import com.example.springbootmongodb.exception.TodoCollectionException;
import com.example.springbootmongodb.model.Todo;
import com.example.springbootmongodb.repository.TodoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import java.util.*;

@Slf4j
@Service
public class TodoServiceImpl implements TodoService {

    @Autowired
    private TodoRepository todoRepository;

    @Override
    public void CreateTodo(Todo todo) throws ConstraintViolationException, TodoCollectionException {
        Optional<Todo> optionalTodo = todoRepository.findByTodo(todo.getTodo());
        if ((optionalTodo.isPresent())) {
            log.warn("there is an existing todo with {} name already", todo.getTodo());
            throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
        } else {
            todo.setCreatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todo);
            log.info("todo -  {} successfully created", todo);
        }
    }

    @Override
    public List<Todo> getAllTodos() {
        List<Todo> todos = todoRepository.findAll();
        if (todos.size() > 0) {
            log.info("successfully loaded todos {} ", todos);
            return todos;
        } else {
            log.warn("no todos in the database");
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Object> getAllTodos(int page, int size, String todo) {
        List<Todo> todos;
        PageRequest paging = PageRequest.of(page, size);
        Page<Todo> pageTodos;
        if (todo == null) {
            pageTodos = todoRepository.findAll(paging);

        } else {
            pageTodos = todoRepository.findByTodoContainingIgnoreCase(todo, paging);
        }
        todos = pageTodos.getContent();
        Map<String, Object> response = new HashMap<>();
        response.put("todos", todos);
        response.put("currentPage", pageTodos.getNumber());
        response.put("totalItems", pageTodos.getTotalElements());
        response.put("totalPages", pageTodos.getTotalPages());
        return response;
    }

    @Override
    public Todo getTodoById(String id) throws TodoCollectionException {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isPresent()) {
            log.info("found todo - {}", optionalTodo.get());
            return optionalTodo.get();
        } else {
            log.warn("no todo with id - {}", id);
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void updateTodo(String id, Todo todo) throws TodoCollectionException {
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        Optional<Todo> todoWithSameName = todoRepository.findByTodo(todo.getTodo());
        if (optionalTodo.isPresent()) {
            log.info("found todo to update - {}", optionalTodo.get());
            if (todoWithSameName.isPresent() && !todoWithSameName.get().getId().equals(id)) {
                log.warn("there is already a existing todo with that name {}", todo.getTodo());
                throw new TodoCollectionException(TodoCollectionException.TodoAlreadyExists());
            }
            Todo todoToUpdate = optionalTodo.get();
            todoToUpdate.setTodo(todo.getTodo());
            todoToUpdate.setDescription(todo.getDescription());
            todoToUpdate.setCompleted(todo.getCompleted());
            todoToUpdate.setUpdatedAt(new Date(System.currentTimeMillis()));
            todoRepository.save(todoToUpdate);
            log.info("todo updated - {}", todoWithSameName);
        } else {
            log.warn("not found todo with id - {}", id);
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        }
    }

    @Override
    public void deleteTodoById(String id) throws TodoCollectionException {
        log.info("searching for todo with id - {}, to delete", id);
        Optional<Todo> optionalTodo = todoRepository.findById(id);
        if (optionalTodo.isEmpty()) {
            log.warn("todo with id - {} not found", id);
            throw new TodoCollectionException(TodoCollectionException.NotFoundException(id));
        } else {
            log.info("todo with id - {} successfully deleted", id);
            todoRepository.deleteById(id);
        }
    }
}