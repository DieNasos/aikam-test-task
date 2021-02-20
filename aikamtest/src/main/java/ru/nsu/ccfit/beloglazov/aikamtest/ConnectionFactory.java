package ru.nsu.ccfit.beloglazov.aikamtest;

import java.sql.*;
import org.postgresql.Driver;

public class ConnectionFactory {
    public static Connection getConnection(String url, String user, String pass) throws SQLException {
        DriverManager.registerDriver(new Driver());
        return DriverManager.getConnection(url, user, pass);
    }
}