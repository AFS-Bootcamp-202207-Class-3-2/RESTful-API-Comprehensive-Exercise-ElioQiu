package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.exception.EmployeeNotFoundException;
import com.rest.springbootemployee.repository.EmployeeJpaRepository;
import com.rest.springbootemployee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeJpaRepository employeeJpaRepository;

    public List<Employee> findAll() {
        return employeeJpaRepository.findAll();
    }

    public Employee update(int id, Employee employeeToUpdate) {
        Employee employee = findById(id);
        if (employeeToUpdate.getAge() != null) {
            employee.setAge(employeeToUpdate.getAge());
        }
        if (employeeToUpdate.getSalary() != null) {
            employee.setSalary(employeeToUpdate.getSalary());
        }
        employeeJpaRepository.save(employee);
        return employee;
    }

    public Page<Employee> findByPage(int page, int pageSize) {
        return employeeJpaRepository.findAll(PageRequest.of(page, pageSize));
    }

    public Employee findById(int id) {
        return employeeJpaRepository.findById(id)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> findByGender(String gender) {
        return employeeJpaRepository.findByGender(gender);
    }

    public Employee addEmployee(Employee newEmployee) {
        return employeeJpaRepository.save(newEmployee);
    }

    public void deleteEmployee(int id) {
        employeeJpaRepository.delete(findById(id));
//        employeeJpaRepository.deleteById(id);
    }
}
