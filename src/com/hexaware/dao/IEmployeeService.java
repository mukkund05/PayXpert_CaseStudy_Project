package com.hexaware.dao;

import com.hexaware.entity.Employee;
import com.hexaware.exception.EmployeeNotFoundException;
import com.hexaware.exception.InvalidInputException;

import java.util.List;

public interface IEmployeeService {
    Employee getEmployeeById(int employeeID) throws EmployeeNotFoundException;
    List<Employee> getAllEmployees() throws EmployeeNotFoundException;
    boolean addEmployee(Employee employee) throws InvalidInputException;
    boolean updateEmployee(Employee employee) throws EmployeeNotFoundException, InvalidInputException;
    boolean removeEmployee(int employeeID) throws EmployeeNotFoundException;
}