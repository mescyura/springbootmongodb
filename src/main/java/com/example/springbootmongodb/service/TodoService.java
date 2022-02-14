package com.example.springbootmongodb.service;

import com.example.springbootmongodb.exception.TodoCollectionException;
import com.example.springbootmongodb.model.Todo;

import javax.validation.ConstraintViolationException;
import java.util.List;

public interface TodoService {

    void CreateTodo(Todo todo) throws ConstraintViolationException, TodoCollectionException;

    List<Todo> getAllTodos();

    Todo getTodoById(String id) throws TodoCollectionException;

    void updateTodo(String id, Todo todo) throws TodoCollectionException;

    void deleteTodoById(String id) throws TodoCollectionException;
}