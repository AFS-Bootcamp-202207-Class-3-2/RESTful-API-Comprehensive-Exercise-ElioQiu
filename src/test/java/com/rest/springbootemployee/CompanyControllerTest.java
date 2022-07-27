package com.rest.springbootemployee;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.repository.CompanyRepository;
import com.rest.springbootemployee.repository.EmployeeRepository;
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
    void should_get_all_employees_when_perform_get_given_employees() throws Exception {
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
}
