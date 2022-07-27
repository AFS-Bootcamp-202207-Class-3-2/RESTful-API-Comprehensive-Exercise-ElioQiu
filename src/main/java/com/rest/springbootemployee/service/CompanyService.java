package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Company findById(Integer id) {
        return companyRepository.findById(id);
    }

    public List<Employee> findEmployeesByCompanyId(Integer id) {
        return companyRepository.findEmployeesByCompanyId(id);
    }

    public List<Company> findByPage(Integer page, Integer pageSize) {
        return companyRepository.findByPage(page, pageSize);
    }

    public Company addCompany(Company company) {
        return companyRepository.addCompany(company);
    }

    public void deleteCompanyById(Integer id) {
        companyRepository.deleteCompanyById(id);
    }

    public Company updateCompanyById(Integer id, Company company) {
        Company existingCompany = findById(id);
        if (company.getCompanyName() != null) {
            existingCompany.setCompanyName(company.getCompanyName());
        }
        return existingCompany;
    }
}
