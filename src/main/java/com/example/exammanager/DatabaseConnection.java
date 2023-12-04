package com.example.exammanager;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class DatabaseConnection {
    private static final String db = "jdbc:sqlite:D:/ExamManager/src/main/java/DB/mydb.db";
    private Connection connection;

    public void connect() {
        try {
            if (connection == null || connection.isClosed()) {
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

    public void deleteQuestion(int questionId) {
        String sql = "DELETE FROM Questions WHERE question_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, questionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addQuestionToExam(int questionId, String examName) {
        String sql = "INSERT INTO Exam_Questions (exam_id, question_id) VALUES ((SELECT exam_id FROM Exams WHERE exam_name = ?), ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, examName);
            pstmt.setInt(2, questionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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
// Inside DatabaseConnection class

    public void updateQuestion(int questionId, String newQuestionText, int newPoints) {
        String sql = "UPDATE Questions SET question_text = ?, question_points = ? WHERE question_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newQuestionText);
            pstmt.setInt(2, newPoints);
            pstmt.setInt(3, questionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating question: " + e.getMessage());
        }
    }


    public void addQuestionToExam(int questionId, int examId) {
        String sql = "INSERT INTO Exam_Questions (exam_id, question_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, examId);
            pstmt.setInt(2, questionId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error adding question to exam: " + e.getMessage());
        }
    }

    public List<String> getExams() {
        List<String> exams = new ArrayList<>();
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT exam_name FROM Exams")) {
            while (rs.next()) {
                exams.add(rs.getString("exam_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exams;
    }

    public int getExamIdByName(String examName) {
        String sql = "SELECT exam_id FROM Exams WHERE exam_name = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, examName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("exam_id");
            }
        } catch (SQLException e) {
            System.out.println("Error finding exam by name: " + e.getMessage());
        }
        return -1;
    }
    public int getQuestionIdByText(String questionText) {
        String sql = "SELECT question_id FROM Questions WHERE question_text = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, questionText);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("question_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getQuestionPoints(String questionText) {
        String sql = "SELECT question_points FROM Questions WHERE question_text = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, questionText);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("question_points");
            }
        } catch (SQLException e) {
            System.out.println("Error finding points for question: " + e.getMessage());
        }
        return 0; // or a suitable default value or error indicator
    }

    public List<String> getAllQuestionsWithPoints() {
        List<String> questionsWithPoints = new ArrayList<>();
        String sql = "SELECT question_text, question_points FROM Questions";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String questionText = rs.getString("question_text");
                int points = rs.getInt("question_points");
                questionsWithPoints.add(questionText + " - Points: " + points);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching all questions: " + e.getMessage());
        }
        return questionsWithPoints;
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
