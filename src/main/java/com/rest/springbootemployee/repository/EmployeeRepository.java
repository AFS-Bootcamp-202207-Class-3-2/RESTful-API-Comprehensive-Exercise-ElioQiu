package com.rest.springbootemployee.repository;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.exception.EmployeeNotFound;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {

    private final List<Employee> employees;
    public EmployeeRepository() {
        this.employees = new ArrayList<>();
        employees.add(new Employee(1, "Mike", 22, "male", 8000));
        employees.add(new Employee(2, "Lucy", 22, "female", 8000));
        employees.add(new Employee(3, "Jack", 22, "male", 8000));
        employees.add(new Employee(4, "Lisa", 22, "female", 8000));
        employees.add(new Employee(5, "Tom", 22, "male", 8000));
        employees.add(new Employee(6, "Tomi", 22, "male", 8000));
    }

    public List<Employee> findAll() {
        return employees;
    }

    public Employee findById(Integer id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(EmployeeNotFound::new);
    }

    public List<Employee> findByGender(String gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }
}
