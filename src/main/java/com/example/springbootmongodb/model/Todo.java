package com.example.springbootmongodb.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "todos")
public class Todo {
    @Id
    private String id;
    @NotNull(message = "todo cannot be null")
    private String todo;
    @NotNull(message = "description cannot be null")
    private String description;
    @NotNull(message = "completed cannot be null")
    private Boolean completed;
    private Date createdAt;
    private Date updatedAt;

    @Override
    public String toString() {
        return "TodoDTO{" +
                "id='" + id + '\'' +
                ", todo='" + todo + '\'' +
                ", description='" + description + '\'' +
                ", completed=" + completed +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}