package com.example.exammanager;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String db = "jdbc:sqlite:D:/ExamManager/src/main/java/DB/mydb.db";
    private Connection connection;

    public void connect() {
        try {
            connection = DriverManager.getConnection(db);
            System.out.println("Connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
