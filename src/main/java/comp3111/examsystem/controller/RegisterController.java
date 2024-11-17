package comp3111.examsystem.controller;

import comp3111.examsystem.Student;
import comp3111.examsystem.SystemDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

    public void initialize(URL location, ResourceBundle resources) {
        genderChoice.getItems().removeAll(genderChoice.getItems());
        genderChoice.getItems().addAll("Male", "Female");
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
        if (reg) {
            String registered = SystemDatabase.registerStudent(new Student(username, password, name, gender, age, dept));
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
