package com.hexaware.dao;

import com.hexaware.entity.*;
import com.hexaware.exception.DatabaseConnectionException;
import com.hexaware.exception.EmployeeNotFoundException;
import com.hexaware.exception.InvalidInputException;
import com.hexaware.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService implements IEmployeeService {
    private Connection conn;

    public EmployeeService() throws DatabaseConnectionException {
        this.conn = DBConnUtil.getConnection("E:\\CASE STUDY\\PayXpert\\PayXpert\\src\\com\\hexaware\\util\\db.properties");
    }

    @Override
    public Employee getEmployeeById(int employeeID) throws EmployeeNotFoundException {
        String sql = "SELECT * FROM Employee WHERE EmployeeID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            } else {
                throw new EmployeeNotFoundException("Employee with ID " + employeeID + " not found");
            }
        } catch (SQLException e) {
            throw new EmployeeNotFoundException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws EmployeeNotFoundException {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM Employee";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
            return employees;
        } catch (SQLException e) {
            throw new EmployeeNotFoundException("Database error: " + e.getMessage());
        }
    }

    @Override
    public boolean addEmployee(Employee employee) throws InvalidInputException {
        if (employee == null || employee.getFirstName() == null || employee.getEmail() == null) {
            throw new InvalidInputException("Invalid employee data");
        }
        String sql = "INSERT INTO Employee (FirstName, LastName, DateOfBirth, Gender, Email, PhoneNumber, Address, Position, JoiningDate, TerminationDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setEmployeeParameters(pstmt, employee);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new InvalidInputException("Failed to add employee: " + e.getMessage());
        }
    }

    @Override
    public boolean updateEmployee(Employee employee) throws EmployeeNotFoundException, InvalidInputException {
        if (employee == null || employee.getEmployeeID() <= 0) {
            throw new InvalidInputException("Invalid employee ID");
        }
        String sql = "UPDATE Employee SET FirstName = ?, LastName = ?, DateOfBirth = ?, Gender = ?, Email = ?, PhoneNumber = ?, Address = ?, Position = ?, JoiningDate = ?, TerminationDate = ? WHERE EmployeeID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setEmployeeParameters(pstmt, employee);
            pstmt.setInt(11, employee.getEmployeeID());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new EmployeeNotFoundException("Employee with ID " + employee.getEmployeeID() + " not found");
            }
            return true;
        } catch (SQLException e) {
            throw new InvalidInputException("Failed to update employee: " + e.getMessage());
        }
    }

    @Override
    public boolean removeEmployee(int employeeID) throws EmployeeNotFoundException {
        String sql = "DELETE FROM Employee WHERE EmployeeID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new EmployeeNotFoundException("Employee with ID " + employeeID + " not found");
            }
            return true;
        } catch (SQLException e) {
            throw new EmployeeNotFoundException("Database error: " + e.getMessage());
        }
    }

    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeID(rs.getInt("EmployeeID"));
        employee.setFirstName(rs.getString("FirstName"));
        employee.setLastName(rs.getString("LastName"));
        employee.setDateOfBirth(rs.getDate("DateOfBirth"));
        employee.setGender(rs.getString("Gender"));
        employee.setEmail(rs.getString("Email"));
        employee.setPhoneNumber(rs.getString("PhoneNumber"));
        employee.setAddress(rs.getString("Address"));
        employee.setPosition(rs.getString("Position"));
        employee.setJoiningDate(rs.getDate("JoiningDate"));
        employee.setTerminationDate(rs.getDate("TerminationDate"));
        return employee;
    }

    private void setEmployeeParameters(PreparedStatement pstmt, Employee employee) throws SQLException {
        pstmt.setString(1, employee.getFirstName());
        pstmt.setString(2, employee.getLastName());
        pstmt.setDate(3, employee.getDateOfBirth());
        pstmt.setString(4, employee.getGender());
        pstmt.setString(5, employee.getEmail());
        pstmt.setString(6, employee.getPhoneNumber());
        pstmt.setString(7, employee.getAddress());
        pstmt.setString(8, employee.getPosition());
        pstmt.setDate(9, employee.getJoiningDate());
        pstmt.setDate(10, employee.getTerminationDate());
    }
}