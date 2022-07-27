package com.rest.springbootemployee;

import com.rest.springbootemployee.entity.Employee;
import com.rest.springbootemployee.repository.EmployeeRepository;
import com.rest.springbootemployee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

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


}
