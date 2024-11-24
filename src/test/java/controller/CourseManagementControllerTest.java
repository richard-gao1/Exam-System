package controller;

import comp3111.examsystem.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobotInterface;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

class CourseManagementControllerTest extends ApplicationTest implements FxRobotInterface {

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(Main.class.getResource("CourseManagementUI.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Course Management");
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
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        Course course2 = new Course("COMP3511", "Operating Systems", "cse");
        Course course3 = new Course("COMP3711", "Design and Analysis of Algorithms", "cse");
        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.createCourse(course3);

        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", Gender.list[0], 21, "econ");
        Student student2 = new Student("wktangaf", "comp3511", "Tang Wai Kin", Gender.list[0], 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", Gender.list[0], 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);

        Teacher teacher1 = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", Gender.list[0], 21, "econ", Position.list[1]);
        Teacher teacher2 = new Teacher("wktangaf", "comp3511", "Tang Wai Kin", Gender.list[0], 22, "fina", Position.list[0]);
        SystemDatabase.registerTeacher(teacher1);
        SystemDatabase.registerTeacher(teacher2);
    }

    @Test
    void refresh() {
        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);
        TableView<Course> courseTable = find("#courseTable");
        Course[] output = courseTable.getItems().toArray(Course[]::new);
        Course[] expected = SystemDatabase.getCourseList("", "", "").toArray(Course[]::new);
        assertArrayEquals(expected, output);
    }


    @Test
    void reset() {
        Button resetBtn = find("#resetBtn");
        TableView<Course> courseTable = find("#courseTable");
        clickOn(resetBtn);
        Course[] output = courseTable.getItems().toArray(Course[]::new);
        Course[] expected = SystemDatabase.getCourseList("", "", "").toArray(Course[]::new);
        assertArrayEquals(output, expected);
    }

    @Test
    void query1() {
        String courseIDFilter = "COMP3111";
        String courseNameFilter = "";
        String departmentFilter = "";
        Course[] expected = SystemDatabase.getCourseList(courseIDFilter, courseNameFilter, departmentFilter).toArray(Course[]::new);

        writeTextField("#courseIDFilter", courseIDFilter);
        writeTextField("#courseNameFilter", courseNameFilter);
        writeTextField("#departmentFilter", departmentFilter);

        Button filterBtn = find("#filterBtn");
        clickOn(filterBtn);

        TableView<Course> courseTable = find("#courseTable");

        Course[] output = courseTable.getItems().toArray(Course[]::new);
        assertArrayEquals(expected, output);
    }

    void checkTextField(String id, String content) {
        TextField textField = find(id);
        assertEquals(content, textField.getText());
    }

    void writeTextField(String id, String content) {
        if (content == null) return;
        TextField textField = find(id);
        textField.clear();
        clickOn(textField).write(content);
    }

    @Test
    void addCourse_valid1() {
        TableView<Course> courseTable = find("#courseTable");
        ArrayList<Course> original = new ArrayList<>(courseTable.getItems());

        add("ECON3113", "Microeconomics Theory I", "econ");
        Course newCourse = new Course("ECON3113", "Microeconomics Theory I", "econ");

        refresh();
        Course[] output = courseTable.getItems().toArray(Course[]::new);
        original.add(newCourse);
        Course[] expected = original.toArray(Course[]::new);
        assertArrayEquals(expected, output);
        checkSetFieldEmpty();
    }

    void checkSetFieldEmpty() {
        checkTextField("#courseIDSet", "");
        checkTextField("#courseNameSet", "");
        checkTextField("#departmentSet", "");
    }

    @Test
    void addCourse_invalid1() {
        add("COMP3111", "Software Engineering", "cse");
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    void writeSetFields(String courseID, String courseName, String department) {
        writeTextField("#courseIDSet", courseID);
        writeTextField("#courseNameSet", courseName);
        writeTextField("#departmentSet", department);
    }

    void add(String courseID, String courseName, String department) {
        writeSetFields(courseID, courseName, department);

        Button addBtn = find("#addBtn");
        clickOn(addBtn);
    }

    @Test
    void modify_valid() {
        refresh();
        TableView<Course> courseTable = find("#courseTable");
        courseTable.getSelectionModel().select(0);
        Node node = lookup("#courseIDColumn").nth(0).query();
        clickOn(node);

        ArrayList<Course> original = new ArrayList<>(courseTable.getItems());
        Course course = courseTable.getSelectionModel().getSelectedItem();
        Course newCourse = new Course("COMP5111", course.getCourseName(), course.getDepartment());
        original.remove(0);
        original.add(newCourse);

        modify("COMP5111", null, null);

        Course[] expected = original.toArray(Course[]::new);
        Course[] output = courseTable.getItems().toArray(Course[]::new);
        assertArrayEquals(expected, output);
    }

    @Test
    void modify_invalid() {
        refresh();

        modify("COMP5111", null, null);

        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    void modify(String courseID, String courseName, String department) {
        writeSetFields(courseID, courseName, department);

        Button modifyBtn = find("#modifyBtn");
        clickOn(modifyBtn);
    }

    @Test
    void delete_valid1() {
        refresh();
        TableView<Course> courseTable = find("#courseTable");
        courseTable.getSelectionModel().select(0);
        Node node = lookup("#courseIDColumn").nth(0).query();
        clickOn(node);

        ArrayList<Course> original = new ArrayList<>(courseTable.getItems());
        original.remove(0);

        delete();

        Course[] expected = original.toArray(Course[]::new);
        Course[] output = courseTable.getItems().toArray(Course[]::new);
        assertArrayEquals(expected, output);
    }

    @Test
    void delete_invalid1() {
        refresh();
        delete();
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    void delete() {
        Button deleteBtn = find("#deleteBtn");
        clickOn(deleteBtn);
    }

    @Test
    void addStudent_valid() {
        SystemDatabase.createCourse(new Course("COMP3111", "Software Engineering", "cse"));

        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);

        TableView<Course> courseTable = find("#courseTable");
        courseTable.getSelectionModel().select(0);
        Node node = lookup("#courseIDColumn").nth(0).query();
        clickOn(node);

        TableView<Student> notEnrollTable = find("#notEnrollTable");
        TableView<Student> enrollTable = find("#enrollTable");

        notEnrollTable.getSelectionModel().select(0);
        node = lookup("#notEnrollStudentUsername").nth(0).query();
        clickOn(node);

        Student selected = notEnrollTable.getSelectionModel().getSelectedItem();
        ArrayList<Student> originalNotEnroll = new ArrayList<Student>(notEnrollTable.getItems());
        ArrayList<Student> originalEnroll = new ArrayList<Student>(enrollTable.getItems());
        originalNotEnroll.remove(selected);
        originalEnroll.add(selected);

        Button addStudentBtn = find("#addStudentBtn");
        clickOn(addStudentBtn);

        Student[] expectEnroll = originalEnroll.toArray(Student[]::new);
        Student[] expectNotEnroll = originalNotEnroll.toArray(Student[]::new);

        Student[] outputEnroll = enrollTable.getItems().toArray(Student[]::new);
        Student[] outputNotEnroll = notEnrollTable.getItems().toArray(Student[]::new);

        assertArrayEquals(expectEnroll, outputEnroll);
        assertArrayEquals(expectNotEnroll, outputNotEnroll);
    }

    @Test
    void removeStudent_valid() {
        refresh();

        Course comp3111 = SystemDatabase.getCourse("COMP3111");
        assertNotNull(comp3111);
        comp3111.addStudent(SystemDatabase.getStudent("whwma"));

        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);

        TableView<Course> courseTable = find("#courseTable");
        courseTable.getSelectionModel().select(0);
        Node node = lookup("#courseIDColumn").nth(0).query();
        clickOn(node);

        TableView<Student> notEnrollTable = find("#notEnrollTable");
        TableView<Student> enrollTable = find("#enrollTable");

        enrollTable.getSelectionModel().select(0);
        node = lookup("#enrollStudentUsername").nth(0).query();
        clickOn(node);

        Student selected = enrollTable.getSelectionModel().getSelectedItem();
        ArrayList<Student> originalNotEnroll = new ArrayList<Student>(notEnrollTable.getItems());
        ArrayList<Student> originalEnroll = new ArrayList<Student>(enrollTable.getItems());
        originalNotEnroll.add(selected);
        originalEnroll.remove(selected);

        Button removeStudentBtn = find("#removeStudentBtn");
        clickOn(removeStudentBtn);

        Student[] expectEnroll = originalEnroll.toArray(Student[]::new);
        Student[] expectNotEnroll = originalNotEnroll.toArray(Student[]::new);

        Student[] outputEnroll = enrollTable.getItems().toArray(Student[]::new);
        Student[] outputNotEnroll = notEnrollTable.getItems().toArray(Student[]::new);

        assertArrayEquals(expectEnroll, outputEnroll);
        assertArrayEquals(expectNotEnroll, outputNotEnroll);
    }

    @Test
    void addStudent_invalid() {
        refresh();

        Button addStudentBtn = find("#addStudentBtn");
        clickOn(addStudentBtn);

        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void removeStudent_invalid() {
        refresh();

        Button removeStudentBtn = find("#removeStudentBtn");
        clickOn(removeStudentBtn);

        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void addTeacher() {
        refresh();

        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);

        TableView<Course> courseTable = find("#courseTable");
        courseTable.getSelectionModel().select(0);
        Node node = lookup("#courseIDColumn").nth(0).query();
        clickOn(node);

        String courseID = courseTable.getSelectionModel().getSelectedItem().getCourseID();

        TableView<Teacher> teacherTable = find("#teacherTable");
        teacherTable.getSelectionModel().select(0);
        node = lookup("#teacherUsername").nth(0).query();
        clickOn(node);

        Teacher selected = teacherTable.getSelectionModel().getSelectedItem();

        Button modifyBtn = find("#modifyBtn");
        clickOn(modifyBtn);

        Course course = SystemDatabase.getCourse(courseID);
        assertEquals(selected, course.getTeacher());
    }

    @Test
    void removeTeacher() {
        refresh();

        Course comp3111 = SystemDatabase.getCourse("comp3111");
        assertNotNull(comp3111);
        comp3111.setTeacher(SystemDatabase.getTeacher("whwma"));

        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);

        TableView<Course> courseTable = find("#courseTable");
        courseTable.getSelectionModel().select(0);
        Node node = lookup("#courseIDColumn").nth(0).query();
        clickOn(node);

        Button removeTeacher = find("#removeTeacher");
        clickOn(removeTeacher);

        Button modifyBtn = find("#modifyBtn");
        clickOn(modifyBtn);

        comp3111 = SystemDatabase.getCourse("comp3111");
        assertNull(comp3111.getTeacher());
    }
}