package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Todo;
import com.rest.springbootemployee.exception.TodoNotFoundException;
import com.rest.springbootemployee.repository.TodoJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    @Autowired
    TodoJpaRepository todoJpaRepository;

    public List<Todo> findAllTodos() {
        return todoJpaRepository.findAll();
    }

    public Todo findTodoById(Integer id) {
        return todoJpaRepository.findById(id)
                .orElseThrow(TodoNotFoundException::new);
    }

    public Todo saveTodo(Todo todo) {
        return todoJpaRepository.save(todo);
    }

    public Todo updateTodoById(Integer id, Todo todo) {
        Todo todoById = findTodoById(id);
        if (todo.getText() != null) {
            todoById.setText(todo.getText());
        }
        if (todo.getDone() != null) {
            todoById.setDone(todo.getDone());
        }
        return todoJpaRepository.save(todoById);
    }

    public void deleteTodoById(Integer id) {
        Todo todoById = findTodoById(id);
        todoJpaRepository.delete(todoById);
    }
}
