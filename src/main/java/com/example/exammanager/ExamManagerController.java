package com.example.exammanager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.util.Objects;

public class ExamManagerController {
    @FXML public BorderPane mainLayout;
    public ComboBox categoryComboBox2;

    @FXML
    private void openQuestionCreation() {
        loadPage("QuestionCreation.fxml");
    }

    @FXML
    private void openQuestionManagement() {
        loadPage("QuestionManagement.fxml");
    }

    @FXML
    private void openExamManagement() {
        loadPage("ExamManagement.fxml");
    }

    private void loadPage(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
            Node page = loader.load();
            mainLayout.setCenter(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public ComboBox<String> categoryComboBox;

    @FXML
    public TableView<String> questionTable;

    @FXML
    public TableColumn<String, String> questionColumn;

    public void initialize() {
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connect();


        // TableView
        questionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        ObservableList<String> questions = FXCollections.observableArrayList(dbConnection.getQuestions());
        questionTable.setItems(questions);

        // ComboBox
        ObservableList<String> categories = FXCollections.observableArrayList(dbConnection.getCategories());
        categoryComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                updateQuestionTable(newValue);
            }
        });
        categoryComboBox.setItems(categories);
    }

    private void updateQuestionTable(String category) {
        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connect();
        ObservableList<String> questions = FXCollections.observableArrayList(dbConnection.getQuestionsByCategory(category));
        questionTable.setItems(questions);
    }

    @FXML
    public Button addButton, editButton, deleteButton, createExamButton;


    @FXML
    protected void addNewQuestion() {}
/*
        String questionText = newQuestionTextField.getText();
        String points = pointsChoiceBox.getValue();
        String category = categoryComboBox.getValue();

        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connect();
        dbConnection.addQuestion(questionText, points, category);
        dbConnection.disconnect();


        updateQuestionTable(category);


    @FXML
    protected void editSelectedQuestion() {

        String selectedQuestion = ...;
        String newQuestionText = ...;

        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connect();
        dbConnection.updateQuestion(selectedQuestion, newQuestionText);
        dbConnection.disconnect();
    }

    @FXML
    protected void deleteSelectedQuestion() {
        String selectedQuestion = ...;

        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connect();
        dbConnection.deleteQuestion(selectedQuestion);
        dbConnection.disconnect();
    }

    @FXML
    protected void addQuestionToExam() {
        String selectedQuestion = ...;
        String selectedExam = ...;

        DatabaseConnection dbConnection = new DatabaseConnection();
        dbConnection.connect();
        dbConnection.addQuestionToExam(selectedQuestion, selectedExam);
        dbConnection.disconnect();
    }

*/

}