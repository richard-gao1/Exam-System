package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class StudentMainController implements Initializable {
    @FXML
    ComboBox<String> examCombox;

    Student student;
    Map<String, Exam> examPairs = new HashMap<>();

    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("in initialize");

//        examCombox.getSelectionModel().select("Option B");
    }

    public void initStudent(Student student) {
        this.student = student;
        // manually make a new course and add the student to it
        ArrayList<Student> students = new ArrayList<>();
        students.add(this.student);
        ArrayList<Exam> initExams = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();
        String[] options = {"option1", "option2", "option3", "option4"};
        Question q1 = new Question("Question 1", options, "", 0, 0);
        questions.add(q1);
        Exam testExam = new Exam("exam", "testCourse", false, 30, questions);
        initExams.add(testExam);
        Course testCourse = new Course("testCourse", "CourseName", "dept", students, initExams);

        ArrayList<Course> courses = student.getCourses();
        ArrayList<Exam> exams = new ArrayList<Exam>();
        for (Course course : courses) {
            exams.addAll(course.getExams());
        }
        for (Exam exam : exams) {
            examPairs.put(exam.getExamName(), exam);
        }
        examCombox.getItems().removeAll(examCombox.getItems());
        examCombox.getItems().addAll(examPairs.keySet());
    }

    @FXML
    public void openExamUI(ActionEvent e) {
        FXMLLoader quizLoader = new FXMLLoader();
        quizLoader.setLocation(Main.class.getResource("QuizTakingUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Taking Exam");
        try {

            System.out.println("This is before loading");
            Parent root = quizLoader.load();
            /*
            QuizController quizController = quizLoader.getController();
            System.out.println("init exam with controller: " + quizController);
            String examName = examCombox.getValue();
            if (!examName.isEmpty()) {
                quizController.setExam(examPairs.get(examName));
            } else {
                // TODO: select an exam pop up
                System.out.println("Select an Exam");
            }
             */
            stage.setScene(new Scene(root));

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    @FXML
    public void openGradeStatistic() {
    }

    @FXML
    public void exit() {
        System.exit(0);
    }
}
