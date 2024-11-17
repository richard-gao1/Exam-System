package comp3111.examsystem.controller;


import comp3111.examsystem.Question;
import comp3111.examsystem.SystemDatabase;
import comp3111.examsystem.Teacher;
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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class QuestionBankController implements Initializable {

    @FXML private TextField questionFilter, scoreFilter;
    @FXML private ChoiceBox<String> typeFilter;
    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, String> questionColumn, optionAColumn, optionBColumn, optionCColumn, optionDColumn, answerColumn, typeColumn;
    @FXML private TableColumn<Question, Integer> scoreColumn;

    @FXML private TextField  questionInput, aInput, bInput, cInput, dInput, answerInput, scoreInput;
    @FXML private ChoiceBox<String> typeInput;

    @FXML private Button addBtn, updateBtn, deleteBtn, refreshBtn, resetBtn, filterBtn;
    private Teacher currentTeacher = (Teacher) SystemDatabase.currentUser;

    private ObservableList<Question> questionList = FXCollections.observableArrayList(currentTeacher.getQuestionBank());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        // Bind ChoiceBox with options
        typeInput.setItems(FXCollections.observableArrayList("Type", "Single", "Multiple"));
        typeInput.setValue("Type"); // Default selection
        typeFilter.setItems(FXCollections.observableArrayList("Type", "Single", "Multiple"));
        typeFilter.setValue("Type"); // Default selection

        // Load initial data into the table
        questionTable.setItems(questionList);

        // Placeholder
        questionFilter.setPromptText("Filter questions...");
        scoreFilter.setPromptText("Enter score...");

        // Add a listener to the selection model
        questionTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFields(newValue); // Populate input fields with selected question
                //updateBtn.setDisable(false);
                //deleteBtn.setDisable(false);
            } else {
                clearInputFields(); // Clear input fields
                //updateBtn.setDisable(true);
                //deleteBtn.setDisable(true);

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
    }



    @FXML
    private void onAdd() {
        if (!validateInputs()) {
            return; // Stop if validation fails
        }
        // Collect data from input fields
        String[] option = {aInput.getText(), bInput.getText(), cInput.getText(), dInput.getText()};
        int typeChoice = typeInput.getValue().equals("Single") ? 0 : 1;
        try {
            Question question = new Question(
                    questionInput.getText(),
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

    @FXML
    private void onUpdate() {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        // Validate Single Choice Answer
        if (!validateInputs()) {
            return; // Stop if validation fails
        }
        try {
            // Update Question Properties
            selected.setContent(questionInput.getText());
            selected.setOptions(new String[]{
                    aInput.getText(),
                    bInput.getText(),
                    cInput.getText(),
                    dInput.getText()
            });
            selected.setAnswer(answerInput.getText());
            selected.setTypeChoice(typeInput.getValue().equals("Multiple") ? 1 : 0);
            selected.setScore(Integer.parseInt(scoreInput.getText()));

            // Clear Input Fields and Refresh Table
            clearInputFields();
            questionTable.refresh();
            questionTable.getSelectionModel().clearSelection();

        } catch (IllegalArgumentException e) {
            showAlert(Alert.AlertType.ERROR, "Invalid Question", e.getMessage());
        }
    }

    @FXML
    private void onDelete() {
        Question selected = questionTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            // Remove from both the list and the question bank
            questionList.remove(selected);
            currentTeacher.getQuestionBank().remove(selected);
        }
    }

    @FXML
    private void onRefresh() {
        questionList.setAll(currentTeacher.getQuestionBank());
        questionTable.refresh();
        questionTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void onFilter() {
        String questionText = questionFilter.getText().toLowerCase();
        String type = typeFilter.getValue();
        String scoreText = scoreFilter.getText();

        questionTable.setItems(questionList.filtered(question -> {
            boolean matchesQuestion = questionText.isEmpty() || question.getContent().toLowerCase().contains(questionText);
            boolean matchesType = type == null || (type.equals("Single") && question.getTypeChoice() == 0) || (type.equals("Multiple") && question.getTypeChoice() == 1);
            boolean matchesScore = scoreText.isEmpty() || Integer.toString(question.getScore()).equals(scoreText);
            return matchesQuestion && matchesType && matchesScore;
        }));
    }

    @FXML
    private void onReset() {
        // Clear filter fields
        questionFilter.clear();
        scoreFilter.clear();
        typeFilter.setValue(null);
        questionTable.setItems(questionList); // Reset the table view to original data
    }

    // Utility Functions

    private void populateFields(Question question) {
        questionInput.setText(question.getContent());
        aInput.setText(question.getOptions().size() > 0 ? question.getOptions().get(0) : "");
        bInput.setText(question.getOptions().size() > 1 ? question.getOptions().get(1) : "");
        cInput.setText(question.getOptions().size() > 2 ? question.getOptions().get(2) : "");
        dInput.setText(question.getOptions().size() > 3 ? question.getOptions().get(3) : "");
        answerInput.setText(question.getAnswer());
        scoreInput.setText(String.valueOf(question.getScore()));
        typeInput.setValue(question.getTypeChoice() == 0 ? "Single" : "Multiple");
    }


    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean validateInputs() {
        if (questionInput.getText().isEmpty()) {
            questionInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Question content is required.");
            return false;
        }

        if (aInput.getText().isEmpty()) {
            aInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option A is required.");
            return false;
        }

        if (bInput.getText().isEmpty()) {
            bInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option B is required.");
            return false;
        }

        if (cInput.getText().isEmpty()) {
            cInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option C is required.");
            return false;
        }

        if (dInput.getText().isEmpty()) {
            dInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Option D is required.");
            return false;
        }

        if (answerInput.getText().isEmpty()) {
            answerInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Answer is required.");
            return false;
        }

        if (scoreInput.getText().isEmpty()) {
            scoreInput.requestFocus();
            showAlert(Alert.AlertType.WARNING, "Missing Input", "Score is required.");
            return false;
        }

        try {
            Integer.parseInt(scoreInput.getText()); // Validate numeric input
        } catch (NumberFormatException e) {
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
        typeInput.setValue(null);
    }

    // Utility method to check if a target is a descendant of a specific node type
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

}
