package com.hexaware.dao;

import com.hexaware.entity.FinancialRecord;
import com.hexaware.exception.DatabaseConnectionException;
import com.hexaware.exception.FinancialRecordException;
import com.hexaware.util.DBConnUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FinancialRecordService implements IFinancialRecordService {
    private Connection conn;

    public FinancialRecordService() throws DatabaseConnectionException {
        this.conn = DBConnUtil.getConnection("E:\\CASE STUDY\\PayXpert\\PayXpert\\src\\com\\hexaware\\util\\db.properties");
    }

    @Override
    public boolean addFinancialRecord(FinancialRecord record) throws FinancialRecordException {
        String sql = "INSERT INTO FinancialRecord (EmployeeID, RecordDate, Description, Amount, RecordType) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, record.getEmployeeID());
            pstmt.setDate(2, record.getRecordDate());
            pstmt.setString(3, record.getDescription());
            pstmt.setDouble(4, record.getAmount());
            pstmt.setString(5, record.getRecordType());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new FinancialRecordException("Failed to add financial record: " + e.getMessage());
        }
    }

    @Override
    public FinancialRecord getFinancialRecordById(int recordID) throws FinancialRecordException {
        String sql = "SELECT * FROM FinancialRecord WHERE RecordID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, recordID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToFinancialRecord(rs);
            } else {
                throw new FinancialRecordException("Financial record with ID " + recordID + " not found");
            }
        } catch (SQLException e) {
            throw new FinancialRecordException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialRecord> getFinancialRecordsForEmployee(int employeeID) throws FinancialRecordException {
        List<FinancialRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM FinancialRecord WHERE EmployeeID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, employeeID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToFinancialRecord(rs));
            }
            return records;
        } catch (SQLException e) {
            throw new FinancialRecordException("Database error: " + e.getMessage());
        }
    }

    @Override
    public List<FinancialRecord> getFinancialRecordsForDate(java.sql.Date recordDate) throws FinancialRecordException {
        List<FinancialRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM FinancialRecord WHERE RecordDate = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, recordDate);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                records.add(mapResultSetToFinancialRecord(rs));
            }
            return records;
        } catch (SQLException e) {
            throw new FinancialRecordException("Database error: " + e.getMessage());
        }
    }

    private FinancialRecord mapResultSetToFinancialRecord(ResultSet rs) throws SQLException {
        FinancialRecord record = new FinancialRecord();
        record.setRecordID(rs.getInt("RecordID"));
        record.setEmployeeID(rs.getInt("EmployeeID"));
        record.setRecordDate(rs.getDate("RecordDate"));
        record.setDescription(rs.getString("Description"));
        record.setAmount(rs.getDouble("Amount"));
        record.setRecordType(rs.getString("RecordType"));
        return record;
    }
}