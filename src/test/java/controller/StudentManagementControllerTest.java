package controller;

import comp3111.examsystem.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

class StudentManagementControllerTest extends ApplicationTest {
    Student student1;
    Student student2;
    Student student3;

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(Main.class.getResource("StudentManagementUI.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Student Management");
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
        student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", Gender.list[0], 21, "econ");
        student2 = new Student("wktangaf", "comp3511", "Tang Wai Kin", Gender.list[0], 22, "fina");
        student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "Male", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);
    }

    @Test
    void refresh() {
        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);
        TableView<Student> accountTable = find("#accountTable");
        Student[] output = accountTable.getItems().toArray(Student[]::new);
        Student[] expected = SystemDatabase.getStudentList("", "", "").toArray(Student[]::new);
        assertArrayEquals(expected, output);
    }


    @Test
    void reset() {
        Button resetBtn = find("#resetBtn");
        TableView<Student> accountTable = find("#accountTable");
        clickOn(resetBtn);
        Student[] output = accountTable.getItems().toArray(Student[]::new);
        Student[] expected = SystemDatabase.getStudentList("", "", "").toArray(Student[]::new);
        assertArrayEquals(output, expected);
    }

    @Test
    void query1() {
        String usernameFilter = "whwma";
        String nameFilter = "";
        String departmentFilter = "";
        Student[] expected = SystemDatabase.getStudentList(usernameFilter, nameFilter, departmentFilter).toArray(Student[]::new);

        writeTextField("#usernameFilter", usernameFilter);
        writeTextField("#nameFilter", nameFilter);
        writeTextField("#departmentFilter", departmentFilter);

        Button filterBtn = find("#filterBtn");
        clickOn(filterBtn);

        TableView<Student> accountTable = find("#accountTable");

        Student[] output = accountTable.getItems().toArray(Student[]::new);
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

    void chooseChoiceBox(String id, int index) {
        if (index < 0) return;
        ChoiceBox choiceBox = find(id);
        clickOn(choiceBox);
        for (int i=0;i<index;i++) {
            type(KeyCode.DOWN);
        }
        type(KeyCode.ENTER);
    }

    @Test
    void addStudent_valid1() {
        refresh();
        TableView<Student> studentTable = find("#accountTable");
        ArrayList<Student> original = new ArrayList<>(studentTable.getItems());

        String gender = Gender.list[0];

        add("kwtleung", "comp2711", "Kenneth Leung", 0, "18", "cse");
        Student newStudent = new Student("kwtleung", "comp2711", "Kenneth Leung", gender, 18, "cse");

        Student[] output = studentTable.getItems().toArray(Student[]::new);
        original.add(newStudent);
        Student[] expected = original.toArray(Student[]::new);
        assertArrayEquals(expected, output);
        checkSetFieldEmpty();
    }

    void checkSetFieldEmpty() {
        checkTextField("#usernameSet", "");
        checkTextField("#nameSet", "");
        checkTextField("#ageSet", "");
        checkTextField("#departmentSet", "");
        checkTextField("#passwordSet", "");
    }

    @Test
    void addStudent_invalid1() {
        add("student", "comp2711", "Kenneth Leung", 0, "18a", "cse");
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void addStudent_invalid2() {
        add("student", "comp2711", "Kenneth Leung", -1, "18", "cse");
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void addStudent_invalid3() {
        add("whwma", "comp3111", "Ma Wai Him Wesley", 0, "21", "econ");
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    void writeSetFields(String username, String password, String name, int genderIndex, String age, String department) {
        writeTextField("#usernameSet", username);
        writeTextField("#nameSet", name);
        chooseChoiceBox("#genderSet", genderIndex);
        writeTextField("#ageSet", age);
        writeTextField("#departmentSet", department);
        writeTextField("#passwordSet", password);
    }

    void add(String username, String password, String name, int genderIndex, String ageText, String department) {
        writeSetFields(username, password, name, genderIndex, ageText, department);

        Button addBtn = find("#addBtn");
        clickOn(addBtn);
    }

    @Test
    void update_valid() {
        refresh();
        TableView<Student> accountTable = find("#accountTable");
        accountTable.getSelectionModel().select(0);
        Node node = lookup("#usernameColumn").nth(0).query();
        clickOn(node);

        ArrayList<Student> original = new ArrayList<>(accountTable.getItems());
        Student student = accountTable.getSelectionModel().getSelectedItem();
        Student newStudent = new Student("kwtleung12", student.getPassword(), student.getName(), student.getGender(), student.getAge(), student.getDepartment());
        original.set(0, newStudent);

        update("kwtleung12", null, null, -1, null, null);

        List<Student> output = accountTable.getItems();
        assertTrue(output.contains(newStudent));
        assertFalse(output.contains(student));
    }

    @Test
    void update_invalid2() {
        refresh();

        update(null, null, null, -1, "18a", null);

        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    void update(String username, String password, String name, int genderIndex, String ageText, String department) {
        writeSetFields(username, password, name, genderIndex, ageText, department);

        Button updateBtn = find("#updateBtn");
        clickOn(updateBtn);
    }

    @Test
    void delete_valid1() {
        refresh();
        TableView<Student> accountTable = find("#accountTable");
        accountTable.getSelectionModel().select(0);
        Node node = lookup("#usernameColumn").nth(0).query();
        clickOn(node);

        Student student = accountTable.getSelectionModel().getSelectedItem();

        ArrayList<Student> original = new ArrayList<>(accountTable.getItems());

        delete();

        List<Student> output = accountTable.getItems();
        assertFalse(output.contains(student));
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

}