package com.example.springbootmongodb.repository;

import com.example.springbootmongodb.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {

    @Query("{'todo' : ?0}")
    Optional<Todo> findByTodo(String todo);

    Page<Todo> findByTodoContainingIgnoreCase(String todo, Pageable pageable);

}
