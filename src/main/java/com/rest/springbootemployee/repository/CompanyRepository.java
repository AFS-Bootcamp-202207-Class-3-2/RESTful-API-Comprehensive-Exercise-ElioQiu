package com.rest.springbootemployee.repository;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.exception.CompanyNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class CompanyRepository {

    public static final int DEFAULT_MIN_ID = -1;
    private final List<Company> companies;

    public CompanyRepository() {
        this.companies = new ArrayList<>();
        companies.add(new Company(1, "OOCL",
                new EmployeeRepository().getEmployeesByIds(Stream.of(1).collect(Collectors.toList()))));
        companies.add(new Company(2, "CargoSmart",
                new EmployeeRepository().getEmployeesByIds(Stream.of(2, 3).collect(Collectors.toList()))));
        companies.add(new Company(3, "IQAX",
                new EmployeeRepository().getEmployeesByIds(Stream.of(4, 5, 6).collect(Collectors.toList()))));
    }

    public List<Company> findAll() {
        return companies;
    }

    public Company findById(Integer id) {
        return companies.stream()
                .filter(company -> company.getId().equals(id))
                .findFirst()
                .orElseThrow(CompanyNotFoundException::new);
    }

    public List<Employee> findEmployeesByCompanyId(Integer id) {
        return findById(id).getEmployees();
    }

    public List<Company> findByPage(Integer page, Integer pageSize) {
        return companies.stream()
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public Company addCompany(Company company) {
        company.setId(generateId());
        companies.add(company);
        return company;
    }

    private Integer generateId() {
        int maxId = companies.stream()
                .mapToInt(Company::getId)
                .max().orElse(DEFAULT_MIN_ID);
        return ++maxId;
    }

    public Company updateCompanyById(Integer id, Company company) {
        Company existingCompany = findById(id);
        if (company.getEmployees() != null) {
            existingCompany.setEmployees(company.getEmployees());
        }
        return existingCompany;
    }
}
