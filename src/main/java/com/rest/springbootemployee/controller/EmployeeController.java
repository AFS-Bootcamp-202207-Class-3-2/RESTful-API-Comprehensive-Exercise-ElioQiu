package com.rest.springbootemployee.controller;

import com.rest.springbootemployee.controller.dto.EmployeeRequest;
import com.rest.springbootemployee.controller.dto.EmployeeResponse;
import com.rest.springbootemployee.controller.mapper.EmployeeMapper;
import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeRepository;
import com.rest.springbootemployee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeMapper employeeMapper;

    @GetMapping
    public List<EmployeeResponse> findAll() {
        return employeeService.findAll().stream()
                .map(employee -> employeeMapper.toResponse(employee))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public EmployeeResponse findById(@PathVariable Integer id) {
        return employeeMapper.toResponse(employeeService.findById(id));
    }

    @GetMapping(params = "gender")
    public List<EmployeeResponse> findByGender(String gender) {
        return employeeService.findByGender(gender)
                .stream()
                .map(employee -> employeeMapper.toResponse(employee))
                .collect(Collectors.toList());
    }

    @GetMapping(params = {"page", "pageSize"})
    public Page<EmployeeResponse> findByPage(Integer page, Integer pageSize) {
        List<Employee> employees = employeeService.findByPage(page, pageSize).toList();
        List<EmployeeResponse> collect = employees.stream()
                .map(employee -> employeeMapper.toResponse(employee))
                .collect(Collectors.toList());
        return new PageImpl<EmployeeResponse>(collect);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse addEmployee(@RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeMapper.toEntity(employeeRequest);
        return employeeMapper.toResponse(employeeService.addEmployee(employee));
    }

    @PutMapping("/{id}")
    public EmployeeResponse updateEmployeeById(@PathVariable Integer id, @RequestBody EmployeeRequest employeeRequest) {
        return employeeMapper.toResponse(employeeService.update(id, employeeMapper.toEntity(employeeRequest)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployeeById(@PathVariable Integer id) {
        employeeService.deleteEmployee(id);
    }
}
