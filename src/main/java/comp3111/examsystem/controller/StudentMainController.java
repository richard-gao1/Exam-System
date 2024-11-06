package comp3111.examsystem.controller;

import comp3111.examsystem.Course;
import comp3111.examsystem.Exam;
import comp3111.examsystem.Main;
import comp3111.examsystem.Student;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StudentMainController implements Initializable {
    @FXML
    ComboBox<String> examCombox;

    Student student;

    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("in initialize");

//        examCombox.getSelectionModel().select("Option B");
    }

    public void initStudent(Student student) {
        this.student = student;
        ArrayList<Course> courses = student.getCourses();
        ArrayList<Exam> exams = new ArrayList<Exam>();
        ArrayList<String> examNames = new ArrayList<>();
        for (Course course : courses) {
            exams.addAll(course.getExams());
        }
        for (Exam exam : exams) {
            examNames.add(exam.getExamName());
        }
        examCombox.getItems().removeAll(examCombox.getItems());
        examCombox.getItems().addAll(examNames);
    }

    @FXML
    public void openExamUI() {
    }

    @FXML
    public void openGradeStatistic() {
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
