package com.abhijeet.employeemanagement.service;

import com.abhijeet.employeemanagement.entity.Employee;
import com.abhijeet.employeemanagement.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService (EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //CREATE
    public Employee createEmployee (Employee employee) {
        return employeeRepository.save(employee);
    }

    //READ - all
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    //READ - by id
    public Employee getEmployeeById (Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);

        if(employee.isPresent()) {
            return employee.get();
        }

        throw new RuntimeException("Employee not found with id: " + id);
    }

    //UPDATE
    public Employee updateEmployee(Long id, Employee updatedEmployee) {

        Employee existingEmployee = getEmployeeById(id);

        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setDesignation(updatedEmployee.getDesignation());
        existingEmployee.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(existingEmployee);
    }

    //DELETE
    public void deleteEmployee(Long id) {

        Employee existingEmployee = getEmployeeById(id);

        employeeRepository.delete(existingEmployee);

    }

}
