package comp3111.examsystem.controller;

import comp3111.examsystem.AccountType;
import comp3111.examsystem.Student;
import comp3111.examsystem.Teacher;
import comp3111.examsystem.SystemDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

import java.net.URL;
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
    public void register(ActionEvent e) {
        boolean reg = true;
        String gender = genderChoice.getValue();
        if (gender == null){
            // TODO: gender pop up
            System.out.println("gender is null");
            reg = false;
        }
        String username = usernameTxt.getText();
        if (username.isEmpty()) {
            // TODO: username popup
            System.out.println("username is empty");
            reg = false;
        }
        String password = passwordTxt.getText();
        if (password.isEmpty()) {
            // TODO: password popup
            System.out.println("password is empty");
            reg = false;
        }
        String name = nameTxt.getText();
        if (name.isEmpty()) {
            // TODO: name popup
            System.out.println("name is empty");
            reg = false;
        }
        String dept = departmentTxt.getText();
        if (dept.isEmpty()) {
            // TODO: name popup
            System.out.println("dept is empty");
            reg = false;
        }
        int age = 0;
        try {
            age = Integer.parseInt(ageTxt.getText());
        } catch(NumberFormatException ex) {
            System.out.println("invalid age");
            reg = false;
            // TODO: age error
        }
        if (!passwordTxt.getText().equals(passwordConfirmTxt.getText())) {
            reg = false;
            System.out.println("Passwords do not match");
            // TODO: popup again
        }
        String pos = positionChoice.getValue();
        if (isTeacher){
            if (pos == null){
                System.out.println("pos is empty");
                reg = false;
            }
        }
        if (reg) {
            String registered = (isTeacher)
                    ? SystemDatabase.registerTeacher(new Teacher(username, password, name, gender, age, dept, pos))
                    : SystemDatabase.registerStudent(new Student(username, password, name, gender, age, dept));

            if (registered.isEmpty()) {
                System.out.println("Registered");
                // TODO: make a successfully registered pop up or something similar
                // TODO: perhaps reroute to the login page?
            } else {
                System.out.println(registered);
                // TODO: popup duplicate user
            }
        } else {
            System.out.println("Did not register");
        }
    }
}
