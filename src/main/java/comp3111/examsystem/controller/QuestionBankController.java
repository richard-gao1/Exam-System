package comp3111.examsystem.controller;


import comp3111.examsystem.Question;
import comp3111.examsystem.SystemDatabase;
import comp3111.examsystem.Teacher;
import javafx.animation.PauseTransition;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class for the Question Bank view.
 *
 * This class is responsible for handling user interactions and managing the state of the Question Bank view.
 * It initializes the UI components, sets up the data bindings, and handles events such as adding, updating, and deleting questions.
 */

public class QuestionBankController implements Initializable {

    @FXML private TextField questionFilter, scoreFilter;
    @FXML private ChoiceBox<String> typeFilter;
    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, String> questionColumn, optionAColumn, optionBColumn, optionCColumn, optionDColumn, answerColumn, typeColumn;
    @FXML private TableColumn<Question, Integer> scoreColumn;

    @FXML private TextField  questionInput, aInput, bInput, cInput, dInput, answerInput, scoreInput;
    @FXML private ChoiceBox<String> typeInput;

    @FXML private Button addBtn, updateBtn, deleteBtn, refreshBtn, resetBtn, filterBtn;

    @FXML private Label answerHint, scoreHint, scoreFilterHint;

    private Teacher currentTeacher = (Teacher) SystemDatabase.currentUser;

    private ObservableList<Question> questionList = FXCollections.observableArrayList(currentTeacher.getQuestionBank());

    /**
     * Initializes the controller and sets up the UI components.
     *
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setHint();
        setBtn();
        setChoiceBox();
        initializeTable();
        setListener();

        // Load initial data into the table
        questionTable.setItems(questionList);

        // Placeholder
        questionFilter.setPromptText("Filter questions...");
        scoreFilter.setPromptText("Enter score...");
    }


    /**
     * Adds a new question to the question bank.
     */
    @FXML
    private void onAdd() {
        try {
            if (!validateInputs()) {
                return; // Stop if validation fails
            }
            // Collect data from input fields
            String[] option = {aInput.getText().trim(), bInput.getText().trim(), cInput.getText().trim(), dInput.getText().trim()};
            int typeChoice = typeInput.getValue().equals("Single") ? 0 : 1;

            Question question = new Question(
                    questionInput.getText().trim(),
                    option,
                    answerInput.getText(),
                    Integer.parseInt(scoreInput.getText()),
                    typeChoice
            );
            currentTeacher.createQuestion(question);
            questionList.add(question);
            clearInputFields();
        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Question", e.getMessage());
        }

        currentTeacher.viewQuestionBank();
    }

    /**
     * Updates an existing question in the question bank.
     */
    @FXML
    private void onUpdate() {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        // Validate Single Choice Answer
        try {
            if (!validateInputs()) {
                return; // Stop if validation fails
            }

            // Update Question Properties
            currentTeacher.updateQuestion(selected,
                    questionInput.getText(),
                    new String[]{
                            aInput.getText(),
                            bInput.getText(),
                            cInput.getText(),
                            dInput.getText()
                    },
                    answerInput.getText(),
                    Integer.parseInt(scoreInput.getText()),
                    typeInput.getValue()
                    );
            // Clear Input Fields and Refresh Table
            clearInputFields();
            questionTable.refresh();
            questionTable.getSelectionModel().clearSelection();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Question", e.getMessage());
        }
    }

    /**
     * Deletes the selected question from the question bank and the list.
     */
    @FXML
    private void onDelete() {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Remove from both the list and the question bank
            questionList.remove(selected);
            currentTeacher.deleteQuestion(selected);
        }
    }

    /**
     * Refreshes the question bank and the table view.
     */
    @FXML
    private void onRefresh() {
        questionList.setAll(currentTeacher.getQuestionBank());
        questionTable.refresh();
        questionTable.getSelectionModel().clearSelection();
    }

    /**
     * Filters the questions based on the filter fields.
     */
    @FXML
    private void onFilter() {
        String questionText = questionFilter.getText().toLowerCase();
        String type = typeFilter.getValue();
        String scoreText = scoreFilter.getText();

        questionTable.setItems(questionList.filtered(question -> {
            boolean matchesQuestion = questionText.isEmpty() || question.getContent().toLowerCase().contains(questionText);
            boolean matchesType = type == null ||type.equals("Type") || (type.equals("Single") && question.getTypeChoice() == 0) || (type.equals("Multiple") && question.getTypeChoice() == 1);
            boolean matchesScore = scoreText.isEmpty() || Integer.toString(question.getScore()).equals(scoreText);
            return matchesQuestion && matchesType && matchesScore;
        }));
    }

    /**
     * Resets the filter fields and the table view.
     */
    @FXML
    private void onReset() {
        // Clear filter fields
        questionFilter.clear();
        scoreFilter.clear();
        typeFilter.setValue("Type");
        questionTable.setItems(questionList); // Reset the table view to original data
    }

    // Utility Functions

    private void setHint(){
        // Hints are hidden
        answerHint.setVisible(false);
        answerHint.setPrefHeight(0);
        scoreHint.setVisible(false);
        scoreHint.setPrefHeight(0);
        scoreFilterHint.setVisible(false);
        scoreFilterHint.setPrefHeight(0);
        // Add TextFormatter
        setTextFormatter(scoreInput,scoreHint,"[0-9]*","Score must be a number",false);
        setTextFormatter(scoreFilter,scoreFilterHint,"[0-9]*","Score must be a number",false);
        setTextFormatter(answerInput,answerHint,"[a-dA-D]*","Answer must be in &quot;ABCD&quot;",true);
    }

    private void setBtn(){
        // Initialize button states
        updateBtn.setDisable(true); // Disabled by default
        deleteBtn.setDisable(true); // Disabled by default

        // Bind addBtn to disable when table selection is not empty
        BooleanBinding isTableEmpty = questionTable.getSelectionModel().selectedItemProperty().isNull();
        addBtn.disableProperty().bind(isTableEmpty.not()); // Disable when something is selected

        // Bind the disable property of deleteBtn to the selection model
        deleteBtn.disableProperty().bind(isTableEmpty);

        // Bind the disable property of updateBtn to the selection model
        updateBtn.disableProperty().bind(isTableEmpty);
    }

    private void setChoiceBox(){
        // Bind ChoiceBox with options
        typeInput.setItems(FXCollections.observableArrayList("Single", "Multiple"));
        typeInput.setValue("Type"); // Default selection
        typeFilter.setItems(FXCollections.observableArrayList("Single", "Multiple"));
        typeFilter.setValue("Type"); // Default selection
    }

    private void initializeTable(){
        // Set up table columns
        questionColumn.setCellValueFactory(cellData -> cellData.getValue().contentProperty());
        optionAColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOptions().size() > 0 ? cellData.getValue().getOptions().get(0) : "")
        );
        optionBColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOptions().size() > 1 ? cellData.getValue().getOptions().get(1) : "")
        );
        optionCColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOptions().size() > 2 ? cellData.getValue().getOptions().get(2) : "")
        );
        optionDColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getOptions().size() > 3 ? cellData.getValue().getOptions().get(3) : "")
        );
        answerColumn.setCellValueFactory(cellData -> cellData.getValue().answerProperty());
        typeColumn.setCellValueFactory(cellData -> {
            int typeChoice = cellData.getValue().getTypeChoice();
            String typeString = (typeChoice == 0) ? "Single" : "Multiple";
            return new SimpleStringProperty(typeString);
        });
        scoreColumn.setCellValueFactory(cellData -> cellData.getValue().scoreProperty().asObject());


    }

    private void setTextFormatter(TextField textField, Label hintLabel, String regex, String hintMessage, boolean toUpperCase){
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getText();
            if (!newText.matches(regex)) {
                hintLabel.setText(hintMessage); // Set hint message dynamically
                hintLabel.setVisible(true); // Show hint
                hintLabel.setPrefHeight(18);
                startHintHideTimer(hintLabel); // Schedule to hide the hint
                return null; // Reject invalid input
            }
            hintLabel.setVisible(false); // Hide hint for valid input
            if (toUpperCase) {
                change.setText(newText.toUpperCase()); // Convert to uppercase if specified
            }
            return change; // Accept valid input
        }));
    }

    private void setListener(){
        // Add a listener to the selection model
        questionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue); // Populate input fields with selected question
            } else {
                clearInputFields(); // Clear input fields
            }
        });
        // Listener to unselect row
        // Add a listener to execute logic after the TableView is added to the scene
        questionTable.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Parent root = newScene.getRoot();
                // Add a global mouse click listener
                root.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                    Object target = event.getTarget();

                    // Check if the target node or its parent is a TextField or ChoiceBox
                    boolean isInInputField = isDescendantOf(target, TextField.class) ||
                            isDescendantOf(target, ChoiceBox.class) ||
                            isDescendantOf(target, Button.class) ||
                            isDescendantOf(target, TableRow.class)
                            ;
                    if (!isInInputField) {
                        questionTable.getSelectionModel().clearSelection();
                    }
                });
            }
        });
        // Add a listener to resize the questionColumn dynamically
        questionTable.widthProperty().addListener((observable, oldWidth, newWidth) -> {
            // Calculate the available width for the questionColumn
            double fixedWidth = optionAColumn.getWidth() + optionBColumn.getWidth() +
                    optionCColumn.getWidth() + optionDColumn.getWidth() +
                    answerColumn.getWidth() + typeColumn.getWidth() +
                    scoreColumn.getWidth() + 2; // Account for borders
            double availableWidth = newWidth.doubleValue() - fixedWidth;

            // Set the column width to fill the remaining space
            questionColumn.setPrefWidth(Math.max(availableWidth, questionColumn.getMinWidth())); // Minimum width of 100
        });
    }

    /**
     * Populates the input fields with the properties of the given question.
     *
     * @param question the question to populate the input fields with
     */
    private void populateFields(Question question) {
        questionInput.setText(question.getContent());
        aInput.setText(question.getOptions().size() > 0 ? question.getOptions().get(0) : "");
        bInput.setText(question.getOptions().size() > 1 ? question.getOptions().get(1) : "");
        cInput.setText(question.getOptions().size() > 2 ? question.getOptions().get(2) : "");
        dInput.setText(question.getOptions().size() > 3 ? question.getOptions().get(3) : "");
        answerInput.setText(String.valueOf(question.answerProperty().get()));
        scoreInput.setText(String.valueOf(question.getScore()));
        typeInput.setValue(question.getTypeChoice() == 0 ? "Single" : "Multiple");
    }

    /**
     * Shows an alert dialog with the given type, title, and message.
     *
     * @param type the type of the alert dialog
     * @param title the title of the alert dialog
     * @param message the message to display in the alert dialog
     */
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Validates the inputs in the input fields.
     *
     * @return true if all inputs are valid, false otherwise
     */
    private boolean validateInputs() {
        if (questionInput.getText() == null || questionInput.getText().trim().isEmpty()) {
            questionInput.requestFocus();
            questionInput.getOnMouseClicked();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Question content is required.");
            return false;
        }

        if (aInput.getText() == null || aInput.getText().trim().isEmpty()) {
            aInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option A is required.");
            return false;
        }

        if (bInput.getText() == null || bInput.getText().trim().isEmpty()) {
            bInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option B is required.");
            return false;
        }

        if (cInput.getText() == null || cInput.getText().trim().isEmpty()) {
            cInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option C is required.");
            return false;
        }

        if (dInput.getText() == null || dInput.getText().trim().isEmpty()) {
            dInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option D is required.");
            return false;
        }

        if (answerInput.getText() == null || answerInput.getText().isEmpty()) {
            answerInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Answer is required.");
            return false;
        }

        if (scoreInput.getText() == null || scoreInput.getText().isEmpty()) {
            scoreInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Score is required.");
            return false;
        }
        int score = 0;
        try {
            score = Integer.parseInt(scoreInput.getText()); // Validate numeric input
        } catch (NumberFormatException e) {
            scoreInput.requestFocus();
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Score must be a valid number.");
            return false;
        }

        if ((score <0) || (score >1000000000)){
            scoreInput.requestFocus();
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Score must be a valid number.");
            return false;
        }

        if (typeInput.getValue() == null || typeInput.getValue().equals("Type")) {
            typeInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Please select a question type.");
            return false;
        }

        if (typeInput.getValue().equals("Single") && answerInput.getText().length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }

        return true; // All fields are valid
    }



    private void clearInputFields() {
        questionInput.clear();
        aInput.clear();
        bInput.clear();
        cInput.clear();
        dInput.clear();
        answerInput.clear();
        scoreInput.clear();
        typeInput.setValue("Type");
    }

    /**
     * Checks if the given target is a descendant of a node of the specified type.
     *
     * @param target the node to check
     * @param nodeClass the type of the node to check for
     * @return true if the node is a descendant of a node of the specified type, false otherwise
     */
    private boolean isDescendantOf(Object target, Class<?> nodeClass) {
        if (!(target instanceof Node)) return false;

        Node node = (Node) target;
        while (node != null) {
            if (nodeClass.isInstance(node)) {
                return true;
            }
            node = node.getParent();
        }
        return false;
    }
    // Showing hint when input is invalid
    private void startHintHideTimer(Label hintLabel) {
        PauseTransition delay = new PauseTransition(Duration.seconds(2)); // Delay before hiding the hint
        delay.setOnFinished(event -> {
            hintLabel.setVisible(false);
            hintLabel.setPrefHeight(0);
        });
        delay.play();
    }

}
