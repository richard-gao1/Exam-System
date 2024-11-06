package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

        // stuff used to manually test
        SystemDatabase db = new SystemDatabase();
        Student student = new Student("username", "password", "studentName", "male", 21, "department");
        Student account = null;
        Exam testExam = new Exam("examName", null, false, 0, null);
        ArrayList<Exam> exams = new ArrayList<>();
        exams.add(testExam);
        student.addCourse(new Course("course", new ArrayList<>(), exams));
        try {
            db.registerStudent(student);
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // try to login
        try{
            account = (Student)db.login(usernameTxt.getText(), passwordTxt.getText(), AccountType.STUDENT);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        if (account != null) {
            stage.setTitle("Hi " + account.getName() +", Welcome to HKUST Examination System");
            try {
                System.out.println("This is before loading");
                Parent root = studentMainLoader.load();
                StudentMainController mainController = studentMainLoader.getController();
                System.out.println("init student");
                mainController.initStudent(account);
                stage.setScene(new Scene(root));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            stage.show();
            ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
        } else {
            // TODO: add login failed pop up
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
