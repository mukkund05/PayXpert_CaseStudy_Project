package com.hexaware.dao;

import com.hexaware.entity.Payroll;
import com.hexaware.exception.PayrollGenerationException;

import java.util.List;

public interface IPayrollService {
    boolean generatePayroll(int employeeID, java.sql.Date payPeriodStartDate, java.sql.Date payPeriodEndDate) throws PayrollGenerationException;
    Payroll getPayrollById(int payrollID) throws PayrollGenerationException;
    List<Payroll> getPayrollsForEmployee(int employeeID) throws PayrollGenerationException;
    List<Payroll> getPayrollsForPeriod(java.sql.Date startDate, java.sql.Date endDate) throws PayrollGenerationException;
}