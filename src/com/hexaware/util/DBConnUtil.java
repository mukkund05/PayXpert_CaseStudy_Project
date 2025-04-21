package com.hexaware.util;

import com.hexaware.exception.DatabaseConnectionException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnUtil {
    public static Connection getConnection(String propertyFileName) throws DatabaseConnectionException {
        try {
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            String connectionString = DBPropertyUtil.getConnectionString(propertyFileName);
            return DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("MySQL JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Failed to connect to database: " + e.getMessage());
        }
    }
}