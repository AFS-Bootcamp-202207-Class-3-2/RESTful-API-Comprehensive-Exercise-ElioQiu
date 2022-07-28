package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.CompanyRepository;
import com.rest.springbootemployee.repository.EmployeeRepository;
import com.rest.springbootemployee.service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CompanyServiceTest {
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    @Mock
    CompanyRepository companyRepository;

    @InjectMocks
    CompanyService companyService;

    @Test
    void should_return_all_companies_when_find_all_given_employees() {
        // given
        List<Company> preparedCompanies = new ArrayList<>();
        Company firstCompany = new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(2, 3).collect(Collectors.toList())));
        preparedCompanies.add(firstCompany);
        given(companyRepository.findAll()).willReturn(preparedCompanies);
        // when
        List<Company> companies = companyService.findAll();
        assertEquals(1, companies.size());
        assertEquals(firstCompany, companies.get(0));
    }

    @Test
    void should_find_by_id_when_get_given_company_and_id() {
        // given
        Company companyById = new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(2, 3).collect(Collectors.toList())));
        companyRepository.addCompany(companyById);
        given(companyRepository.findById(2)).willReturn(companyById);
        // when
        Company company = companyService.findById(2);
        // then
        assertEquals(companyById, company);
    }

    @Test
    void should_return_employees_by_company_id_when_get_given_company_and_id() {
        // given
        Company company = new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList())));
        companyRepository.addCompany(company);
        List<Employee> exceptEmployees = new ArrayList<>();
        exceptEmployees.add(new Employee(1, "Mike", 22, "male", 8000));
        given(companyRepository.findEmployeesByCompanyId(2)).willReturn(exceptEmployees);
        // when
        List<Employee> employeesByCompanyId = companyService.findEmployeesByCompanyId(2);
        // then
        assertEquals(1, employeesByCompanyId.size());
        assertEquals(exceptEmployees, employeesByCompanyId);
    }

    @Test
    void should_find_by_second_page_when_get_given_company_page_and_pageSize() {
        // given
        Company company = new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList())));
        companyRepository.addCompany(company);
        companyRepository.addCompany(new Company(1, "OOCL",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList()))));
        List<Company> secondCompanyPage = new ArrayList<>();
        secondCompanyPage.add(company);
        given(companyRepository.findByPage(2, 1)).willReturn(secondCompanyPage);
        // when
        List<Company> companyByPage = companyService.findByPage(2, 1);
        // then
        assertEquals(1, companyByPage.size());
        assertEquals(secondCompanyPage, companyByPage);
    }

    @Test
    void should_return_new_company_when_post_given_new_company() {
        // given
        Company company = new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList())));
        given(companyRepository.addCompany(company)).willReturn(company);
        // when
        Company addCompany = companyService.addCompany(company);
        // then
        assertEquals(company, addCompany);
    }

    @Test
    void should_update_only_companyName_when_update_given_company() {
        // given
        Company beforeUpdateCompany = new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList())));
        Company afterUpdateCompany = new Company(2, "OOCL",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList())));
        given(companyRepository.findById(2)).willReturn(beforeUpdateCompany);
        // when
        Company update = companyService.updateCompanyById(2, afterUpdateCompany);
        // then
        assertEquals(2, update.getId());
        assertEquals("OOCL", update.getCompanyName());
        assertEquals(employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList())), update.getEmployees());
    }

    @Test
    void should_return_0_when_delete_given_company_and_id() {
        //given
        Company company = new Company(2, "CargoSmart",
                employeeRepository.getEmployeesByIds(Stream.of(1).collect(Collectors.toList())));
        companyRepository.addCompany(company);
        //when
        companyService.deleteCompanyById(2);
        //then
        verify(companyRepository, times(1)).deleteCompanyById(2);
    }
}
