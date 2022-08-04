package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.entity.Todo;
import com.rest.springbootemployee.repository.TodoJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class TodoControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    TodoJpaRepository todoJpaRepository;

    private Todo preparedTodo;

    @BeforeEach
    void clearTodoInRepository() {
        preparedTodo = new Todo();
        todoJpaRepository.deleteAll();
        Todo todo = new Todo();
        todo.setDone(false);
        todoJpaRepository.flush();
    }

    @Test
    void should_get_all_todos_when_perform_get_given_todos() throws Exception {
        preparedTodo.setText("Test Todo Test!!!");
        todoJpaRepository.save(preparedTodo);
        client.perform(MockMvcRequestBuilders.get("/todos"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].text").value("Test Todo Test!!!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].done").value(false));
    }

    @Test
    void should_find_todo_by_id_when_get_given_todo_id() throws Exception {
        preparedTodo.setText("Test Todo Test!!!");
        todoJpaRepository.save(preparedTodo);
        client.perform(MockMvcRequestBuilders.get("/todos/{id}", preparedTodo.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Test Todo Test!!!"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false));
    }

    @Test
    void should_find_todo_by_id_when_get_given_todo_not_exist_id() throws Exception {
        // given
        // when
        client.perform(MockMvcRequestBuilders.get("/todo/{id}", 88))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        // should
    }

    @Test
    void should_create_a_new_todo_when_perform_post_given_a_new_todo() throws Exception {
        // given
        String newTodoJson = "{\n" +
                "    \"text\" : \"Text Todo\"\n" +
                "}";
        //when
        client.perform(MockMvcRequestBuilders.post("/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newTodoJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("Text Todo"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false));
        //then
    }

    @Test
    void should_update_todo_by_id_when_perform_put_given_a_new_todo() throws Exception {
        preparedTodo.setText("Test Todo Test!!!");
        Todo save = todoJpaRepository.save(preparedTodo);
        String updateTodoJson = "{\n" +
                "    \"text\" : \"test\"\n" +
                "}";
        // when
        client.perform(MockMvcRequestBuilders.put("/todos/{id}", save.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateTodoJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.text").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.done").value(false));
        //then
    }

    @Test
    void should_return_null_when_perform_delete_given_delete_todo_by_id() throws Exception {
        // given
        preparedTodo.setText("Test Todo Test!!!");
        Todo save = todoJpaRepository.save(preparedTodo);
        //when
        client.perform(MockMvcRequestBuilders.delete("/todos/{id}", preparedTodo.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        //then
        List<Todo> todos = todoJpaRepository.findAll();
        assertThat(todos.size(), equalTo(0));
    }
}
