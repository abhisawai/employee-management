package com.abhijeet.employeemanagement.controller;

import com.abhijeet.employeemanagement.entity.Employee;
import com.abhijeet.employeemanagement.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService =employeeService;
    }

    //CREATE
    @PostMapping
    public Employee createEmployee (@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    //READ - all
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    //READ - by ID
    @GetMapping("/{id}")
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    //UPDATE
    @PutMapping("/{id}")
    public Employee updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee) {

        return employeeService.updateEmployee(id, employee);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
    }

}
