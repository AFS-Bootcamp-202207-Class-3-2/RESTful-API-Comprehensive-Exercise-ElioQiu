package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.CompanyJpaRepository;
import com.rest.springbootemployee.repository.CompanyRepository;
import com.rest.springbootemployee.repository.EmployeeJpaRepository;
import com.rest.springbootemployee.repository.EmployeeRepository;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "test")
public class CompanyControllerTest {

    @Autowired
    MockMvc client;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CompanyJpaRepository companyJpaRepository;

    @Autowired
    EmployeeJpaRepository employeeJpaRepository;

    private Company preparedCompany;

    @BeforeEach
    void clearCompanyInRepository() {
        companyJpaRepository.deleteAll();
        employeeJpaRepository.deleteAll();
        Company company = new Company();
        company.setCompanyName("AppleCompany");
        preparedCompany = companyJpaRepository.save(company);
        companyJpaRepository.flush();
    }

    Employee prepareMike(){
        Employee employee = new Employee(null, "Mike", 22, "male", 8000,preparedCompany.getId());
        return employeeJpaRepository.save(employee);
    }

    Employee prepareJack(){
        Employee employee = new Employee(null, "Jack", 22, "male", 8000,preparedCompany.getId());
        return employeeJpaRepository.save(employee);
    }

    @Test
    void should_get_all_companies_when_perform_get_given_companies() throws Exception {
        // given
        preparedCompany.setEmployees(Arrays.asList(prepareMike(), prepareJack()));
        companyJpaRepository.save(preparedCompany);
        // when
        client.perform(MockMvcRequestBuilders.get("/companies"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].companyName").value("AppleCompany"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[0].name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].employees[1].name").value("Jack"));
        // should
    }

    @Test
    void should_find_company_by_id_when_get_given_company_id() throws Exception {
        // given
        preparedCompany.setEmployees(Arrays.asList(prepareMike(), prepareJack()));
        companyJpaRepository.save(preparedCompany);
        // when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", preparedCompany.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("AppleCompany"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[0].name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.employees[1].name").value("Jack"));
        // should
    }

    @Test
    void should_find_company_by_id_when_get_given_company_not_exist_id() throws Exception {
        // given
        preparedCompany.setEmployees(Arrays.asList(prepareMike(), prepareJack()));
        companyJpaRepository.save(preparedCompany);
        // when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}", 88))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
        // should
    }

    @Test
    void should_return_employees_when_get_given_company_id_and_company() throws Exception {
        // given
        preparedCompany.setEmployees(Arrays.asList(prepareMike(), prepareJack()));
        companyJpaRepository.save(preparedCompany);
        // when
        client.perform(MockMvcRequestBuilders.get("/companies/{id}/employees", preparedCompany.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].name", containsInAnyOrder("Mike", "Jack")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].age", everyItem(is(22))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].gender", everyItem(is("male"))))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].salary", everyItem(is(8000))));
    }

    @Test
    void should_return_companies_by_page_when_perform_get_given_companies_by_page_and_pageSize() throws Exception{
        // given
        preparedCompany.setEmployees(Arrays.asList(prepareMike(), prepareJack()));
        companyJpaRepository.save(preparedCompany);
        Company company = new Company();
        company.setCompanyName("PearCompany");
        company.setEmployees(null);
        companyJpaRepository.save(company);
        // when
        client.perform(MockMvcRequestBuilders.get("/companies")
                        .param("page", "0")
                        .param("pageSize", "1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].companyName").value("AppleCompany"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employees.size()").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employees[0].name").value("Mike"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].employees[1].name").value("Jack"));
        // should
    }

    @Test
    void should_create_a_new_company_when_perform_post_given_a_new_company() throws Exception {
        // given
        String newCompanyJson = "{\n" +
                "    \"companyName\": \"AppleCompany\"\n" +
                "}\n";
        //when
        client.perform(MockMvcRequestBuilders.post("/companies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCompanyJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("AppleCompany"));
        //then
    }

    @Test
    void should_update_employees_by_id_when_perform_put_given_a_new_employee() throws Exception {
        // given
        preparedCompany.setEmployees(Arrays.asList(prepareMike(), prepareJack()));
        companyJpaRepository.save(preparedCompany);
        String updateCompanyJson = "{\n" +
                "    \"companyName\" : \"PearCompany\"\n" +
                "}";
        // when
        client.perform(MockMvcRequestBuilders.put("/companies/{id}", preparedCompany.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateCompanyJson))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.companyName").value("PearCompany"));
        //then
    }

    @Test
    void should_return_null_when_perform_delete_given_delete_company_by_id() throws Exception {
        // given
        preparedCompany.setEmployees(Arrays.asList(prepareMike(), prepareJack()));
        companyJpaRepository.save(preparedCompany);
        //when
        client.perform(MockMvcRequestBuilders.delete("/companies/{id}", preparedCompany.getId()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        //then
        List<Company> companies = companyJpaRepository.findAll();
        assertThat(companies.size(), equalTo(0));
    }
}
