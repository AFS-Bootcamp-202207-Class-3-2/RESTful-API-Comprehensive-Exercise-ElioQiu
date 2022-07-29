package com.rest.springbootemployee.service;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class EmployeeServiceTest {

    @Mock
    EmployeeJpaRepository employeeJpaRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees() {
        //given
        List<Employee> preparedEmployees = new ArrayList<>();
        Employee firstEmployee = new Employee(1, "Lisa", 23, "female", 8000, 1);
        Employee secondEmployee = new Employee(2, "Jack", 25, "male", 10000, 1);
        preparedEmployees.add(firstEmployee);
        preparedEmployees.add(secondEmployee);
        given(employeeJpaRepository.findAll()).willReturn(preparedEmployees);
        //when
        List<Employee> employees = employeeService.findAll();
        //then
        assertEquals(2, employees.size());
        assertEquals(firstEmployee, employees.get(0));
    }

    @Test
    void should_update_only_age_and_salary_when_update_given_employee() {
        //given
        Employee employeeToUpdate = new Employee(null, "Susan", 20, "female", 8000, 1);
        Employee employeeAfterUpdate = new Employee(null, "Mike", 40, "male", 28000, 1);
        given(employeeJpaRepository.findById(1)).willReturn(Optional.of(employeeToUpdate));
        //when
        Employee update = employeeService.update(1, employeeAfterUpdate);
        //then
        assertEquals("Susan", update.getName());
        assertEquals(40, update.getAge());
        assertEquals("female", update.getGender());
        assertEquals(28000, update.getSalary());
    }

    @Test
    void should_find_by_id_when_get_given_employee_and_id() {
        //given
        Employee employeeById =
                new Employee(1, "Lucy", 22, "female", 8000, 1);
        given(employeeJpaRepository.findById(1)).willReturn(Optional.of(employeeById));
        //when
        Employee employee = employeeService.findById(employeeById.getId());
        //then
        assertEquals(employeeById, employee);
    }

    @Test
    void should_find_by_gender_when_get_given_employee_and_gender() {
        //given
        List<Employee> maleEmployees = new ArrayList<>();
        Employee employeeByGender1 = new Employee(1, "Mike", 22, "male", 8000, 1);
        Employee employeeByGender2 = new Employee(3, "Jack", 22, "male", 8000, 2);
        employeeJpaRepository.save(employeeByGender1);
        employeeJpaRepository.save(employeeByGender2);
        employeeJpaRepository.save(new Employee(2, "Lucy", 22, "female", 8000, 1));
        maleEmployees.add(employeeByGender1);
        maleEmployees.add(employeeByGender2);
        given(employeeJpaRepository.findByGender("male")).willReturn(maleEmployees);
        //when
        List<Employee> employees = employeeService.findByGender("male");
        //then
        assertEquals(2, employees.size());
        assertEquals(maleEmployees, employees);
    }

    @Test
    void should_find_by_second_page_when_get_given_employee_page_and_pageSize() {
        //given
        Employee firstEmployee = new Employee(3, "Jack", 22, "male", 8000, 2);
        Employee secondEmployee = new Employee(4, "Lisa", 22, "female", 8000, 2);
        employeeJpaRepository.save(new Employee(1, "Mike", 22, "male", 8000, 1));
        employeeJpaRepository.save(new Employee(2, "Lucy", 22, "female", 8000, 1));
        employeeJpaRepository.save(firstEmployee);
        employeeJpaRepository.save(secondEmployee);
        List<Employee> secondPageEmployee = new ArrayList<>();
        secondPageEmployee.add(firstEmployee);
        secondPageEmployee.add(secondEmployee);
        PageImpl<Employee> employeePage = new PageImpl<>(secondPageEmployee);
        given(employeeJpaRepository.findAll(PageRequest.of(2, 2))).willReturn(employeePage);
        //when
        Page<Employee> employeeByPage = employeeService.findByPage(2, 2);
        //then
        assertEquals(2, employeeByPage.getSize());
        assertEquals(firstEmployee, employeeByPage.getContent().get(0));
        assertEquals(secondEmployee, employeeByPage.getContent().get(1));
    }

    @Test
    void should_return_new_employee_when_post_given_new_employee() {
        //given
        Employee newEmployee = new Employee(1, "Mike", 22, "male", 8000, 1);
        given(employeeJpaRepository.save(newEmployee)).willReturn(newEmployee);
        //when
        Employee employees = employeeService.addEmployee(newEmployee);
        //then
        assertEquals(newEmployee, employees);
    }

    @Test
    void should_return_0_when_delete_given_employee_and_id() {
        //given
        Employee employee = new Employee(1, "Mike", 22, "male", 8000, 1);
        given(employeeJpaRepository.findById(1)).willReturn(Optional.of(employee));
        //when
        employeeService.deleteEmployee(1);
//        employeeJpaRepository.deleteById(1);
        //then
        verify(employeeJpaRepository,times(1)).delete(employee);
    }

}
