package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
     * Initializes the student and sets up the exams for selection.
     *
     * @param location  The location used to resolve relative paths for the root object, or
    null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root
    object was not localized.
     */
    public void initialize(URL location, ResourceBundle resources) {
        this.student = (Student) SystemDatabase.currentUser;
        initTestObjects();
        // add exam options to the dropdown
        ArrayList<Course> courses = student.getCourses();
        ArrayList<Exam> exams = new ArrayList<Exam>();
        for (Course course : courses) {
            exams.addAll(course.getExams());
        }
        for (Exam exam : exams) {
            if (exam.getIsPublished()) {
                examPairs.put(exam.getExamName(), exam);
            }
        }
        examCombox.getItems().removeAll(examCombox.getItems());
        examCombox.getItems().addAll(examPairs.keySet());
    }

    /**
     * Initializes courses, exams, and grades used for manual testing
     */
    public void initTestObjects() {
        System.out.println("Student before any course registration: " + student);

        // manually make a new course and add the student to it (reset course)
        SystemDatabase.removeCourse("CourseID0");
        SystemDatabase.removeCourse("CourseID1");
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
        Course course0 = new Course("CourseID0","Course0", "dept", null, students, initExams);
        Course course1 = new Course("CourseID1","Course1", "dept", null, students, initExams);

        System.out.println("Student after course initialized: " + student);
        SystemDatabase.createCourse(course0);
        SystemDatabase.createCourse(course1);
        Exam testExamUnpubC0 = new Exam("C0 examUnpublished", "CourseID0", true, 30, questions);
        Exam testExamPubC0 = new Exam("C0 examPublished", "CourseID0", true, 30, questions);
        Exam testExamUnpubC1 = new Exam("C1 examUnpublished", "CourseID1", true, 30, questions);
        Exam testExamPubC1 = new Exam("C1 examPublished", "CourseID1", true, 30, questions);

        // add test grades
        testExamPubC0.gradeStudent((Student) SystemDatabase.currentUser, 10, 12);
        testExamUnpubC0.gradeStudent((Student) SystemDatabase.currentUser, 15, 7);
        testExamPubC1.gradeStudent((Student) SystemDatabase.currentUser, 20, 19);
        testExamUnpubC1.gradeStudent((Student) SystemDatabase.currentUser, 5, 27);

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
        try {
            Parent root = quizLoader.load();
            QuizController quizController = quizLoader.getController();
            String examName = examCombox.getValue();
            stage.setTitle("Taking Exam: " + examName);
            if (examName != null && !examName.isEmpty()) {
                quizController.setExam(examPairs.get(examName));
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Select Exam", ButtonType.OK);
                alert.setTitle("Exam selection error");
                alert.show();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    public void openGradeStatistic(ActionEvent e) {
        FXMLLoader gradeLoader = new FXMLLoader();
        gradeLoader.setLocation(Main.class.getResource("StudentGradeStatistic.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Grade Statistics");
        try {
            Parent root = gradeLoader.load();
            stage.setScene(new Scene(root));

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        stage.show();
    }

    /**
     * Opens the Grade Statistics UI.
     */
    @FXML
    public void openGradeStatistic() {
    }

    /**
     * Exits the application.
     */
    @FXML
    public void exit() {
        System.exit(0);
    }
}
