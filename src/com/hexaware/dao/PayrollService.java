package com.hexaware.dao;

import com.hexaware.entity.Payroll;
import com.hexaware.exception.DatabaseConnectionException;
import com.hexaware.exception.PayrollGenerationException;
import com.hexaware.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PayrollService implements IPayrollService {
    private Connection conn;

    public PayrollService() throws DatabaseConnectionException {
        this.conn = DBConnUtil.getConnection("E:\\CASE STUDY\\PayXpert\\PayXpert\\src\\com\\hexaware\\util\\db.properties");
    }

    @Override
    public boolean generatePayroll(int employeeID, java.sql.Date payPeriodStartDate, java.sql.Date payPeriodEndDate) throws PayrollGenerationException {
    	if (payPeriodStartDate == null || payPeriodEndDate == null || payPeriodStartDate.after(payPeriodEndDate)) {
            throw new PayrollGenerationException("Invalid pay period dates provided.");
        }
    		
    		double DAILY_PAY_RATE = 1000.00;
    	
    	
            // Convert java.sql.Date to java.time.LocalDate for easier date manipulation
            LocalDate startLocalDate = payPeriodStartDate.toLocalDate();
            LocalDate endLocalDate = payPeriodEndDate.toLocalDate();

            // Calculate the number of days in the pay period
            long numberOfDays = ChronoUnit.DAYS.between(startLocalDate, endLocalDate) + 1; // +1 to include both start and end dates

            // Calculate the total payroll
            double netSalary = numberOfDays * DAILY_PAY_RATE;

            // In this simplified scenario, we'll just print the result.
            // In a real application, you would likely store this payroll information.
            System.out.println("Payroll generated for Employee ID: " + employeeID);
            System.out.println("Pay Period: " + payPeriodStartDate + " to " + payPeriodEndDate);
            System.out.println("Number of Days: " + numberOfDays);
            System.out.println("Total Payroll: ₹" + netSalary);

         
    	
    	
    	double basicSalary = 400000.0; // Placeholder for actual salary logic
        double overtimePay = 500.0;    // Placeholder
        double deductions = 0.0;   // Placeholder
//        double netSalary = basicSalary + overtimePay - deductions;

        String sql = "INSERT INTO Payroll (EmployeeID, PayPeriodStartDate, PayPeriodEndDate, BasicSalary, OvertimePay, Deductions, NetSalary) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            pstmt.setDate(2, payPeriodStartDate);
            pstmt.setDate(3, payPeriodEndDate);
            pstmt.setDouble(4, basicSalary);
            pstmt.setDouble(5, overtimePay);
            pstmt.setDouble(6, deductions);
            pstmt.setDouble(7, netSalary);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new PayrollGenerationException("Failed to generate payroll: " + e.getMessage());
        }
    }

    @Override
    public Payroll getPayrollById(int payrollID) throws PayrollGenerationException {
        String sql = "SELECT * FROM Payroll WHERE PayrollID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, payrollID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToPayroll(rs);
            } else {
                throw new PayrollGenerationException("Payroll with ID " + payrollID + " not found");
            }
        } catch (SQLException e) {
            throw new PayrollGenerationException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Payroll> getPayrollsForEmployee(int employeeID) throws PayrollGenerationException {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM Payroll WHERE EmployeeID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                payrolls.add(mapResultSetToPayroll(rs));
            }
            return payrolls;
        } catch (SQLException e) {
            throw new PayrollGenerationException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<Payroll> getPayrollsForPeriod(java.sql.Date startDate, java.sql.Date endDate) throws PayrollGenerationException {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM Payroll WHERE PayPeriodStartDate >= ? AND PayPeriodEndDate <= ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, startDate);
            pstmt.setDate(2, endDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                payrolls.add(mapResultSetToPayroll(rs));
            }
            return payrolls;
        } catch (SQLException e) {
            throw new PayrollGenerationException("Database error: " + e.getMessage());
        }
    }

    private Payroll mapResultSetToPayroll(ResultSet rs) throws SQLException {
        Payroll payroll = new Payroll();
        payroll.setPayrollID(rs.getInt("PayrollID"));
        payroll.setEmployeeID(rs.getInt("EmployeeID"));
        payroll.setPayPeriodStartDate(rs.getDate("PayPeriodStartDate"));
        payroll.setPayPeriodEndDate(rs.getDate("PayPeriodEndDate"));
        payroll.setBasicSalary(rs.getDouble("BasicSalary"));
        payroll.setOvertimePay(rs.getDouble("OvertimePay"));
        payroll.setDeductions(rs.getDouble("Deductions"));
        payroll.setNetSalary(rs.getDouble("NetSalary"));
        return payroll;
    }
}