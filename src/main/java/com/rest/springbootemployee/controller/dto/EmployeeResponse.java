package com.rest.springbootemployee.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeResponse {
    private Integer id;
    private String name;
    private Integer age;
    private String gender;
}
