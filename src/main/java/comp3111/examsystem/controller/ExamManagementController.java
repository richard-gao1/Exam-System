package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Controller class for the Exam Management view.
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
    @FXML private HBox tableBox;
    @FXML private VBox leftPane, rightPane;
    @FXML private BorderPane borderPane;

    private Teacher currentTeacher = (Teacher) SystemDatabase.currentUser;
    private ObservableList<Exam> examList = FXCollections.observableArrayList(currentTeacher.getExams());
    private ObservableList<Question> questionList = FXCollections.observableArrayList(currentTeacher.getQuestionBank());
    private ObservableList<Question> examQuestionList = FXCollections.observableArrayList();
    private ObservableList<Question> tempQuestionList = FXCollections.observableArrayList();
    private ObservableList<String> courseList =  FXCollections.observableArrayList("Course");

    /**
     * Initializes the UI components and sets up the data bindings.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTables();
        initializeChoiceBoxes();
        addListener();
        bindButtonStates();
    }

    /**
     * TODO: Now adding questions to existing exam would temporarily save it
     * You can view them again when selecting
     * But if you don't click "update" button it would not be written to the systemDatabase
     * which kind of makes some sense tho
     * if you press "refresh" before "update" the questions would be rolled back to before
     * I would say "This is not a bug, this is a feature", but I want to know what you think
     */

    /**
     * Initialize the exam table, question table, and examQuestion table with their respective properties and items.
     */
    private void initializeTables(){
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

        // Initialize the examQuestionTable
        examQuestionTable.setItems(examQuestionList);
        examQuestionColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());
        examTypeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTypeChoice() == 0 ? "Single" : "Multiple"));
        examScoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());
    }

    /**
     * Initializes the choice boxes used in the application.
     *
     * This method sets up the course ID, published status, and question type choice boxes
     * with appropriate values. It populates the course list from the current teacher's courses
     * and sets default values for each choice box.
     */
    private void initializeChoiceBoxes(){
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
    }

    /**
     * Adds listeners to the UI components for updating input fields and handling table width
     changes.
     */
    private void addListener(){
        // Add a listener to the examTable to update input fields
        examTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            boolean isExamSelected = (newSelection != null);
            if (isExamSelected) {
                examInput.setText(newSelection.getExamName());
                durationInput.setText(String.valueOf(newSelection.getDuration()));
                courseInput.setValue(newSelection.getCourse().getCourseID());
                publishInput.setValue(newSelection.getIsPublished() ? "Yes" : "No");
            } else {
                clearInputFields();
            }
            updateQuestionLists(newSelection);
            refreshQuestion();
        });

        borderPane.widthProperty().addListener((observable, oldWidth, newWidth) ->
                javafx.application.Platform.runLater(() -> initializeWidth(newWidth.doubleValue()))
        );

        examQuestionTable.widthProperty().addListener((observable, oldWidth, newWidth) ->
                javafx.application.Platform.runLater(() -> initializeColumnWidth(newWidth.doubleValue(), examTypeColumn, examScoreColumn, examQuestionColumn))
        );

        questionTable.widthProperty().addListener((observable, oldWidth, newWidth) ->
                javafx.application.Platform.runLater(() -> initializeColumnWidth(newWidth.doubleValue(), typeColumn, scoreColumn, questionColumn))
        );

    }

    /**
     * Initializes the width of various UI components based on the border pane's width.
     *
     * @param borderWidth the new width of the border pane
     */
    private void initializeWidth(double borderWidth) {
        double leftWidth = leftPane.getWidth(); // New width of the right pane
        double padding = borderPane.getInsets().getLeft() + borderPane.getInsets().getRight(); // Padding of the borderPane
        double dynamicWidth = borderWidth - leftWidth- padding; // Remaining space for questionColumn (with padding)
        // Set the width dynamically for the questionColumn
        if (dynamicWidth > 0) {
            rightPane.setPrefWidth(dynamicWidth);
            tableBox.setPrefWidth(dynamicWidth);
            examQuestionTable.setPrefWidth(dynamicWidth / 2);
            questionTable.setPrefWidth(dynamicWidth / 2);
        }
    }

    /**
     * Initializes the column widths of a table based on the total table width.
     *
     * @param tableWidth the total width of the table
     * @param col1 the first column with fixed width
     * @param col2 the second column with fixed width
     * @param targetCol the target column to dynamically adjust its width
     */
    private void initializeColumnWidth(double tableWidth, TableColumn col1, TableColumn col2, TableColumn targetCol){
        double fixedWidth = col1.getWidth()+col2.getWidth(); // New width of the right pane
        double dynamicWidth = tableWidth - fixedWidth-1; // Remaining space for questionColumn (with padding)
        // Set the width dynamically for the questionColumn
        if (dynamicWidth > 0) {
            targetCol.setPrefWidth(dynamicWidth);
        }
    }

    /**
     * Binds the enabled state of various buttons to the selection state of UI components.
     */
    private void bindButtonStates(){
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
        // Reset exam List
        examList.setAll(currentTeacher.getExams());
        refreshExam();
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
        // Reset question List
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        updateQuestionLists(selectedExam);
        refreshQuestion();
    }
    // TODO: After filtering, add question to an exam will cause the question disappear, but still can add the course.

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

        // Filter for questionTable (remaining questions not in the selected exam)
        tempQuestionList.setAll(questionList);
        questionList.setAll(tempQuestionList.filtered(question -> {
            // Exclude questions already in the selected exam
            boolean notInExam = (selectedExam == null)||(!selectedExam.getQuestions().contains(question));
            //showAlert(Alert.AlertType.INFORMATION,"Hi", String.valueOf(matchesQuestion).concat(String.valueOf(matchesType)).concat(String.valueOf(matchesScore)).concat(String.valueOf(notInExam)));
            return filterQuestion(question,questionText,type,scoreText) && notInExam;
        }));
        questionTable.setItems(questionList);

        // Filter for examQuestionTable
        if (selectedExam != null) {
            tempQuestionList.setAll(selectedExam.getQuestions());
            examQuestionList.setAll(tempQuestionList.filtered(
                            question -> filterQuestion(question,questionText,type,scoreText)
                    )
            );
            examQuestionTable.setItems(examQuestionList);
        }
    }

    /**
     * Filters a question based on the provided criteria.
     *
     * @param question the question to be filtered
     * @param questionText the text to search within the question content
     * @param type the type of the question (e.g., "Single", "Multiple")
     * @param scoreText the score to match against the question's score
     * @return true if the question matches all criteria, false otherwise
     */
    private boolean filterQuestion(Question question, String questionText, String type, String scoreText) {
        boolean matchesQuestion = questionText.isEmpty() || question.getContent().toLowerCase().contains(questionText);
        boolean matchesType = type == null ||type.equals("Type") || (type.equals("Single") && question.getTypeChoice() == 0) || (type.equals("Multiple") && question.getTypeChoice() == 1);
        boolean matchesScore = scoreText.isEmpty() || Integer.toString(question.getScore()).equals(scoreText);
        return matchesQuestion && matchesType && matchesScore;
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
            Course selectedCourse = selectedExam.getCourse();
            examList.remove(selectedExam);
            examTable.setItems(examList);
            currentTeacher.deleteExam(selectedExam,selectedCourse);
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
        if (!validateExamInputs()) {
            return; // Exit if validation fails
        }
        String examName = examInput.getText().trim();
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
            Exam newExam = new Exam(examName, courseID, isPublished, duration, new ArrayList<>(examQuestionList));

            // Update the table
            examList.add(newExam);
            // Clear input fields
            clearInputFields();
        } catch (IllegalArgumentException e){
            showAlert(Alert.AlertType.ERROR, "Invalid Exam", e.getMessage());
        }
        refreshQuestion();
        refreshExam();
    }

    /**
     * Updates an existing exam with the provided inputs.
     * This function checks for validation, duplication, and updates the selected exam in the examList and currentTeacher.
     */
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
            // Now update won't update the question according to the
            currentTeacher.updateExam(selectedExam.getExamName(),selectedExam.getCourse(), updatedExamName,updatedCourseID,updatedIsPublished,updatedDuration,new ArrayList<>(selectedExam.getQuestions()));
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

    /**
     * Removes a selected question from the examQuestionTable and adds it to the questionTable.
     */
    @FXML
    private void onUnselectQuestion() {
        // Remove question from left table
        // Really remove the question from exam, no need to wait for "update" is clicked
        Question selectedQuestion = examQuestionTable.getSelectionModel().getSelectedItem();
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            examQuestionList.remove(selectedQuestion);
            questionList.add(selectedQuestion);
            if (selectedExam != null){
                selectedExam.removeQuestion(selectedQuestion);
            }
            refreshQuestion();
        }
    }

    /**
     * Adds a selected question from the questionTable to the examQuestionTable.
     *
     * Select question logic
     * If no exam is selected, questionList should load the full list of the teacher's question bank.
     * If an exam is selected, examList will display the question it has
     * while the questionList will display the question not in the examList
     */
    @FXML
    private void onSelectQuestion() {
        // Add question to left table
        // Really add the question to exam, no need to wait for "update" is clicked
        Question selectedQuestion = questionTable.getSelectionModel().getSelectedItem();
        Exam selectedExam = examTable.getSelectionModel().getSelectedItem();
        if (selectedQuestion != null) {
            questionList.remove(selectedQuestion);
            examQuestionList.add(selectedQuestion);
            if (selectedExam != null){
                selectedExam.addQuestion(selectedQuestion);
            }
            refreshQuestion();
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
     * Updates the question lists based on the selected exam.
     *
     * @param selectedExam the selected exam to update questions for
     */
    private void updateQuestionLists(Exam selectedExam) {
        questionList.setAll(currentTeacher.getQuestionBank());
        if (selectedExam != null) {
            // Update examQuestionTable with questions in the selected exam
            examQuestionList.setAll(selectedExam.getQuestions());
            // Update questionTable to exclude questions already in the selected exam
            tempQuestionList.setAll(currentTeacher.getQuestionBank());
            questionList.setAll(tempQuestionList.filtered(question -> {
                for (Question q : examQuestionList) {
                    if (question.equals(q)) {
                        return false;
                    }
                }
                return true;
            }));
        } else {
            examQuestionList.clear();
        }
    }



    /**
     * Refreshes the UI components displaying exams.
     */
    private void refreshExam(){
        // Reload the examTable with the full list
        examTable.setItems(examList);
        examTable.refresh();
    }

    /**
     * Refreshes the UI components displaying questions.
     */
    private void refreshQuestion(){
        // Reload the examQuestionTable and questionTable
        examQuestionTable.setItems(examQuestionList);
        questionTable.setItems(questionList);
        examQuestionTable.refresh();
        questionTable.refresh();
    }

    /**
     * Refreshes all input fields, filters, and tables.
     * This function clears all input fields and filters, reloads the examTable and questionTable with full lists,
     * and resets the selection for all tables.
     */
    private void refresh() {
        // Reset selection
        examTable.getSelectionModel().clearSelection();
        questionTable.getSelectionModel().clearSelection();
        examQuestionTable.getSelectionModel().clearSelection();
        // Clear all input fields and filters
        clearInputFields();
        onExamReset();
        onQuestionReset();
    }



}

