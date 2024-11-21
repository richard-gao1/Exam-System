package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.Student;
import comp3111.examsystem.SystemDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
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
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a gender", ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
            reg = false;
        }
        String username = usernameTxt.getText();
        if (username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a username", ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
            reg = false;
        }
        String password = passwordTxt.getText();
        if (password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a password", ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
            reg = false;
        }
        String name = nameTxt.getText();
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a name", ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
            reg = false;
        }
        String dept = departmentTxt.getText();
        if (dept.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input a department", ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
            reg = false;
        }
        int age = 0;
        try {
            age = Integer.parseInt(ageTxt.getText());
        } catch(NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please input an age", ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
            reg = false;
        }
        if (!passwordTxt.getText().equals(passwordConfirmTxt.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Passwords do not match", ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
            reg = false;
        }
        if (reg) {
            String registered = SystemDatabase.registerStudent(new Student(username, password, name, gender, age, dept));
            if (registered.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Successfully Registered", ButtonType.OK);
                alert.setTitle("Registered!");
                alert.show();
                // TODO: perhaps reroute to the login page?
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentLoginUI.fxml"));
                    Scene scene = new Scene(fxmlLoader.load());
                    stage.setTitle("Student Login");
                    stage.setScene(scene);
                    stage.show();
                    ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Did not register - duplicate username", ButtonType.OK);
                alert.setTitle("Register error");
                alert.show();
            }
        } else {
            System.out.println("Did not register");
        }
    }
}
