package com.rest.springbootemployee;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.repository.CompanyRepository;
import com.rest.springbootemployee.repository.EmployeeRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @BeforeEach
    void clearCompanyInRepository() {
        companyRepository.clearAll();
    }

    @Test
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        // given
        companyRepository.addCompany(new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(2, 3).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].companyName").value("CargoSmart"))
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
        companyRepository.addCompany(new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(2, 3).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", 0))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("CargoSmart"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].name").value("Lucy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].name").value("Jack"));
        // should
    }

    @Test
    void should_return_employees_when_get_given_company_id_and_company() throws Exception {
        // given
        companyRepository.addCompany(new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(2, 3).collect(Collectors.toList()))));
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
        companyRepository.addCompany(new Company(1, "OOCL",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList()))));
        companyRepository.addCompany(new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(2, 3).collect(Collectors.toList()))));
        // when
        client.perform(MockMvcRequestBuilders.get("/companies")
                        .param("page", "2")
                        .param("pageSize", "1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].companyName").value("CargoSmart"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[0].name").value("Lucy"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[1].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[1].name").value("Jack"));
        // should
    }
}
