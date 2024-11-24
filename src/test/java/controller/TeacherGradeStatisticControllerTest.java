package controller;

import comp3111.examsystem.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TeacherGradeStatisticControllerTest extends ApplicationTest {
    private Teacher teacher1;

    @Override
    public void start(Stage stage) throws IOException {
        teacher1 = new Teacher("teacher", "comp3111", "Teacher", Gender.list[0], 21, "cse", Position.list[1]);
        SystemDatabase.registerTeacher(teacher1);
        SystemDatabase.currentUser = teacher1;
        Parent parent = FXMLLoader.load(Main.class.getResource("TeacherGradeStatistic.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Grade Statistics");
        stage.show();
        stage.toFront();
    }

    public <T extends Node> T find(final String query) {
        /* TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).query();
    }

    @BeforeEach
    void initialize() {
        SystemDatabase.removeAll();
        new SystemDatabase();
        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", Gender.list[0], 21, "econ");
        Student student2 = new Student("wktangaf", "comp3511", "Tang Wai Kin", Gender.list[0], 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "Male", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);

        Course course1 = new Course("COMP3511", "Operating Systems", "cse");
        course1.setTeacher(teacher1);
        course1.addStudent(student1);
        course1.addStudent(student2);

        Course course2 = new Course("COMP3111", "Software Engineering", "cse");
        course2.setTeacher(teacher1);
        course2.addStudent(student2);
        course2.addStudent(student3);
        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.updateTeacher(teacher1);

        ArrayList<Question> questionList = new ArrayList<>();
        String[] options = {"This", "is", "a", "question"};
        questionList.add(new Question(
                "This is a question",
                options,
                "A",
                100,
                0
        ));
        Exam exam1 = new Exam(
                "midterm",
                "COMP3511",
                true,
                60,
                questionList
        );
        Exam exam2 = new Exam(
                "final",
                "COMP3111",
                true,
                60,
                questionList
        );

        exam1.gradeStudent(student1, 0, 60);
        SystemDatabase.getCourse("COMP3511").updateGrade(exam1);

        exam1.gradeStudent(student2, 100, 60);
        SystemDatabase.getCourse("COMP3511").updateGrade(exam1);

        exam2.gradeStudent(student2, 100, 60);
        SystemDatabase.getCourse("COMP3111").updateGrade(exam2);

        exam2.gradeStudent(student3, 100, 60);
        SystemDatabase.getCourse("COMP3111").updateGrade(exam2);
    }


    @Test
    void refresh() {
        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);
    }

    @Test
    void reset() {
        Button resetBtn = find("#resetBtn");
        clickOn(resetBtn);
    }

    @Test
    void query() {
        Button filterBtn = find("#filterBtn");
        clickOn(filterBtn);
    }
}