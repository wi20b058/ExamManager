package com.example.exammanager;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;


public class ExamManagerController {

    @FXML
    private ComboBox<String> categoryComboBox;

    @FXML
    private TableView<String> questionTable;

    public void initialize() {
        DatabaseConnection dbHandler = new DatabaseConnection();
        dbHandler.connect();

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