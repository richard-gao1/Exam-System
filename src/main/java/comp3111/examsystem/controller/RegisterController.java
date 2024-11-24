package comp3111.examsystem.controller;

import comp3111.examsystem.Position;
import comp3111.examsystem.Student;
import comp3111.examsystem.Teacher;
import comp3111.examsystem.SystemDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.RowConstraints;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The RegisterController class manages the registration process for both students and
 teachers.
 */
public class RegisterController implements Initializable {

    @FXML
    private TextField ageTxt;

    @FXML
    private TextField departmentTxt;

    @FXML
    private ChoiceBox<String> genderChoice;

    @FXML
    private TextField nameTxt;

    @FXML
    private PasswordField passwordConfirmTxt;

    @FXML
    private PasswordField passwordTxt;

    @FXML
    private TextField usernameTxt;

    @FXML
    private ChoiceBox<String> positionChoice;

    @FXML
    private RowConstraints positionRow;

    @FXML
    private Label positionLabel;

    private boolean isTeacher = false;

    /**
     * Initializes the UI components.
     *
     * @param location  The location of the FXML file.
     * @param resources The resource bundle containing locale-specific objects.
     */
    public void initialize(URL location, ResourceBundle resources) {
        positionRow.setMinHeight(0);
        positionRow.setPrefHeight(0);
        positionRow.setMaxHeight(0);
        positionLabel.setVisible(false);
        positionChoice.setVisible(false);

        genderChoice.getItems().removeAll(genderChoice.getItems());
        genderChoice.getItems().addAll("Male", "Female");
    }

    /**
     * Configures the UI for teacher-specific settings.
     */
    public void teacherSet(){
        positionRow.setMinHeight(10);
        positionRow.setMaxHeight(30);
        positionRow.setPrefHeight(30);
        positionLabel.setVisible(true);
        positionChoice.setVisible(true);
        positionChoice.getItems().addAll(Position.list);
        isTeacher = true;
    }

    /**
     * Handles the registration action.
     *
     * @param e The action event triggered by the register button.
     */
    @FXML
    public void onRegister(ActionEvent e) {
        try {
            String gender = genderChoice.getValue();
            String username = usernameTxt.getText();
            String password = passwordTxt.getText();
            String name = nameTxt.getText();
            String dept = departmentTxt.getText();
            int age = Integer.parseInt(ageTxt.getText());
            String pos = positionChoice.getValue();
            if (validate(username,name,gender,age,pos,dept,password,passwordConfirmTxt.getText())) {
                String registered = (isTeacher)
                        ? SystemDatabase.registerTeacher(new Teacher(username, password, name, gender, age, dept, pos))
                        : SystemDatabase.registerStudent(new Student(username, password, name, gender, age, dept));

                if (registered.isEmpty()) {
                    if (showAlert(Alert.AlertType.INFORMATION,null, "Registration successful.").get()==ButtonType.OK){
                        genderChoice.setValue(null);
                        usernameTxt.clear();
                        passwordTxt.clear();
                        passwordConfirmTxt.clear();
                        nameTxt.clear();
                        departmentTxt.clear();
                        ageTxt.clear();
                        positionChoice.setValue(null);
                    }
                } else {
                    showAlert(Alert.AlertType.ERROR,"Duplicate User",registered);
                }
            } else {
                showAlert(Alert.AlertType.ERROR,"Registration Failed","Did not register.");
            }
        } catch (NumberFormatException ne) {
            showAlert(Alert.AlertType.ERROR, "Invalid age", "Age must be a positive integer.");
        } catch (IllegalArgumentException ie){
            showAlert(Alert.AlertType.ERROR, "Invalid input", ie.getMessage());
        } catch (Exception ex){
            return;
        }
    }

    /**
     * Shows an alert dialog with the specified type, title, and message.
     *
     * @param alertType The type of alert (e.g., INFORMATION, ERROR).
     * @param title     The title of the alert dialog.
     * @param message   The message to display in the alert dialog.
     * @return An Optional containing the button type clicked by the user.
     */
    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait();
    }

    /**
     * Validates the input data for user registration.
     *
     * @param userName   The username provided by the user.
     * @param name       The full name provided by the user.
     * @param gender     The gender selected by the user.
     * @param age        The age provided by the user.
     * @param position   The position selected by the teacher (if applicable).
     * @param department The department provided by the user.
     * @param pwd        The password provided by the user.
     * @param confirmPwd The confirmation password provided by the user.
     * @return True if all inputs are valid, false otherwise.
     * @throws IllegalArgumentException If any input is invalid.
     */
    private boolean validate(String userName, String name, String gender, int age, String position,String department,String pwd, String confirmPwd){
        if (userName==null || userName.trim().isEmpty()){
            usernameTxt.requestFocus();
            throw new IllegalArgumentException("Username cannot be empty or null.");
        }
        if (name == null || name.trim().isEmpty()){
            nameTxt.requestFocus();
            throw new IllegalArgumentException("Name cannot be empty or null.");
        }
        if (gender == null || gender.equals("Gender")){
            genderChoice.requestFocus();
            throw new IllegalArgumentException("Please select a gender.");
        }
        if (age <= 0 || age >= 1000){
            ageTxt.requestFocus();
            throw new IllegalArgumentException("Are you serious?");
        }
        if (isTeacher &&(position == null || position.equals("Position"))){
            positionChoice.requestFocus();
            throw new IllegalArgumentException("Please select a position.");
        }
        if (department == null || department.trim().isEmpty()){
            departmentTxt.requestFocus();
            throw new IllegalArgumentException("Department cannot be empty or null.");
        }
        if (pwd == null){
            passwordTxt.requestFocus();
            throw new IllegalArgumentException("Password cannot be empty.");
        }
        if (!confirmPwd.equals(pwd)){
            passwordConfirmTxt.requestFocus();
            throw new IllegalArgumentException("Passwords do not match.");
        }
        return true;
    }
}
