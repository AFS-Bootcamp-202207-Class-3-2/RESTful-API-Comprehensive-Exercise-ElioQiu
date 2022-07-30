package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.exception.CompanyNotFoundException;
import com.rest.springbootemployee.repository.CompanyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyJpaRepository companyJpaRepository;

    public List<Company> findAll() {
        return companyJpaRepository.findAll();
    }

    public Company findById(Integer id) {
        return companyJpaRepository.findById(id)
                .orElseThrow(CompanyNotFoundException::new);
    }

    public List<Employee> findEmployeesByCompanyId(Integer id) {
        return findById(id).getEmployees();
    }

    public Page<Company> findByPage(Integer page, Integer pageSize) {
        return companyJpaRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Company addCompany(Company company) {
        return companyJpaRepository.save(company);
    }

    public void deleteCompanyById(Integer id) {
        companyJpaRepository.deleteById(id);
    }

    public Company updateCompanyById(Integer id, Company company) {
        Company existingCompany = findById(id);
        if (company.getCompanyName() != null) {
            existingCompany.setCompanyName(company.getCompanyName());
        }
        companyJpaRepository.save(existingCompany);
        return existingCompany;
    }
}
