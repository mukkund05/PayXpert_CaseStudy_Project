package com.hexaware.dao;

import com.hexaware.entity.Tax;
import com.hexaware.exception.DatabaseConnectionException;
import com.hexaware.exception.TaxCalculationException;
import com.hexaware.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaxService implements ITaxService {
    private Connection conn;

    public TaxService() throws DatabaseConnectionException {
        this.conn = DBConnUtil.getConnection("E:\\CASE STUDY\\PayXpert\\PayXpert\\src\\com\\hexaware\\util\\db.properties");
    }

    @Override
    public double calculateTax(int employeeID, int taxYear) throws TaxCalculationException {
        double taxableIncome = 0.0;
        double taxAmount = 0.0;

        // Step 1: Fetch the latest gross salary (taxable income) from Payroll table for the given year
        String sqlSelect = "SELECT SUM(BasicSalary + OvertimePay) AS GrossSalary FROM Payroll WHERE EmployeeID = ? AND YEAR(PayPeriodStartDate) = ?";
        try (PreparedStatement pstmtSelect = conn.prepareStatement(sqlSelect)) {
            pstmtSelect.setInt(1, employeeID);
            pstmtSelect.setInt(2, taxYear);
            ResultSet rs = pstmtSelect.executeQuery();
            if (rs.next()) {
                taxableIncome = rs.getDouble("GrossSalary");
                if (taxableIncome <= 0) {
                    throw new TaxCalculationException("No valid payroll data found for employee ID " + employeeID + " in year " + taxYear);
                }
            } else {
                throw new TaxCalculationException("No payroll data found for employee ID " + employeeID + " in year " + taxYear);
            }
        } catch (SQLException e) {
            throw new TaxCalculationException("Failed to fetch payroll data: " + e.getMessage());
        }

        // Step 2: Calculate tax based on progressive tax slabs
        if (taxableIncome <= 50000) {
            taxAmount = 0.0; // 0% tax for income up to $50,000
        } else if (taxableIncome <= 100000) {
            taxAmount = (taxableIncome - 50000) * 0.20; // 20% on income above $50,000
        } else {
            taxAmount = (50000 * 0.00) + (50000 * 0.20) + ((taxableIncome - 100000) * 0.25); // 25% on income above $100,000
        }

        // Step 3: Insert the calculated tax into the Tax table
        String sqlInsert = "INSERT INTO Tax (EmployeeID, TaxYear, TaxableIncome, TaxAmount) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
            pstmtInsert.setInt(1, employeeID);
            pstmtInsert.setInt(2, taxYear);
            pstmtInsert.setDouble(3, taxableIncome);
            pstmtInsert.setDouble(4, taxAmount);
            pstmtInsert.executeUpdate();
            return taxAmount;
        } catch (SQLException e) {
            throw new TaxCalculationException("Failed to calculate and store tax: " + e.getMessage());
        }
    }

    @Override
    public Tax getTaxById(int taxID) throws TaxCalculationException {
        String sql = "SELECT * FROM Tax WHERE TaxID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taxID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToTax(rs);
            } else {
                throw new TaxCalculationException("Tax with ID " + taxID + " not found");
            }
        } catch (SQLException e) {
            throw new TaxCalculationException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Tax> getTaxesForEmployee(int employeeID) throws TaxCalculationException {
        List<Tax> taxes = new ArrayList<>();
        String sql = "SELECT * FROM Tax WHERE EmployeeID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                taxes.add(mapResultSetToTax(rs));
            }
            return taxes;
        } catch (SQLException e) {
            throw new TaxCalculationException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Tax> getTaxesForYear(int taxYear) throws TaxCalculationException {
        List<Tax> taxes = new ArrayList<>();
        String sql = "SELECT * FROM Tax WHERE TaxYear = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, taxYear);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                taxes.add(mapResultSetToTax(rs));
            }
            return taxes;
        } catch (SQLException e) {
            throw new TaxCalculationException("Database error: " + e.getMessage());
        }
    }

    private Tax mapResultSetToTax(ResultSet rs) throws SQLException {
        Tax tax = new Tax();
        tax.setTaxID(rs.getInt("TaxID"));
        tax.setEmployeeID(rs.getInt("EmployeeID"));
        tax.setTaxYear(rs.getInt("TaxYear"));
        tax.setTaxableIncome(rs.getDouble("TaxableIncome"));
        tax.setTaxAmount(rs.getDouble("TaxAmount"));
        return tax;
    }
}