package com.rest.springbootemployee;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    @Autowired
    MockMvc client;

    @Autowired
    EmployeeRepository employeeRepository;

    @BeforeEach
    void clearEmployeeInRepository() {
        employeeRepository.clearAll();
    }

    @Test
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
        // given
        employeeRepository.addEmployee(new Employee(1, "Mike", 22, "male", 8000));
        // when
        client.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(8000));
        //should
    }

    @Test
    void should_create_a_new_employee_when_perform_post_given_a_new_employee() throws Exception {
        // given
        String newEmployeeJson = "{\n" +
                "    \"name\" : \"Mike\",\n" +
                "    \"age\": 44,\n" +
                "    \"gender\": \"male\",\n" +
                "    \"salary\": 8000\n" +
                "}";
        // when
        client.perform(MockMvcRequestBuilders.post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEmployeeJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(44))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(8000));

        // should
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees, hasSize(1));
        assertThat(employees.get(0).getName(), equalTo("Mike"));
        assertThat(employees.get(0).getAge(), equalTo(44));
        assertThat(employees.get(0).getGender(), equalTo("male"));
        assertThat(employees.get(0).getSalary(), equalTo(8000));
    }

    @Test
    void should_return_all_male_employees_when_perform_get_by_gender_given_is_male() throws Exception {
        // given
        employeeRepository.addEmployee(new Employee(1, "Mike", 22, "male", 8000));
        employeeRepository.addEmployee(new Employee(2, "Lucy", 22, "female", 8000));
        employeeRepository.addEmployee(new Employee(3, "Jack", 23, "male", 80000));
        // when
        client.perform(MockMvcRequestBuilders.get("/employees")
                .param("gender", "male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Mike", "Jack")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(22, 23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", everyItem(is("male"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", containsInAnyOrder(8000, 80000)));
        // should
    }

    @Test
    void should_update_employees_by_id_when_perform_put_given_a_new_employee() throws Exception {
        //given
        employeeRepository.addEmployee(new Employee(0, "Mike", 44, "male", 8000));
        String updateEmployeeJson = "{\n" +
                "    \"name\" : \"Mike\",\n" +
                "    \"age\": 22,\n" +
                "    \"gender\": \"male\",\n" +
                "    \"salary\": 80000\n" +
                "}";
        //when
        client.perform(MockMvcRequestBuilders.put("/employees/{id}", 0)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEmployeeJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value(80000));
        //then
    }
    
    @Test
    void should_return_null_when_perform_delete_given_delete_employee_by_id() throws Exception {
        //given
        employeeRepository.addEmployee(new Employee(0, "Mike", 44, "male", 8000));
        //when
        client.perform(MockMvcRequestBuilders.delete("/employees/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        //then
        List<Employee> employees = employeeRepository.findAll();
        assertThat(employees.size(), equalTo(0));
    }

    @Test
    void should_return_employees_by_page_when_perform_get_given_employees_by_page_and_pageSize() throws Exception {
        //given
        employeeRepository.addEmployee(new Employee(1, "Mike", 22, "male", 8000));
        employeeRepository.addEmployee(new Employee(2, "Lucy", 22, "female", 8000));
        employeeRepository.addEmployee(new Employee(3, "Jack", 22, "male", 8000));
        employeeRepository.addEmployee(new Employee(4, "Lisa", 22, "female", 8000));
        employeeRepository.addEmployee(new Employee(5, "Tom", 22, "male", 8000));
        employeeRepository.addEmployee(new Employee(6, "Tomi", 22, "male", 8000));
        //when
        client.perform(MockMvcRequestBuilders.get("/employees")
                .param("page", "2")
                .param("pageSize", "2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Jack"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value(8000));
        //then
    }


}
