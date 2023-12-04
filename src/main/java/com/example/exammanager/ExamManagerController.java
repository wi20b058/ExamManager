package com.example.exammanager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ExamManagerController {

    @FXML private TextField newQuestionTextField;
    @FXML private ChoiceBox<Integer> pointsChoiceBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField newCategoryTextField;
    @FXML private TableView<String> questionTable;
    @FXML private TableColumn<String, String> questionColumn;
    @FXML private ComboBox<String> examComboBox;
    @FXML private ComboBox<String> categoryComboBox2;
    @FXML private TextField newQuestionTextField1;
    @FXML private ChoiceBox<Integer> pointsChoiceBox1;
    private String selectedQuestion;
    private int selectedQuestionId;
    private final DatabaseConnection dbConnection = new DatabaseConnection();

    @FXML
    public void initialize() {
        System.out.println("Initializing...");
        dbConnection.connect();

        // Populate pointsChoiceBox with values 1 to 10
        pointsChoiceBox.getItems().addAll(IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList()));
        pointsChoiceBox1.getItems().addAll(IntStream.rangeClosed(1, 10).boxed().collect(Collectors.toList()));

        // Populate categoryComboBox with existing categories
        categoryComboBox.setItems(FXCollections.observableArrayList(dbConnection.getCategories()));
        categoryComboBox2.setItems(FXCollections.observableArrayList(dbConnection.getCategories()));

        // Initialize examComboBox with exams
        examComboBox.setItems(FXCollections.observableArrayList(dbConnection.getExams()));

        // Set up the question table column
        questionColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        updateQuestionTable(categoryComboBox2.getValue()); // Update table based on selected category

        // Set up a listener for question table selection
        questionTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedQuestion = newSelection; // newSelection is the question text
                selectedQuestionId = dbConnection.getQuestionIdByText(newSelection.split(" - Points: ")[0]);
                newQuestionTextField1.setText(selectedQuestion.split(" - Points: ")[0]);
                pointsChoiceBox1.setValue(Integer.parseInt(selectedQuestion.split(" - Points: ")[1]));
            }
        });
    }

    @FXML
    private void handleAddQuestionToCategory() {
        String questionText = newQuestionTextField.getText();
        if (questionText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "The question field can't be left empty.");
            return;
        }
        Integer points = pointsChoiceBox.getValue();
        String categoryName = categoryComboBox.getValue();
        String newCategoryName = newCategoryTextField.getText();

        if (!newCategoryName.isEmpty()) {
            dbConnection.addCategory(newCategoryName);
            categoryName = newCategoryName;
        }

        if (categoryName != null && points != null) {
            dbConnection.addQuestion(questionText, points, categoryName);
            newQuestionTextField.clear();
            pointsChoiceBox.setValue(null);
            categoryComboBox.setValue(null);
            newCategoryTextField.clear();
            showAlert(Alert.AlertType.INFORMATION, "Success", "New question successfully saved");
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Category or points not selected");
        }
    }

    @FXML
    protected void editSelectedQuestion() {
        if (selectedQuestionId <= 0) {
            showAlert(Alert.AlertType.ERROR, "No Question Selected", "Please select a question to edit.");
            return;
        }

        String editedQuestionText = newQuestionTextField1.getText();
        Integer editedPoints = pointsChoiceBox1.getValue();

        if (editedQuestionText.isEmpty() || editedPoints == null) {
            showAlert(Alert.AlertType.ERROR, "Incomplete Information", "Please provide both question text and points.");
            return;
        }

        dbConnection.updateQuestion(selectedQuestionId, editedQuestionText, editedPoints);
        updateQuestionTable(categoryComboBox2.getValue());
    }

    @FXML
    protected void deleteSelectedQuestion() {
        if (selectedQuestion == null) {
            showAlert(Alert.AlertType.ERROR, "No Question Selected", "Please select a question to delete.");
            return;
        }

        int questionId = dbConnection.getQuestionIdByText(selectedQuestion);
        dbConnection.deleteQuestion(questionId);
        updateQuestionTable(categoryComboBox2.getValue());
    }

    @FXML
    protected void addQuestionToExam() {
        if (selectedQuestion == null || examComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Required", "Please select a question and an exam.");
            return;
        }

        int questionId = dbConnection.getQuestionIdByText(selectedQuestion);
        int examId = dbConnection.getExamIdByName(examComboBox.getValue());

        dbConnection.addQuestionToExam(questionId, examId);
    }


    public void updateQuestionTable(String category) {
        List<String> questions;
        if (category == null || category.isEmpty()) {
            // Fetch all questions if no specific category is selected
            questions = dbConnection.getAllQuestionsWithPoints();
        } else {
            // Fetch questions by the selected category
            questions = dbConnection.getQuestionsByCategory(category);
        }

        ObservableList<String> formattedQuestions = FXCollections.observableArrayList();
        for (String question : questions) {
            int points = dbConnection.getQuestionPoints(question);
            formattedQuestions.add(question + " - Points: " + points);
        }
        questionTable.setItems(formattedQuestions);
    }

    @FXML
    public void handleCategorySelection(ActionEvent event) {
        String selectedCategory = categoryComboBox2.getValue();
        updateQuestionTable(selectedCategory);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
