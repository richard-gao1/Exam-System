package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Exam Management view.
 *
 * This class is responsible for handling user interactions and managing the state of the Exam Management view.
 * It initializes the UI components, sets up the data bindings, and handles events such as adding, updating, and deleting questions.
 */
public class ExamManagementController implements Initializable {
    @FXML private TextField examFilter, questionFilter, scoreFilter, examInput, durationInput;
    @FXML private ChoiceBox<String> courseFilter, publishFilter, typeFilter, courseInput, publishInput;
    @FXML private TableView<Exam> examTable;
    @FXML private TableView<Question> examQuestionTable, questionTable;
    @FXML private TableColumn<Exam, String> examColumn, courseColumn, publishColumn;
    @FXML private TableColumn<Exam, Integer> durationColumn; // Example types
    @FXML private TableColumn<Question, String> examQuestionColumn, examTypeColumn, questionColumn, typeColumn;
    @FXML private TableColumn<Question, Integer> examScoreColumn, scoreColumn;
    @FXML private Button deleteBtn, refreshBtn, addBtn, updateBtn;
    @FXML private Button unselectQuestionBtn, selectQuestionBtn;

    private Teacher currentTeacher = (Teacher) SystemDatabase.currentUser;
    private ObservableList<Exam> examList = FXCollections.observableArrayList(currentTeacher.getExams());
    private ObservableList<Question> questionList = FXCollections.observableArrayList(currentTeacher.getQuestionBank());
    private ObservableList<Question> examQuestionList = FXCollections.observableArrayList();
    private ObservableList<String> courseList =  FXCollections.observableArrayList("Course");

    /**
     * Initializes the UI components and sets up the data bindings.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the examTable
        examTable.setItems(examList);
        examColumn.setCellValueFactory(cellData -> cellData.getValue().examNameProperty());
        courseColumn.setCellValueFactory(cellData -> cellData.getValue().courseIDProperty());
        durationColumn.setCellValueFactory(cellData -> cellData.getValue().durationProperty().asObject());
        publishColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getIsPublished() ? "Yes" : "No"));

        // Initialize the questionTable
        questionTable.setItems(questionList);
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());
        typeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTypeChoice() == 0 ? "Single" : "Multiple"));
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // Initialize the courseList for CourseID choice boxes
        courseList.addAll(currentTeacher.getCourseID());
        courseInput.setItems(courseList);
        courseInput.setValue("Course");
        courseFilter.setItems(courseList);
        courseFilter.setValue("Course");

        // Initialize Published choice boxes
        publishFilter.setItems(FXCollections.observableArrayList("Publish","Yes","No"));
        publishFilter.setValue("Publish");
        publishInput.setItems(FXCollections.observableArrayList("Publish","Yes","No"));
        publishInput.setValue("Publish");

        // Initialize Type
        typeFilter.setItems(FXCollections.observableArrayList("Type","Single","Multiple"));
        typeFilter.setValue("Type");

        // Initialize the examQuestionTable
        examQuestionTable.setItems(examQuestionList);
        examQuestionColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());
        examTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTypeChoice() == 0 ? "Single" : "Multiple"));
        examScoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());

        // Add a listener to the examTable to update input fields
        examTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isExamSelected = (newSelection != null);

            if (isExamSelected) {
                examInput.setText(newSelection.getExamName());
                durationInput.setText(String.valueOf(newSelection.getDuration()));
                courseInput.setValue(newSelection.getCourse().getCourseID());
                publishInput.setValue(newSelection.getIsPublished() ? "Yes" : "No");

                // Update examQuestionTable and questionTable based on selected exam
                updateQuestionTables(newSelection);
            } else {
                clearInputFields();
            }
        });
        // Enable/disable buttons based on selection
        addBtn.disableProperty().bind(examTable.getSelectionModel().selectedItemProperty().isNotNull());
        deleteBtn.disableProperty().bind(examTable.getSelectionModel().selectedItemProperty().isNull());
        updateBtn.disableProperty().bind(examTable.getSelectionModel().selectedItemProperty().isNull());
        unselectQuestionBtn.disableProperty().bind(
                examQuestionTable.getSelectionModel().selectedItemProperty().isNull()
        );
        selectQuestionBtn.disableProperty().bind(
                questionTable.getSelectionModel().selectedItemProperty().isNull()
        );

    }

    /**
     * Resets the exam filter fields to their default values.
     */
    @FXML
    private void onExamReset() {
        // Reset exam filter fields
        examFilter.clear();
        courseFilter.setValue("Course");
        publishFilter.setValue("Publish");
    }

    /**
     * Applies the exam filters to the exam table.
     */
    @FXML
    private void onExamFilter() {
        // Apply exam filters
        String examNameText = examFilter.getText().toLowerCase();
        String courseID = courseFilter.getValue();
        String published = publishFilter.getValue();

        examTable.setItems(examList.filtered(exam -> {
            boolean matchesExamName = examNameText.isEmpty() || exam.getExamName().toLowerCase().contains(examNameText);
            boolean matchesCourse = courseID == null||courseID.equals("Course") || exam.getCourse().getCourseID().equals(courseID);
            boolean matchesPublished = published == null || published.equals("Publish")|| (published.equals("Yes") && exam.getIsPublished()) || (published.equals("No") && !exam.getIsPublished());
            return matchesExamName && matchesCourse && matchesPublished;
        }));
    }

    /**
     * Resets the question filter fields.
     */
    @FXML
    private void onQuestionReset() {
        // Reset question filter fields
        questionFilter.clear();
        typeFilter.setValue("Type");
        scoreFilter.clear();
    }

    /**
     * Applies question filters and updates the question tables accordingly.
     */
    @FXML
    private void onQuestionFilter() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        // Apply question filters
        String questionText = questionFilter.getText().toLowerCase();
        String type = typeFilter.getValue();
        String scoreText = scoreFilter.getText();

        // Filter for examQuestionTable
        if (selectedExam != null) {
            examQuestionTable.setItems(FXCollections.observableArrayList(selectedExam.getQuestions()).filtered(question -> {
                boolean matchesQuestion = questionText.isEmpty() || question.getContent().toLowerCase().contains(questionText);
                boolean matchesType = type == null || type.equals("Type") || (type.equals("Single") && question.getTypeChoice() == 0) || (type.equals("Multiple") && question.getTypeChoice() == 1);
                boolean matchesScore = scoreText.isEmpty() || Integer.toString(question.getScore()).equals(scoreText);
                return matchesQuestion && matchesType && matchesScore;
            }));
        }

        // Filter for questionTable (remaining questions not in the selected exam)
        questionTable.setItems(questionList.filtered(question -> {
            boolean matchesQuestion = questionText.isEmpty() || question.getContent().toLowerCase().contains(questionText);
            boolean matchesType = type == null || (type.equals("Single") && question.getTypeChoice() == 0) || (type.equals("Multiple") && question.getTypeChoice() == 1);
            boolean matchesScore = scoreText.isEmpty() || Integer.toString(question.getScore()).equals(scoreText);

            // Exclude questions already in the selected exam
            boolean notInExam = (selectedExam==null)||(!selectedExam.getQuestions().contains(question));
            return matchesQuestion && matchesType && matchesScore && notInExam;
        }));
    }

    /**
     * Deletes the selected exam from the examList and updates the UI accordingly.
     * This function checks if an exam is selected, removes it from the examList, and updates the examTable.
     * It also clears the input fields and refreshes the UI.
     */
    @FXML
    private void onDelete() {
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam != null) {
            examList.remove(selectedExam);
            examTable.setItems(examList);

            // Clear input fields
            clearInputFields();
            refresh();
        }
    }

    /**
     * Refreshes the UI.
     */
    @FXML
    private void onRefresh() {
        // Refresh the UI
        refresh();
    }

    /**
     * Adds a new exam with the provided inputs.
     */
    @FXML
    private void onAdd() {
        String examName = examInput.getText();
        String courseID = courseInput.getValue();
        int duration = Integer.parseInt(durationInput.getText());
        boolean isPublished = (publishInput.getValue().equals("Yes"));

        if (!validateExamInputs()) {
            return; // Exit if validation fails
        }
        // Check for duplicate exam names
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(examName)) {
                showAlert(Alert.AlertType.ERROR,"Validation Error", "Exam with the same name already exists.");
                return;
            }
        }
        // Create and add new exam
        Exam newExam = new Exam(examName, courseID, isPublished, duration);
        newExam.getQuestions().addAll(examQuestionList);
        try {
            currentTeacher.addExam(newExam, courseID);
            examList.add(newExam);
            // Update the table

            // Clear input fields
            clearInputFields();
        } catch (IllegalArgumentException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Exam", e.getMessage());
        }
        refresh();
    }

    /**
     * Updates an existing exam with the provided inputs.
     * This function checks for validation, duplication, and updates the selected exam in the examList and currentTeacher.
     */
    @FXML
    private void onUpdate() {
        // Handle updating an existing exam
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedExam == null) {
            return;
        }

        // Validate the inputs
        if (!validateExamInputs()) {
            return; // Exit if validation fails
        }
        String updatedExamName = examInput.getText();
        String updatedCourseID = courseInput.getValue();
        boolean updatedIsPublished = (publishInput.getValue().equals("Yes"));
        int updatedDuration = Integer.parseInt(durationInput.getText());


        for (Exam exam : examList) {
            if (exam != selectedExam &&
                    exam.getExamName().equalsIgnoreCase(updatedExamName) &&
                    exam.getCourse().getCourseID().equals(updatedCourseID)) {
                showAlert(Alert.AlertType.WARNING,"Duplicate Exam", "An exam with the same name and course already exists.");
                return;
            }
        }
        Exam newExam = new Exam(updatedExamName,updatedCourseID,updatedIsPublished,updatedDuration);
        newExam.getQuestions().addAll(examQuestionList);
        try {
            currentTeacher.updateExam(selectedExam.getExamName(), newExam, selectedExam.getCourse());
            int index = examList.indexOf(selectedExam);
            examList.set(index, newExam);
            examTable.setItems(examList);
            clearInputFields();
        } catch (IllegalArgumentException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Exam", e.getMessage());
        }
        refresh();
    }

    /**
     * Removes a selected question from the examQuestionTable and adds it to the questionTable.
     */
    @FXML
    private void onUnselectQuestion() {
        // Remove question from left table
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        Question selectedQuestion = examQuestionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            examQuestionList.remove(selectedQuestion);
            selectedExam.removeQuestion(selectedQuestion);
            questionList.add(selectedQuestion);
            questionTable.refresh();
            examQuestionTable.refresh();
        }
    }

    /**
     * Adds a selected question from the questionTable to the examQuestionTable.
     */
    @FXML
    private void onSelectQuestion() {
        // Add question to left table
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            questionList.remove(selectedQuestion);
            examQuestionList.add(selectedQuestion);
            selectedExam.addQuestion(selectedQuestion);
            questionTable.refresh();
            examQuestionTable.refresh();
        }
    }

    /**
     * Clears all input fields related to exam creation.
     */
    private void clearInputFields() {
        examInput.clear();
        durationInput.clear();
        courseInput.setValue("Course");
        publishInput.setValue("Publish");
    }

    /**
     * Validates the input fields for exam creation.
     * This function checks if all required fields are filled and validates the exam name, course ID, and duration.
     *
     * @return True if all inputs are valid, false otherwise.
     */
    private boolean validateExamInputs() {
        String examName = examInput.getText();
        String courseID = courseInput.getValue();
        String durationText = durationInput.getText();
        boolean isPublished = (publishInput.getValue() != null)&&(!publishInput.getValue().equals("Publish"));

        // Check required fields
        if (examName.isEmpty() || courseID == null || durationText.isEmpty() || !isPublished) {
            showAlert(Alert.AlertType.ERROR,"Validation Error", "All fields must be filled.");
            return false;
        }
        // Check Valid exam name
        if (examName.trim().isEmpty() || examName.length() > 100) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Exam name must be non-empty and less than 100 characters.");
            return false;
        }
        // Check if duration is a valid number
        try {
            int duration = Integer.parseInt(durationText);
            if (duration <= 0) {
                showAlert(Alert.AlertType.ERROR,"Validation Error", "Duration must be a positive number.");
                return false;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR,"Validation Error", "Duration must be a valid number.");
            return false;
        }

        // If all validations pass
        return true;
    }

    /**
     * Displays an alert dialog with the given type, title, and message.
     *
     * @param type   The type of the alert dialog.
     * @param title  The title of the alert dialog.
     * @param message The message to be displayed in the alert dialog.
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Updates the question tables based on the selected exam.
     * This function updates the examQuestionTable with questions in the selected exam and questionTable to exclude questions already in the selected exam.
     *
     * @param selectedExam The selected exam for which the question tables will be updated.
     */
    private void updateQuestionTables(Exam selectedExam) {
        // Update examQuestionTable with questions in the selected exam
        examQuestionList.setAll(selectedExam.getQuestions());

        // Update questionTable to exclude questions already in the selected exam
        questionTable.setItems(questionList.filtered(question -> !examQuestionList.contains(question)));
    }

    /**
     * Refreshes all input fields, filters, and tables.
     * This function clears all input fields and filters, reloads the examTable and questionTable with full lists,
     * and resets the selection for all tables.
     */
    private void refresh() {
        // Clear all input fields and filters
        clearInputFields();
        examFilter.clear();
        courseFilter.setValue("Course");
        publishFilter.setValue("Publish");
        questionFilter.clear();
        typeFilter.setValue("Type");
        scoreFilter.clear();

        // Reload the examTable with the full list
        examTable.setItems(examList);

        // Reload the questionTable with the full list
        questionTable.setItems(questionList);

        // Clear the examQuestionTable
        examQuestionList.clear();
        examQuestionTable.setItems(examQuestionList);

        // Refresh the tables to ensure UI updates
        examTable.refresh();
        questionTable.refresh();
        examQuestionTable.refresh();

        // Reset selection
        examTable.getSelectionModel().clearSelection();
        questionTable.getSelectionModel().clearSelection();
        examQuestionTable.getSelectionModel().clearSelection();
    }



}

