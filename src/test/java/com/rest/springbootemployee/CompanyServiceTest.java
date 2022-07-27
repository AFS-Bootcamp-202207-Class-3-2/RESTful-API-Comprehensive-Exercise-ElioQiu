package com.rest.springbootemployee;

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
}
