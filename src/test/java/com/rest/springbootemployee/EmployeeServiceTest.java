package com.rest.springbootemployee;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeRepository;
import com.rest.springbootemployee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    @Test
    void should_return_all_employees_when_find_all_given_employees() {
        //given
        List<Employee> preparedEmployees = new ArrayList<>();
        Employee firstEmployee = new Employee(1, "Lisa", 23, "female", 8000);
        Employee secondEmployee = new Employee(2, "Jack", 25, "male", 10000);
        preparedEmployees.add(firstEmployee);
        preparedEmployees.add(secondEmployee);
//        employeeRepository.addEmployee(firstEmployee);
//        employeeRepository.addEmployee(secondEmployee);
        given(employeeRepository.findAll()).willReturn(preparedEmployees);

        //when
        List<Employee> employees = employeeService.findAll();
        //then
        assertEquals(2, employees.size());
        assertEquals(firstEmployee, employees.get(0));
    }

    @Test
    void should_update_only_age_and_salary_when_update_given_employee() {
        //given
        Employee employeeToUpdate = new Employee(1, "Susan", 20, "female", 8000);
        Employee employeeAfterUpdate = new Employee(1, "Mike", 40, "male", 28000);
        given(employeeRepository.findById(1)).willReturn(employeeToUpdate);
        
        //when
        employeeService.update(1, employeeAfterUpdate);
        //then
        assertEquals("Susan", employeeToUpdate.getName());
        assertEquals(40, employeeToUpdate.getAge());
        assertEquals("female", employeeToUpdate.getGender());
        assertEquals(28000, employeeToUpdate.getSalary());
    }

    @Test
    void should_find_by_id_when_get_given_employee_and_id() {
        //given
        Employee employeeById = new Employee(2, "Lucy", 22, "female", 8000);
        employeeRepository.addEmployee(employeeById);
        employeeRepository.addEmployee(new Employee(1, "Mike", 22, "male", 8000));
        given(employeeRepository.findById(2)).willReturn(employeeById);
        //when
        Employee employee = employeeService.findById(2);
        //then
        assertEquals(employeeById, employee);
    }

    @Test
    void should_find_by_gender_when_get_given_employee_and_gender() {
        //given
        List<Employee> maleEmployees = new ArrayList<>();
        Employee employeeByGender1 = new Employee(1, "Mike", 22, "male", 8000);
        Employee employeeByGender2 = new Employee(3, "Jack", 22, "male", 8000);
        employeeRepository.addEmployee(employeeByGender1);
        employeeRepository.addEmployee(employeeByGender2);
        employeeRepository.addEmployee(new Employee(2, "Lucy", 22, "female", 8000));
        maleEmployees.add(employeeByGender1);
        maleEmployees.add(employeeByGender2);
        given(employeeRepository.findByGender("male")).willReturn(maleEmployees);
        //when
        List<Employee> employees = employeeService.findByGender("male");
        //then
        assertEquals(2, employees.size());
        assertEquals(maleEmployees, employees);
    }

    @Test
    void should_find_by_second_page_when_get_given_employee_page_and_pageSize() {
        //given
        Employee firstEmployee = new Employee(3, "Jack", 22, "male", 8000);
        Employee secondEmployee = new Employee(4, "Lisa", 22, "female", 8000);
        employeeRepository.addEmployee(new Employee(1, "Mike", 22, "male", 8000));
        employeeRepository.addEmployee(new Employee(2, "Lucy", 22, "female", 8000));
        employeeRepository.addEmployee(firstEmployee);
        employeeRepository.addEmployee(secondEmployee);
        List<Employee> secondPageEmployee = new ArrayList<>();
        secondPageEmployee.add(firstEmployee);
        secondPageEmployee.add(secondEmployee);
        given(employeeRepository.findByPage(2, 2)).willReturn(secondPageEmployee);
        //when
        List<Employee> employees = employeeService.findByPage(2, 2);
        //then
        assertEquals(2, employees.size());
        assertEquals(firstEmployee, employees.get(0));
        assertEquals(secondEmployee, employees.get(1));
    }

    @Test
    void should_return_new_employee_when_post_given_new_employee() {
        //given
        Employee newEmployee = new Employee(1, "Mike", 22, "male", 8000);
        given(employeeRepository.addEmployee(newEmployee)).willReturn(newEmployee);
        //when
        Employee employees = employeeService.addEmployee(newEmployee);
        //then
        assertEquals(newEmployee, employees);
        //then
    }

    @Test
    void should_return_0_when_delete_given_employee_and_id() {
        //given
        Employee employee = new Employee(1, "Mike", 22, "male", 8000);
        employeeRepository.addEmployee(employee);
        //when
        employeeService.deleteEmployee(1);
        //then
        verify(employeeRepository, times(1)).deleteById(1);
    }

}
