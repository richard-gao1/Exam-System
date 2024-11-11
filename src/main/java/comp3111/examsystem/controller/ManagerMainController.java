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

public class ManagerMainController implements Initializable {
    @FXML
    private VBox mainbox;

    private Manager manager;

    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void openStudentManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("StudentManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Student Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openTeacherManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("TeacherManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Teacher Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            TeacherManagementController tmc = fxmlLoader.getController();
            tmc.getManager(manager);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void openCourseManageUI() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CourseManagementUI.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Course Management");
            stage.setScene(new Scene(fxmlLoader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        System.exit(0);
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
