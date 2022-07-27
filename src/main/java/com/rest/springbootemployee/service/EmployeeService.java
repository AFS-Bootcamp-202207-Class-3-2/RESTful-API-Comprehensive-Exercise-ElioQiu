package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee update(int id, Employee employeeToUpdate) {
        Employee employee = employeeRepository.findById(id);
        if (employeeToUpdate.getAge() != null) {
            employee.setAge(employeeToUpdate.getAge());
        }
        if (employeeToUpdate.getSalary() != null) {
            employee.setSalary(employeeToUpdate.getSalary());
        }
        return employee;
    }

    public List<Employee> findByPage(int page, int pageSize) {
        return employeeRepository.findByPage(page, pageSize);
    }
}
