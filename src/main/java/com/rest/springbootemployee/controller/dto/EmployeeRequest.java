package com.rest.springbootemployee.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    private String name;
    private Integer age;
    private String gender;
    private Integer salary;
    private Integer companyId;
}
