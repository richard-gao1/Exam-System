package comp3111.examsystem.controller;

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

    public void initialize(URL location, ResourceBundle resources) {
        positionRow.setMinHeight(0);
        positionRow.setPrefHeight(0);
        positionRow.setMaxHeight(0);
        positionLabel.setVisible(false);
        positionChoice.setVisible(false);

        genderChoice.getItems().removeAll(genderChoice.getItems());
        genderChoice.getItems().addAll("Male", "Female");
    }

    public void teacherSet(){
        positionRow.setMinHeight(10);
        positionRow.setMaxHeight(30);
        positionRow.setPrefHeight(30);
        positionLabel.setVisible(true);
        positionChoice.setVisible(true);
        positionChoice.getItems().addAll("Chair Professor","Professor", "Associate Professor", "Assistant Professor","Senior Lecturer","Lecturer");
        isTeacher = true;
    }

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

    private Optional<ButtonType> showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        return alert.showAndWait();
    }

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
