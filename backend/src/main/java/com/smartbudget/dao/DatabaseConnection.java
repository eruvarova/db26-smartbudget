package com.smartbudget.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL      = "jdbc:postgresql://localhost:5432/smartbudget";
    private static final String USERNAME = "sb_user";
    private static final String PASSWORD = "sb_pass";

    private DatabaseConnection() { }   // prevent instantiation

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}