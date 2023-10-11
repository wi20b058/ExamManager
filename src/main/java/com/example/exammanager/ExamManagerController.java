package com.example.exammanager;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;

public class ExamManagerController {

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TableView<String> questionTable;

    @FXML
    private TableColumn<String, String> questionColumn;

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
    private Button addButton, editButton, deleteButton, createExamButton;

    /*


    private void addQuestion() {

    }

    private void editSelectedQuestion() {

    }

    private void deleteSelectedQuestion() {

    }

    private void createExam() {

    }

     */

}