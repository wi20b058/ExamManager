package com.example.exammanager;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String db = "jdbc:sqlite:D:/ExamManager/src/main/java/DB/mydb.db";
    private Connection connection;

    public void connect() {
        try {
            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(db);
                System.out.println("Connected successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to connect to the database.");
        }
    }


    public void addCategory(String categoryName) {
        String sql = "INSERT INTO Categories (category_name) SELECT ? WHERE NOT EXISTS (SELECT 1 FROM Categories WHERE category_name = ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, categoryName);
            pstmt.setString(2, categoryName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding category: " + e.getMessage());
        }
    }

    // Method to add a new question to the database
    public void addQuestion(String questionText, int points, String categoryName) {
        String sql = "INSERT INTO Questions (question_text, question_points, category_id) VALUES (?, ?, (SELECT category_id FROM Categories WHERE category_name = ?))";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, questionText);
            pstmt.setInt(2, points);
            pstmt.setString(3, categoryName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding question: " + e.getMessage());
        }
    }
/*
    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

*/


        public List<String> getQuestions() {
            List<String> questions = new ArrayList<>();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT question_text FROM Questions")) {
                while (rs.next()) {
                    questions.add(rs.getString("question_text"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return questions;
        }

        public List<String> getCategories() {
            List<String> categories = new ArrayList<>();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT category_name FROM Categories")) {
                while (rs.next()) {
                    categories.add(rs.getString("category_name"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return categories;
        }

    public List<String> getQuestionsByCategory(String category) {
        List<String> questions = new ArrayList<>();
        String query = "SELECT question_text FROM Questions WHERE category_id = (SELECT category_id FROM Categories WHERE category_name = ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                questions.add(rs.getString("question_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }



    // Close the database connection
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

