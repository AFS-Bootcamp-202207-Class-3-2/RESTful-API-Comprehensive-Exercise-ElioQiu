package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.CompanyJpaRepository;
import com.rest.springbootemployee.repository.EmployeeJpaRepository;
import org.hamcrest.Matchers;
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
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class EmployeeControllerTest {

    @Autowired
    MockMvc client;

    @Autowired
    EmployeeJpaRepository employeeJpaRepository;

    @Autowired
    CompanyJpaRepository companyJpaRepository;

    private Company preparedCompany;

    @BeforeEach
    void prepareData() {
        employeeJpaRepository.deleteAll();
        companyJpaRepository.deleteAll();
        Company company = new Company();
        company.setCompanyName("afs");
        preparedCompany = companyJpaRepository.save(company);
    }

    @Test
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
        // given
        employeeJpaRepository.save(new Employee(1, "Mike", 22, "male", 8000, preparedCompany.getId()));

        // when
        client.perform(MockMvcRequestBuilders.get("/employees"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").doesNotExist());
        //should
    }

    @Test
    void should_find_by_id_when_get_given_id() throws Exception {
        // given
        Employee save = employeeJpaRepository.save(
                new Employee(1, "Mike", 22, "male", 8000, preparedCompany.getId()));
        // when
        client.perform(MockMvcRequestBuilders.get("/employees/{id}", save.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").doesNotExist());
        //should
    }

    @Test
    void should_find_by_id_when_get_given_Not_exist_id() throws Exception {
        // given
        // when
        client.perform(MockMvcRequestBuilders.get("/employees/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
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
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").doesNotExist());

        // should
        List<Employee> employees = employeeJpaRepository.findAll();
        assertThat(employees, hasSize(1));
        assertThat(employees.get(0).getName(), equalTo("Mike"));
        assertThat(employees.get(0).getAge(), equalTo(44));
        assertThat(employees.get(0).getGender(), equalTo("male"));
        assertThat(employees.get(0).getSalary(), equalTo(8000));
    }

    @Test
    void should_return_all_male_employees_when_perform_get_by_gender_given_is_male() throws Exception {
        // given
        employeeJpaRepository.save(new Employee(null, "Mike", 22, "male", 8000, preparedCompany.getId()));
        employeeJpaRepository.save(new Employee(null, "Lucy", 22, "female", 8000, preparedCompany.getId()));
        employeeJpaRepository.save(new Employee(null, "Jack", 23, "male", 80000, preparedCompany.getId()));
        // when
        client.perform(MockMvcRequestBuilders.get("/employees")
                .param("gender", "male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Mike", "Jack")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", containsInAnyOrder(22, 23)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", everyItem(is("male"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", everyItem(is(null))));
        // should
    }

    @Test
    void should_update_employees_by_id_when_perform_put_given_a_new_employee() throws Exception {
        //given
        Employee employee = new Employee(1, "Mike", 44, "male", 8000, preparedCompany.getId());
        Employee save = employeeJpaRepository.save(employee);
        String updateEmployeeJson = "{\n" +
                "    \"name\" : \"Mike\",\n" +
                "    \"age\": 22,\n" +
                "    \"gender\": \"male\",\n" +
                "    \"salary\": 80000\n" +
                "}";
        //when
        client.perform(MockMvcRequestBuilders.put("/employees/{id}", save.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateEmployeeJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").doesNotExist());
        //then
        Employee updatedEmployee = employeeJpaRepository.findById(save.getId()).get();
        assertEquals(80000, updatedEmployee.getSalary());
    }
    
    @Test
    void should_return_null_when_perform_delete_given_delete_employee_by_id() throws Exception {
        //given
        //when
        client.perform(MockMvcRequestBuilders.delete("/employees/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        //then
    }

    @Test
    void should_return_employees_by_page_when_perform_get_given_employees_by_page_and_pageSize() throws Exception {
        //given
        employeeJpaRepository.save(new Employee(null, "Mike", 22, "male", 8000,preparedCompany.getId()));
        employeeJpaRepository.save(new Employee(null, "Lucy", 22, "female", 8000, preparedCompany.getId()));
        employeeJpaRepository.save(new Employee(null, "Jack", 22, "male", 8000, preparedCompany.getId()));
        employeeJpaRepository.save(new Employee(null, "Lisa", 22, "female", 8000, preparedCompany.getId()));
        employeeJpaRepository.save(new Employee(null, "Tom", 22, "male", 8000, preparedCompany.getId()));
        employeeJpaRepository.save(new Employee(null, "Susan", 22, "male", 8000, preparedCompany.getId()));
        //when
        client.perform(MockMvcRequestBuilders.get("/employees")
                .param("page", "0")
                .param("pageSize", "10"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content", Matchers.hasSize(6)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].age").value(22))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].salary").doesNotExist());
        //then
    }


}
