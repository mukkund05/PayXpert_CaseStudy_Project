package com.hexaware.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class DBPropertyUtil {
    public static String getConnectionString(String propertyFileName) {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(propertyFileName)) {
            props.load(fis);
            String url = props.getProperty("url");
            String username = props.getProperty("username");
            String password = props.getProperty("password");
            if (url == null || username == null || password == null) {
                throw new IOException("Missing required properties in " + propertyFileName);
            }
            return url + "?user=" + username + "&password=" + password;
        } catch (IOException e) {
            throw new RuntimeException("Failed to load database properties: " + e.getMessage());
        }
    }
}