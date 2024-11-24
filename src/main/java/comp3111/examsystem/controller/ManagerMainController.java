package comp3111.examsystem.controller;

import comp3111.examsystem.Main;
import comp3111.examsystem.Manager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the Manager Main view.
 * This class is responsible for handling user interactions and managing the state of the Manager Main view.
 * It initializes the UI components, sets up the data bindings, and handles events such as loading views and managing users.
 */
public class ManagerMainController implements Initializable {
    @FXML
    private VBox mainbox;

    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Opens the Student Management UI.
     */
    @FXML
    public void openStudentManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Student Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            StudentManagementController smc = fxmlLoader.getController();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Teacher Management UI.
     */
    @FXML
    public void openTeacherManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Teacher Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            TeacherManagementController tmc = fxmlLoader.getController();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Course Management UI.
     */
    @FXML
    public void openCourseManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CourseManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Course Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            CourseManagementController cmc = fxmlLoader.getController();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exits the application.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}
