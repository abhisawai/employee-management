package com.abhijeet.employeemanagement.controller;

import com.abhijeet.employeemanagement.dto.EmployeeRequest;
import com.abhijeet.employeemanagement.dto.EmployeeResponse;
import com.abhijeet.employeemanagement.dto.PageResponse;
import com.abhijeet.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // CREATE
    @PostMapping
    public EmployeeResponse createEmployee(
            @Valid @RequestBody EmployeeRequest request) {

        return employeeService.createEmployee(request);
    }

    // READ - all
    @GetMapping
    public PageResponse<EmployeeResponse> getAllEmployees(Pageable pageable) {

        return employeeService.getAllEmployees(pageable);
    }

    // READ - by ID
    @GetMapping("/{id}")
    public EmployeeResponse getEmployeeById(
            @PathVariable Long id) {

        return employeeService.getEmployeeById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public EmployeeResponse updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {

        return employeeService.updateEmployee(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);
    }
}