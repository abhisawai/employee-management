package com.abhijeet.employeemanagement.specification;

import com.abhijeet.employeemanagement.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasDepartment(String department) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("department")),
                        department.toLowerCase()
                );
    }

    public static Specification<Employee> hasDesignation(String designation) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("designation")),
                        designation.toLowerCase()
                );
    }
}