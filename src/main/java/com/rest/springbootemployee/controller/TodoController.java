package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.entity.Todo;
import com.rest.springbootemployee.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {
    @Autowired
    TodoService todoService;

    @GetMapping
    public List<Todo> findAllTodos() {
        return todoService.findAllTodos();
    }

    @GetMapping("/{id}")
    public Todo findTodoById(@PathVariable Integer id) {
        return todoService.findTodoById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Todo saveTodo(@RequestBody Todo todo) {
        return todoService.saveTodo(todo);
    }

    @PutMapping("/{id}")
    public Todo updateTodoById(@PathVariable Integer id, @RequestBody Todo todo) {
        return todoService.updateTodoById(id, todo);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTodoById(@PathVariable Integer id) {
        todoService.deleteTodoById(id);
    }
}
