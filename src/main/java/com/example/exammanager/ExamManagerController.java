package com.example.exammanager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExamManagerController {

    @FXML private TextField newQuestionTextField;
    @FXML private ChoiceBox<Integer> pointsChoiceBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField newCategoryTextField;

    private final DatabaseConnection dbConnection = new DatabaseConnection();

    @FXML
    public void initialize() {
        dbConnection.connect();

        // Populate pointsChoiceBox with values 1 to 10
        pointsChoiceBox.getItems().addAll(IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList()));
        // Populate categoryComboBox with existing categories
        categoryComboBox.setItems(FXCollections.observableArrayList(dbConnection.getCategories()));

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

    @FXML
    private void handleAddQuestionToCategory() {
        String questionText = newQuestionTextField.getText();
        if (questionText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "The question field can't be left empty.");
            return; // Exit the method if the question text is empty
        }
        Integer points = pointsChoiceBox.getValue();
        String categoryName = categoryComboBox.getValue();
        String newCategoryName = newCategoryTextField.getText();

        if (!newCategoryName.isEmpty()) {
            // Create new category and use its name
            dbConnection.addCategory(newCategoryName);
            categoryName = newCategoryName;
        }

        if (categoryName != null && points != null) {
            dbConnection.addQuestion(questionText, points, categoryName);

            // Clear fields after adding the question
            newQuestionTextField.clear();
            pointsChoiceBox.setValue(null); // or set to a default value
            categoryComboBox.setValue(null); // or set to a default value
            newCategoryTextField.clear();

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Success", "New question successfully saved");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Category or points not selected");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }




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
    public TableView<String> questionTable;

    @FXML
    public TableColumn<String, String> questionColumn;



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