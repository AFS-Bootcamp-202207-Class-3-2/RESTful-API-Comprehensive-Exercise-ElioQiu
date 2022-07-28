package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.repository.CompanyJpaRepository;
import com.rest.springbootemployee.repository.CompanyRepository;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class CompanyControllerTest {
    private final EmployeeRepository employeeRepository = new EmployeeRepository();

    @Autowired
    MockMvc client;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    private CompanyJpaRepository companyJpaRepository;

    @BeforeEach
    void clearCompanyInRepository() {
        companyJpaRepository.deleteAll();
    }

    @Test
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        // given
        companyJpaRepository.save(new Company(2, "AppleCompany", 2,
                employeeRepository.getEmployeesByIds(Stream.of(3, 4).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].companyName").value("AppleCompany"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[0].name").value("Lucy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[1].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[1].name").value("Jack"));
        // should
    }

    @Test
    void should_find_company_by_id_when_get_given_company_id() throws Exception {
        // given
        companyRepository.addCompany(new Company(2, "AppleCompany", 2,
                employeeRepository.getEmployeesByIds(Stream.of(3, 4).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("AppleCompany"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].name").value("Lucy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].name").value("Jack"));
        // should
    }

    @Test
    void should_find_company_by_id_when_get_given_company_not_exist_id() throws Exception {
        // given
        companyRepository.addCompany(new Company(2, "AppleCompany", 2,
                employeeRepository.getEmployeesByIds(Stream.of(3, 4).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        // should
    }

    @Test
    void should_return_employees_when_get_given_company_id_and_company() throws Exception {
        // given
        companyRepository.addCompany(new Company(2, "AppleCompany", 2,
                employeeRepository.getEmployeesByIds(Stream.of(3, 4).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees", 0))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Lucy", "Jack")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", everyItem(is(22))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", containsInAnyOrder("female", "male")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", everyItem(is(8000))));
    }

    @Test
    void should_return_companies_by_page_when_perform_get_given_companies_by_page_and_pageSize() throws Exception{
        // given
        companyRepository.addCompany(new Company(1, "BananaCompany", 1,
                employeeRepository.getEmployeesByIds(Stream.of(1, 2).collect(Collectors.toList()))));
        companyRepository.addCompany(new Company(2, "AppleCompany", 2,
                employeeRepository.getEmployeesByIds(Stream.of(3, 4).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies")
                        .param("page", "2")
                        .param("pageSize", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].companyName").value("AppleCompany"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[0].name").value("Lucy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[1].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[1].name").value("Jack"));
        // should
    }

    @Test
    void should_create_a_new_company_when_perform_post_given_a_new_company() throws Exception {
        // given
        String newCompanyJson = "{\n" +
                "    \"companyName\" : \"PearCompany\",\n" +
                "    \"employees\": [\n" +
                "        {\n" +
                "            \"name\": \"Jackson\",\n" +
                "            \"age\": 44,\n" +
                "            \"gender\": \"male\",\n" +
                "            \"salary\": 20000\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        // when
        client.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCompanyJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("PearCompany"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees.size()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].name").value("Jackson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].age").value(44))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].gender").value("male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].salary").value(20000));
    }

    @Test
    void should_update_employees_by_id_when_perform_put_given_a_new_employee() throws Exception {
        // given
        companyRepository.addCompany(new Company(1, "AppleCompany", 1,
                employeeRepository.getEmployeesByIds(Stream.of(1, 2).collect(Collectors.toList()))));
        String updateCompanyJson = "{\n" +
                "    \"companyName\" : \"PearCompany\"\n" +
                "}";
        // when
        client.perform(MockMvcRequestBuilders.put("/companies/{id}", 0)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateCompanyJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("PearCompany"));
        //then
    }

    @Test
    void should_return_null_when_perform_delete_given_delete_company_by_id() throws Exception {
        // given
        companyRepository.addCompany(new Company(1, "AppleCompany", 1,
                employeeRepository.getEmployeesByIds(Stream.of(1, 2).collect(Collectors.toList()))));
        //when
        client.perform(MockMvcRequestBuilders.delete("/companies/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        //then
        List<Company> companies = companyRepository.findAll();
        assertThat(companies.size(), equalTo(0));
    }
}
