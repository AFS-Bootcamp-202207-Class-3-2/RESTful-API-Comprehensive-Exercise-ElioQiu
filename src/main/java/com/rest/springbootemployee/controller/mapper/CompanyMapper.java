package com.rest.springbootemployee.controller.mapper;

import com.rest.springbootemployee.controller.dto.CompanyRequest;
import com.rest.springbootemployee.controller.dto.CompanyResponse;
import com.rest.springbootemployee.controller.dto.EmployeeResponse;
import com.rest.springbootemployee.entity.Company;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    @Autowired
    private EmployeeMapper employeeMapper;

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
        return company;
    }
}