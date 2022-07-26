package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee findById(@PathVariable Integer id) {
        return employeeRepository.findById(id);
    }

    @GetMapping(params = "gender")
    public List<Employee> findByGender(String gender) {
        return employeeRepository.findByGender(gender);
    }

    @GetMapping(params = {"page", "pageSize"})
    public List<Employee> findByPage(Integer page, Integer pageSize) {
        return employeeRepository.findByPage(page, pageSize);
    }
}
