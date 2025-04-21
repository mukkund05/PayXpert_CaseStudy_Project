package com.hexaware.dao;

import com.hexaware.entity.Employee;
import com.hexaware.entity.Payroll;
import com.hexaware.exception.InvalidInputException;

public class ValidationService {
    public static void validateEmployee(Employee employee) throws InvalidInputException {
        if (employee == null) throw new InvalidInputException("Employee object cannot be null");
        if (employee.getFirstName() == null || employee.getFirstName().trim().isEmpty())
            throw new InvalidInputException("First name is required");
        if (employee.getEmail() == null || !employee.getEmail().contains("@"))
            throw new InvalidInputException("Valid email is required");
        // Add more validation rules as needed
    }

    public static void validatePayroll(Payroll payroll) throws InvalidInputException {
        if (payroll == null) throw new InvalidInputException("Payroll object cannot be null");
        if (payroll.getBasicSalary() < 0) throw new InvalidInputException("Basic salary cannot be negative");
        // Add more validation rules as needed
    }
}