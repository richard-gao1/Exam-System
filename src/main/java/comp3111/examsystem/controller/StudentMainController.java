package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * The controller for the Student Main interface.
 */
public class StudentMainController implements Initializable {
    /**
     * The combo box for selecting an exam.
     */
    @FXML
    ComboBox<String> examCombox;

    /**
     * The current student instance.
     */
    Student student;

    /**
     * A map of exam names to their corresponding Exam objects.
     */
    Map<String, Exam> examPairs = new HashMap<>();

    /**
     * Initializes the controller when the FXML file is loaded.
     *
     * @param location  The location used to resolve relative paths for the root object, or
    null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root
    object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
//        System.out.println("in initialize");

//        examCombox.getSelectionModel().select("Option B");
    }

    /**
     * Initializes the student and sets up the exams for selection.
     *
     * @param student The current student.
     */
    public void initStudent(Student student) {
        this.student = student;
        // manually make a new course and add the student to it
        ArrayList<Student> students = new ArrayList<>();
        students.add(this.student);
        ArrayList<Exam> initExams = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();
        String[] options = {"option1", "option2", "option3", "option4"};
        Question q1 = new Question("Question 1", options, "A", 10, 0);
        Question q2 = new Question("Question 2", options, "AB", 10, 1);
        Question q3 = new Question("Question 3", options, "ABC", 10, 1);
        Question q4 = new Question("Question 4", options, "A", 10, 0);
        questions.add(q1);
        questions.add(q2);
        questions.add(q3);
        questions.add(q4);
        Course testCourse = new Course("CourseID","testCourse", "dept", null, students, initExams);
        Exam testExam = new Exam("exam", testCourse, false, 30, questions);
        initExams.add(testExam);

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

    /**
     * Opens the Quiz Taking UI and sets the selected exam.
     *
     * @param e The action event triggered by selecting an option in the combo box.
     */
    @FXML
    public void openExamUI(ActionEvent e) {
        FXMLLoader quizLoader = new FXMLLoader();
        quizLoader.setLocation(Main.class.getResource("QuizTakingUI.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Taking Exam");
        try {
            System.out.println("This is before loading");
            Parent root = quizLoader.load();
            QuizController quizController = quizLoader.getController();
            System.out.println("init exam with controller: " + quizController);
            String examName = examCombox.getValue();
            if (examName != null && !examName.isEmpty()) {
                quizController.setExam(examPairs.get(examName));
                stage.setScene(new Scene(root));
                stage.show();
                ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select Exam", ButtonType.OK);
                alert.setTitle("Exam selection error");
                alert.show();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Opens the Grade Statistics UI.
     */
    @FXML
    public void openGradeStatistic(ActionEvent e) {
        FXMLLoader gradeLoader = new FXMLLoader();
        gradeLoader.setLocation(Main.class.getResource("StudentGradeStatistic.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Grade Statistics");
        try {
            Parent root = gradeLoader.load();
            StudentGradeStatisticController gradeController = gradeLoader.getController();
            stage.setScene(new Scene(root));

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        stage.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }

    /**
     * Exits the application.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}
