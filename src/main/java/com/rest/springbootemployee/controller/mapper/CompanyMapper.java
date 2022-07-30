package com.rest.springbootemployee.controller.mapper;

import com.rest.springbootemployee.controller.dto.CompanyRequest;
import com.rest.springbootemployee.controller.dto.CompanyResponse;
import com.rest.springbootemployee.controller.dto.EmployeeResponse;
import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeJpaRepository;
import com.rest.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private EmployeeJpaRepository employeeJpaRepository;

    public CompanyResponse toResponse(Company company){
        CompanyResponse companyResponse = new CompanyResponse();
        if(company.getEmployees() != null){
            List<EmployeeResponse> employeeResponses = company.getEmployees().stream()
                    .map(employeeMapper::toResponse)
                    .collect(Collectors.toList());
            companyResponse.setEmployees(employeeResponses);
        }
        BeanUtils.copyProperties(company, companyResponse);
        return companyResponse;
    }

    public Company toEntity(CompanyRequest companyRequest){
        Company company = new Company();
        BeanUtils.copyProperties(companyRequest, company);
        List<Integer> existIds = companyRequest.getEmployeeIds().stream()
                .filter(id -> employeeJpaRepository.existsById(id))
                .collect(Collectors.toList());
        companyRequest.setEmployeeIds(existIds);
        List<Employee> employees = companyRequest.getEmployeeIds().stream()
                .map(id -> employeeJpaRepository.findById(id).get())
                .collect(Collectors.toList());
        company.setEmployees(employees);
        return company;
    }
}