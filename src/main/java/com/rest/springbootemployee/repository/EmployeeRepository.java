package com.rest.springbootemployee.repository;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.exception.EmployeeNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class EmployeeRepository {

    public static final int DEFAULT_MIN_ID = -1;
    private final List<Employee> employees;
    public EmployeeRepository() {
        this.employees = new ArrayList<>();
        employees.add(new Employee(1, "Mike", 22, "male", 8000, 1));
        employees.add(new Employee(2, "Lucy", 22, "female", 8000,1));
        employees.add(new Employee(3, "Jack", 22, "male", 8000,1));
        employees.add(new Employee(4, "Lisa", 22, "female", 8000, 1));
        employees.add(new Employee(5, "Tom", 22, "male", 8000,1));
        employees.add(new Employee(6, "Tomi", 22, "male", 8000,1));
    }

    public List<Employee> findAll() {
        return employees;
    }

    public Employee findById(Integer id) {
        return employees.stream()
                .filter(employee -> employee.getId().equals(id))
                .findFirst()
                .orElseThrow(EmployeeNotFoundException::new);
    }

    public List<Employee> findByGender(String gender) {
        return employees.stream()
                .filter(employee -> employee.getGender().equals(gender))
                .collect(Collectors.toList());
    }

    public List<Employee> findByPage(Integer page, Integer pageSize) {
        return employees.stream()
                .skip((long) (page - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());
    }

    public Employee addEmployee(Employee employee) {
        employee.setId(generateId());
        employees.add(employee);
        return employee;
    }

    private Integer generateId() {
        int maxId = employees.stream()
                .mapToInt(Employee::getId)
                .max().orElse(DEFAULT_MIN_ID);
        return ++maxId;
    }

    public void deleteById(Integer id) {
        employees.remove(findById(id));
    }

    public List<Employee> getEmployeesByIds(List<Integer> ids) {
         return employees.stream()
                .filter(employee -> ids.contains(employee.getId()))
                .collect(Collectors.toList());
    }

    public void clearAll() {
        employees.clear();
    }
}
