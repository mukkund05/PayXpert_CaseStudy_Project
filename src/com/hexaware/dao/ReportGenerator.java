package com.hexaware.dao;

import com.hexaware.entity.Payroll;
import com.hexaware.entity.Tax;
import com.hexaware.entity.FinancialRecord;

import java.util.List;

public class ReportGenerator {
    public String generateIncomeStatement(List<Payroll> payrolls) {
        StringBuilder report = new StringBuilder("Income Statement\n");
        double totalIncome = 0.0;
        for (Payroll p : payrolls) {
            totalIncome += p.getNetSalary();
            report.append("Employee ID: ").append(p.getEmployeeID())
                  .append(", Net Salary: ").append(p.getNetSalary()).append("\n");
        }
        report.append("Total Income: ").append(totalIncome).append("\n");
        return report.toString();
    }

    public String generateTaxSummary(List<Tax> taxes) {
        StringBuilder report = new StringBuilder("Tax Summary\n");
        double totalTax = 0.0;
        for (Tax t : taxes) {
            totalTax += t.getTaxAmount();
            report.append("Employee ID: ").append(t.getEmployeeID())
                  .append(", Tax Year: ").append(t.getTaxYear())
                  .append(", Tax Amount: ").append(t.getTaxAmount()).append("\n");
        }
        report.append("Total Tax: ").append(totalTax).append("\n");
        return report.toString();
    }

    public String generateFinancialReport(List<FinancialRecord> records) {
        StringBuilder report = new StringBuilder("Financial Report\n");
        double totalAmount = 0.0;
        for (FinancialRecord r : records) {
            totalAmount += r.getAmount();
            report.append("Record ID: ").append(r.getRecordID())
                  .append(", Description: ").append(r.getDescription())
                  .append(", Amount: ").append(r.getAmount()).append("\n");
        }
        report.append("Total Amount: ").append(totalAmount).append("\n");
        return report.toString();
    }
}