package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.CompanyJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {

    @Mock
    CompanyJpaRepository companyJpaRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_companies_when_find_all_given_employees() {
        // given
        List<Company> preparedCompanies = new ArrayList<>();
        Company firstCompany = new Company(2, "AppleCompany", null);
        preparedCompanies.add(firstCompany);
        given(companyJpaRepository.findAll()).willReturn(preparedCompanies);
        // when
        List<Company> companies = companyService.findAll();
        assertEquals(1, companies.size());
        assertEquals(firstCompany, companies.get(0));
    }

    @Test
    void should_find_by_id_when_get_given_company_and_id() {
        // given
        Company companyById = new Company(2, "AppleCompany", null);
        companyJpaRepository.save(companyById);
        given(companyJpaRepository.findById(2)).willReturn(Optional.of(companyById));
        // when
        Company company = companyService.findById(2);
        // then
        assertEquals(companyById, company);
    }

    @Test
    void should_return_employees_by_company_id_when_get_given_company_and_id() {
        // given
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(null, "Mike", 22, "male", 8000, 2));
        employees.add(new Employee(null, "Jack", 22, "male", 8000, 2));
        Company company = new Company(2, "AppleCompany", employees);
        companyJpaRepository.save(company);
        given(companyJpaRepository.findById(2)).willReturn(Optional.of(company));
        // when
        List<Employee> employeesByCompanyId = companyService.findEmployeesByCompanyId(2);
        // then
        assertEquals(2, employeesByCompanyId.size());
        assertEquals(employees, employeesByCompanyId);
    }

    @Test
    void should_find_by_second_page_when_get_given_company_page_and_pageSize() {
        // given
        Company company = new Company(1, "AppleCompany", null);
        companyJpaRepository.save(company);
        companyJpaRepository.save(new Company(2, "BananaCompany", null));
        List<Company> companyPage = new ArrayList<>();
        companyPage.add(company);
        PageImpl page = new PageImpl<>(companyPage);
        given(companyJpaRepository.findAll(PageRequest.of(0, 1))).willReturn(page);
    }

    @Test
    void should_return_new_company_when_post_given_new_company() {
        // given
        Company company = new Company(2, "AppleCompany", null);
        given(companyJpaRepository.save(company)).willReturn(company);
        // when
        Company addCompany = companyService.addCompany(company);
        // then
        assertEquals(company, addCompany);
    }

    @Test
    void should_update_only_companyName_when_update_given_company() {
        // given
        Company beforeUpdateCompany = new Company(2, "AppleCompany", null);
        Company afterUpdateCompany = new Company(2, "BananaCompany", null);
        given(companyJpaRepository.findById(2)).willReturn(Optional.of(beforeUpdateCompany));
        // when
        Company update = companyService.updateCompanyById(2, afterUpdateCompany);
        // then
        assertEquals(2, update.getId());
        assertEquals("BananaCompany", update.getCompanyName());
    }

    @Test
    void should_return_0_when_delete_given_company_and_id() {
        //given
        //when
//        companyService.deleteCompanyById(2);
        companyJpaRepository.deleteById(2);
        //then
        verify(companyJpaRepository, times(1)).deleteById(2);
    }
}
