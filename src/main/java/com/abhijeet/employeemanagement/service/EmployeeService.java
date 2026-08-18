package com.abhijeet.employeemanagement.service;

import com.abhijeet.employeemanagement.dto.EmployeeRequest;
import com.abhijeet.employeemanagement.dto.EmployeeResponse;
import com.abhijeet.employeemanagement.dto.PageResponse;
import com.abhijeet.employeemanagement.entity.Employee;
import com.abhijeet.employeemanagement.exception.EmployeeNotFoundException;
import com.abhijeet.employeemanagement.repository.EmployeeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.abhijeet.employeemanagement.specification.EmployeeSpecification;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // CREATE
    public EmployeeResponse createEmployee(EmployeeRequest request) {

        Employee employee = new Employee(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getDepartment(),
                request.getDesignation(),
                request.getSalary()
        );

        Employee savedEmployee = employeeRepository.save(employee);

        return mapToResponse(savedEmployee);
    }

    // READ - all
    public PageResponse<EmployeeResponse> getAllEmployees(
            String department,
            String designation,
            Pageable pageable) {

        Specification<Employee> specification = null;

        if (department != null && !department.isBlank()) {
            specification = EmployeeSpecification.hasDepartment(department);
        }

        if (designation != null && !designation.isBlank()) {

            Specification<Employee> designationSpecification =
                    EmployeeSpecification.hasDesignation(designation);

            specification = specification == null
                    ? designationSpecification
                    : specification.and(designationSpecification);
        }

        Page<Employee> employees =
                employeeRepository.findAll(specification, pageable);

        Page<EmployeeResponse> employeeResponses =
                employees.map(this::mapToResponse);

        return new PageResponse<>(
                employeeResponses.getContent(),
                employeeResponses.getNumber(),
                employeeResponses.getSize(),
                employeeResponses.getTotalElements(),
                employeeResponses.getTotalPages()
        );
    }

    // READ - by ID
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id)
                );

        return mapToResponse(employee);
    }

    // UPDATE
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id)
                );

        existingEmployee.setFirstName(request.getFirstName());
        existingEmployee.setLastName(request.getLastName());
        existingEmployee.setEmail(request.getEmail());
        existingEmployee.setDepartment(request.getDepartment());
        existingEmployee.setDesignation(request.getDesignation());
        existingEmployee.setSalary(request.getSalary());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return mapToResponse(updatedEmployee);
    }

    // DELETE
    public void deleteEmployee(Long id) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException("Employee not found with id: " + id)
                );

        employeeRepository.delete(existingEmployee);
    }

    // Entity → Response DTO
    private EmployeeResponse mapToResponse(Employee employee) {

        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getSalary()
        );
    }
}