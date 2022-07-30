package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.controller.dto.CompanyRequest;
import com.rest.springbootemployee.controller.dto.CompanyResponse;
import com.rest.springbootemployee.controller.dto.EmployeeResponse;
import com.rest.springbootemployee.controller.mapper.CompanyMapper;
import com.rest.springbootemployee.controller.mapper.EmployeeMapper;
import com.rest.springbootemployee.entity.Company;
import com.rest.springbootemployee.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @Autowired
    private CompanyMapper companyMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping
    public List<CompanyResponse> findAll() {
        return companyService.findAll().stream()
                .map(company -> companyMapper.toResponse(company))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public CompanyResponse findById(@PathVariable Integer id){
        return companyMapper.toResponse(companyService.findById(id));
    }

    @GetMapping("/{id}/employees")
    public List<EmployeeResponse> findEmployeesByCompanyId(@PathVariable Integer id){
        return companyService.findEmployeesByCompanyId(id).stream()
                .map(employee -> employeeMapper.toResponse(employee))
                .collect(Collectors.toList());
    }

    @GetMapping(params = {"page", "pageSize"})
    public Page<CompanyResponse> findByPage(@RequestParam Integer page, @RequestParam Integer pageSize) {
        List<Company> companies = companyService.findByPage(page, pageSize).toList();
        List<CompanyResponse> collect = companies.stream()
                .map(company -> companyMapper.toResponse(company))
                .collect(Collectors.toList());
        return new PageImpl<CompanyResponse>(collect);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company addCompany(@RequestBody CompanyRequest companyRequest){
        return companyService.addCompany(companyMapper.toEntity(companyRequest));
    }

    @PutMapping("/{id}")
    public Company updateCompanyById(@PathVariable Integer id, @RequestBody Company company) {
        return companyService.updateCompanyById(id, company);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompanyById(@PathVariable Integer id) {
        companyService.deleteCompanyById(id);
    }
}
