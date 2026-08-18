package com.abhijeet.employeemanagement.service;

import com.abhijeet.employeemanagement.dto.EmployeeRequest;
import com.abhijeet.employeemanagement.dto.EmployeeResponse;
import com.abhijeet.employeemanagement.dto.PageResponse;
import com.abhijeet.employeemanagement.entity.Employee;
import com.abhijeet.employeemanagement.exception.EmployeeNotFoundException;
import com.abhijeet.employeemanagement.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        employeeService = new EmployeeService(employeeRepository);
    }


    // =========================================================
    // CREATE
    // =========================================================

    @Test
    void createEmployee_shouldSaveAndReturnEmployee() {

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Abhijeet");
        request.setLastName("Sawai");
        request.setEmail("abhijeet@example.com");
        request.setDepartment("IT");
        request.setDesignation("Software Engineer");
        request.setSalary(60000.0);

        Employee savedEmployee = new Employee(
                "Abhijeet",
                "Sawai",
                "abhijeet@example.com",
                "IT",
                "Software Engineer",
                60000.0
        );

        savedEmployee.setId(1L);

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponse result =
                employeeService.createEmployee(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Abhijeet", result.getFirstName());
        assertEquals("Sawai", result.getLastName());
        assertEquals("abhijeet@example.com", result.getEmail());

        verify(employeeRepository).save(any(Employee.class));
    }


    // =========================================================
    // GET BY ID
    // =========================================================

    @Test
    void getEmployeeById_shouldReturnEmployee() {

        Employee employee = new Employee(
                "Rahul",
                "Sharma",
                "rahul@example.com",
                "IT",
                "Software Engineer",
                60000.0
        );

        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        EmployeeResponse result =
                employeeService.getEmployeeById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Rahul", result.getFirstName());
        assertEquals("Sharma", result.getLastName());

        verify(employeeRepository).findById(1L);
    }


    @Test
    void getEmployeeById_shouldThrowExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeById(1L)
        );

        assertEquals(
                "Employee not found with id: 1",
                exception.getMessage()
        );

        verify(employeeRepository).findById(1L);
    }


    // =========================================================
    // UPDATE
    // =========================================================

    @Test
    void updateEmployee_shouldUpdateAndReturnEmployee() {

        Employee existingEmployee = new Employee(
                "Rahul",
                "Sharma",
                "rahul@example.com",
                "IT",
                "Software Engineer",
                60000.0
        );

        existingEmployee.setId(1L);

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Abhijeet");
        request.setLastName("Sawai");
        request.setEmail("abhijeet@example.com");
        request.setDepartment("Engineering");
        request.setDesignation("Senior Software Engineer");
        request.setSalary(80000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(existingEmployee))
                .thenReturn(existingEmployee);

        EmployeeResponse result =
                employeeService.updateEmployee(1L, request);

        assertEquals(1L, result.getId());
        assertEquals("Abhijeet", result.getFirstName());
        assertEquals("Sawai", result.getLastName());
        assertEquals("abhijeet@example.com", result.getEmail());
        assertEquals("Engineering", result.getDepartment());
        assertEquals("Senior Software Engineer", result.getDesignation());
        assertEquals(80000.0, result.getSalary());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(existingEmployee);
    }


    @Test
    void updateEmployee_shouldThrowExceptionWhenEmployeeDoesNotExist() {

        EmployeeRequest request = new EmployeeRequest();

        request.setFirstName("Abhijeet");
        request.setLastName("Sawai");
        request.setEmail("abhijeet@example.com");
        request.setDepartment("IT");
        request.setDesignation("Software Engineer");
        request.setSalary(60000.0);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(1L, request)
        );

        assertEquals(
                "Employee not found with id: 1",
                exception.getMessage()
        );

        verify(employeeRepository).findById(1L);
        verify(employeeRepository, never())
                .save(any(Employee.class));
    }


    // =========================================================
    // DELETE
    // =========================================================

    @Test
    void deleteEmployee_shouldDeleteEmployee() {

        Employee employee = new Employee(
                "Rahul",
                "Sharma",
                "rahul@example.com",
                "IT",
                "Software Engineer",
                60000.0
        );

        employee.setId(1L);

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    }


    @Test
    void deleteEmployee_shouldThrowExceptionWhenEmployeeDoesNotExist() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.empty());

        EmployeeNotFoundException exception = assertThrows(
                EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(1L)
        );

        assertEquals(
                "Employee not found with id: 1",
                exception.getMessage()
        );

        verify(employeeRepository).findById(1L);

        verify(employeeRepository, never())
                .delete(any(Employee.class));
    }


    // =========================================================
    // GET ALL - PAGINATION / FILTERING
    // =========================================================

    @Test
    void getAllEmployees_shouldReturnPagedEmployees() {

        Employee employee1 = new Employee(
                "Rahul",
                "Sharma",
                "rahul@example.com",
                "IT",
                "Software Engineer",
                60000.0
        );

        employee1.setId(1L);

        Employee employee2 = new Employee(
                "Abhijeet",
                "Sawai",
                "abhijeet@example.com",
                "IT",
                "Software Engineer",
                70000.0
        );

        employee2.setId(2L);

        List<Employee> employees =
                List.of(employee1, employee2);

        Pageable pageable = PageRequest.of(0, 2);

        Page<Employee> employeePage =
                new PageImpl<>(
                        employees,
                        pageable,
                        2
                );

        when(employeeRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(employeePage);

        PageResponse<EmployeeResponse> result =
                employeeService.getAllEmployees(
                        "IT",
                        "Software Engineer",
                        pageable
                );

        assertNotNull(result);

        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPage());
        assertEquals(2, result.getSize());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        assertEquals(
                "Rahul",
                result.getContent().get(0).getFirstName()
        );

        assertEquals(
                "Abhijeet",
                result.getContent().get(1).getFirstName()
        );

        verify(employeeRepository).findAll(
                any(Specification.class),
                eq(pageable)
        );
    }
}