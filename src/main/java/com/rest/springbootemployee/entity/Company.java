package com.rest.springbootemployee.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Company {
    private Integer id;
    private String companyName;
    private List<Employee> employees;
}
