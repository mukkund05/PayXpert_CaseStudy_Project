package com.hexaware.dao;

import com.hexaware.entity.FinancialRecord;
import com.hexaware.exception.FinancialRecordException;

import java.util.List;

public interface IFinancialRecordService {
    boolean addFinancialRecord(FinancialRecord record) throws FinancialRecordException;
    FinancialRecord getFinancialRecordById(int recordID) throws FinancialRecordException;
    List<FinancialRecord> getFinancialRecordsForEmployee(int employeeID) throws FinancialRecordException;
    List<FinancialRecord> getFinancialRecordsForDate(java.sql.Date recordDate) throws FinancialRecordException;
}