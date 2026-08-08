package com.abhijeet.employeemanagement.controller;

import com.abhijeet.employeemanagement.dto.EmployeeRequest;
import com.abhijeet.employeemanagement.dto.EmployeeResponse;
import com.abhijeet.employeemanagement.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            @RequestBody EmployeeRequest request) {

        return employeeService.createEmployee(request);
    }

    // READ - all
    @GetMapping
    public List<EmployeeResponse> getAllEmployees() {

        return employeeService.getAllEmployees();
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
            @RequestBody EmployeeRequest request) {

        return employeeService.updateEmployee(id, request);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {

        employeeService.deleteEmployee(id);
    }
}