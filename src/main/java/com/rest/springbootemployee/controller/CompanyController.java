package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @GetMapping("/{id}")
    public Company findById(@PathVariable Integer id){
        return companyRepository.findById(id);
    }

    @GetMapping("/{id}/employees")
    public List<Employee> findEmployeesByCompanyId(@PathVariable Integer id){
        return companyRepository.findEmployeesByCompanyId(id);
    }
}
