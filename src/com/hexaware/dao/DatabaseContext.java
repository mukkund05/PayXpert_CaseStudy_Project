package com.hexaware.dao;

import com.hexaware.exception.DatabaseConnectionException;
import com.hexaware.util.DBConnUtil;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseContext {
    private Connection conn;

    public DatabaseContext() throws DatabaseConnectionException {
        try {
            this.conn = DBConnUtil.getConnection("E:\\CASE STUDY\\PayXpert\\PayXpert\\src\\com\\hexaware\\util\\db.properties");
        } catch (Exception e) {
            throw new DatabaseConnectionException("Failed to establish database connection: " + e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() throws DatabaseConnectionException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to close database connection: " + e.getMessage());
        }
    }
}