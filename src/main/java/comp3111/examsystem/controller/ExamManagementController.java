package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
        publishFilter.setItems(FXCollections.observableArrayList("Yes","No"));
        publishFilter.setValue("Publish");
        publishInput.setItems(FXCollections.observableArrayList("Yes","No"));
        publishInput.setValue("Publish");

        // Initialize Type
        typeFilter.setItems(FXCollections.observableArrayList("Single","Multiple"));
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
                // Exam is unselected, clear the examQuestionList, reset the QuestionList
                clearInputFields();
                examQuestionList.clear();
                questionList.setAll(currentTeacher.getQuestionBank());
                questionTable.refresh();
                examQuestionTable.refresh();
            }
            // Apply back the filter, if any.
            // onQuestionFilter();
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

    @FXML
    private void onExamReset() {
        // Reset exam filter fields
        examFilter.clear();
        courseFilter.setValue("Course");
        publishFilter.setValue("Publish");
    }

    @FXML
    private void onExamFilter() {
        // Apply exam filters
        String examNameText = examFilter.getText().toLowerCase();
        String courseID = courseFilter.getValue();
        String published = publishFilter.getValue();

        examTable.setItems(examList.filtered
                (exam -> {
            boolean matchesExamName = examNameText.isEmpty() || exam.getExamName().toLowerCase().contains(examNameText);
            boolean matchesCourse = courseID == null|| courseID.equals("Course") || exam.getCourse().getCourseID().equals(courseID);
            boolean matchesPublished = published == null
                    || published.equals("Publish")
                    || (published.equals("Yes") && exam.getIsPublished())
                    || (published.equals("No") && !exam.getIsPublished());
            return matchesExamName && matchesCourse && matchesPublished;
                }
            )
        );
        // TODO: The problem is, when you are filtering and want to add question, the added question will disappear.
        // This is because when you add to left/right, you have not commit the change until you press update,
        // But the button already remove it from the current questionList/examQuestionList
        // The content of the question list is given by question bank - questions in the examlist
        // One solution might be, when pressing the button directly add the question to the exam.
        // But such an approach has another problem: how about when no exam is selected?
        // Maybe can check if selected exam is null
        // Also, amend the listener, such that, if the selected exam changes, completely refresh the examlist
        // if change from not selecting exam to selecting exam, keep the original examList as is, but add the existing exam qs.
        // maybe the best solution is to have a temp observable list to do the filtering stuff.
    }

    @FXML
    private void onQuestionReset() {
        // Reset question filter fields
        questionFilter.clear();
        typeFilter.setValue("Type");
        scoreFilter.clear();
    }
    // TODO: After filtering, add question to an exam will cause the question disappear, but still can add the course.

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
            boolean matchesType = type == null || type.equals("Type")|| (type.equals("Single") && question.getTypeChoice() == 0) || (type.equals("Multiple") && question.getTypeChoice() == 1);
            boolean matchesScore = scoreText.isEmpty() || Integer.toString(question.getScore()).equals(scoreText);

            // Exclude questions already in the selected exam
            boolean notInExam = (selectedExam == null)||(!selectedExam.getQuestions().contains(question));
            //showAlert(Alert.AlertType.INFORMATION,"Hi", String.valueOf(matchesQuestion).concat(String.valueOf(matchesType)).concat(String.valueOf(matchesScore)).concat(String.valueOf(notInExam)));
            return matchesQuestion && matchesType && matchesScore && notInExam;
        }));
    }

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

    @FXML
    private void onRefresh() {
        // Refresh the UI
        refresh();
    }

    @FXML
    private void onAdd() {
        if (!validateExamInputs()) {
            return; // Exit if validation fails
        }
        String examName = examInput.getText();
        String courseID = courseInput.getValue();
        int duration = Integer.parseInt(durationInput.getText());
        boolean isPublished = (publishInput.getValue().equals("Yes"));


        // Check for duplicate exam names
        for (Exam exam : examList) {
            if (exam.getExamName().equalsIgnoreCase(examName) && exam.getCourse().getCourseID().equals(courseID)) {
                showAlert(Alert.AlertType.ERROR,"Validation Error", "Exam with the same name already exists.");
                return;
            }
        }
        // Create and add new exam

        try {
            Exam newExam = new Exam(examName, courseID, isPublished, duration);
            newExam.getQuestions().addAll(examQuestionList);
            examList.add(newExam);
            // Update the table

            // Clear input fields
            clearInputFields();
        } catch (IllegalArgumentException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Exam", e.getMessage());
        }
        refresh();
    }

    @FXML
    private void onUpdate() {
        // Handle updating an existing exam
        try {
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
            currentTeacher.updateExam(selectedExam.getExamName(),selectedExam.getCourse(), updatedExamName,updatedCourseID,updatedIsPublished,updatedDuration,new ArrayList<>(examQuestionList));
            examList.setAll(currentTeacher.getExams());
            clearInputFields();
        } catch (IllegalArgumentException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Exam", e.getMessage());
        }
        refresh();
    }
    /* Select question logic
     * If no exam is selected, questionList should load the full list of the teacher's question bank.
     * If an exam is selected, examList will display the question it has
     * while the questionList will display the question not in the examList
     *
     *
     */


    @FXML
    private void onUnselectQuestion() {
        // Remove question from left table
        // Won't really remove the question from exam unless "update" is clicked
        Question selectedQuestion = examQuestionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            examQuestionList.remove(selectedQuestion);
            questionList.add(selectedQuestion);
            examQuestionTable.refresh();
            questionTable.refresh();
            //onQuestionFilter();       // no use, don't add
        }
    }

    @FXML
    private void onSelectQuestion() {
        // Add question to left table
        // Won't really add the question to exam unless "update" is clicked
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            questionList.remove(selectedQuestion);
            examQuestionList.add(selectedQuestion);
            questionTable.refresh();
            examQuestionTable.refresh();
            //onQuestionFilter();       // no use, don't add
        }
    }

    // Utility
    private void clearInputFields() {
        examInput.clear();
        durationInput.clear();
        courseInput.setValue("Course");
        publishInput.setValue("Publish");
    }

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


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void updateQuestionTables(Exam selectedExam) {
        // Update examQuestionTable with questions in the selected exam
        examQuestionList.setAll(selectedExam.getQuestions());
        // Update questionTable to exclude questions already in the selected exam
        questionList.setAll(currentTeacher.getQuestionBank());
        questionTable.setItems(questionList.filtered(question -> {
            for (Question q : examQuestionList) {
                if (question.equals(q)){
                    return false;
                }
            }
            return true;
        }));
        questionTable.refresh();
        examQuestionTable.refresh();
    }

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

