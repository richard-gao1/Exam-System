package comp3111.examsystem.controller;

import java.io.IOException;

import comp3111.examsystem.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * This class is the controller for the select login page.
 * It provides methods for opening the login pages for students, teachers, and managers.
 */
public class SelectLoginController {

    /**
     * Opens the student login page.
     */
    @FXML
    public void studentLogin(ActionEvent e) {
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
    }

    /**
     * Opens the teacher login page.
     */
    @FXML
    public void teacherLogin(ActionEvent e) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Teacher Login");
            stage.setScene(scene);
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Opens the manager login page.
     */
    public void managerLogin(ActionEvent e) {
        try {
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ManagerLoginUI.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("Manager Login");
            stage.setScene(scene);
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
}
