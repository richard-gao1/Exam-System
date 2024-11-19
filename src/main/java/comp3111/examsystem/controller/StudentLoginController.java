package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentLoginController implements Initializable {
    @FXML
    private TextField usernameTxt;
    @FXML
    private PasswordField passwordTxt;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void login(ActionEvent e) {
//        FXMLLoader studentMainLoader = new FXMLLoader(Main.class.getResource("StudentMainUI.fxml"));
        FXMLLoader studentMainLoader = new FXMLLoader();
        studentMainLoader.setLocation(Main.class.getResource("StudentMainUI.fxml"));
        Stage stage = new Stage();

        Student account = null;
        // try to login
        try{
            account = (Student)SystemDatabase.login(usernameTxt.getText(), passwordTxt.getText(), AccountType.STUDENT);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (account != null) {
            stage.setTitle("Hi " + account.getName() +", Welcome to HKUST Examination System");
            try {
                System.out.println("init student");
                SystemDatabase.currentUser = account;
                System.out.println("This is before loading");
                Parent root = studentMainLoader.load();
                StudentMainController mainController = studentMainLoader.getController();
                stage.setScene(new Scene(root));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Invalid username or password", ButtonType.OK);
            alert.setTitle("Login error");
            alert.show();
        }
    }

    @FXML
    public void register(ActionEvent e) {
        FXMLLoader registerLoader = new FXMLLoader(Main.class.getResource("RegisterUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Register into the HKUST Examination System");
        try {
            stage.setScene(new Scene(registerLoader.load()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }
}
