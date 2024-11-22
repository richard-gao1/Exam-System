package comp3111.examsystem.controller;

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
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

class TeacherManagementControllerTest extends ApplicationTest {
    Teacher teacher1;
    Teacher teacher2;

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(Main.class.getResource("TeacherManagementUI.fxml"));
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Teacher Management");
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
        teacher1 = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Associate Professor");
        teacher2 = new Teacher("wktangaf", "comp3511", "Tang Wai Kin", "M", 22, "fina", "Professor");
        SystemDatabase.registerTeacher(teacher1);
        SystemDatabase.registerTeacher(teacher2);
    }

    @Test
    void refresh() {
        Button refreshBtn = find("#refreshBtn");
        clickOn(refreshBtn);
        TableView<Teacher> accountTable = find("#accountTable");
        Teacher[] output = accountTable.getItems().toArray(Teacher[]::new);
        Teacher[] expected = SystemDatabase.getTeacherList("", "", "").toArray(Teacher[]::new);
        assertArrayEquals(expected, output);
    }


    @Test
    void reset() {
        Button resetBtn = find("#resetBtn");
        TableView<Teacher> accountTable = find("#accountTable");
        clickOn(resetBtn);
        Teacher[] output = accountTable.getItems().toArray(Teacher[]::new);
        Teacher[] expected = SystemDatabase.getTeacherList("", "", "").toArray(Teacher[]::new);
        assertArrayEquals(output, expected);
    }

    @Test
    void query1() {
        String usernameFilter = "whwma";
        String nameFilter = "";
        String departmentFilter = "";
        Teacher[] expected = SystemDatabase.getTeacherList(usernameFilter, nameFilter, departmentFilter).toArray(Teacher[]::new);

        writeTextField("#usernameFilter", usernameFilter);
        writeTextField("#nameFilter", nameFilter);
        writeTextField("#departmentFilter", departmentFilter);

        Button filterBtn = find("#filterBtn");
        clickOn(filterBtn);

        TableView<Teacher> accountTable = find("#accountTable");

        Teacher[] output = accountTable.getItems().toArray(Teacher[]::new);
        assertArrayEquals(expected, output);
    }

    void writeTextField(String id, String content) {
        TextField textField = find(id);
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
    void addTeacher_valid1() {
        addTeacher_valid("kwtleung", "comp2711", "Kenneth Leung", 0, 18, "cse", 2);
    }

    @Test
    void addTeacher_invalid1() {
        addTeacher_invalid("teacher", "comp2711", "Kenneth Leung", 0, "18a", "cse", 2);
    }

    @Test
    void addTeacher_invalid2() {
        addTeacher_invalid("teacher", "comp2711", "Kenneth Leung", -1, "18", "cse", 2);
    }

    @Test
    void addTeacher_invalid3() {
        addTeacher_invalid("teacher", "comp2711", "Kenneth Leung", 0, "18", "cse", -1);
    }

    void addTeacher_valid(String username, String password, String name, int genderIndex, int age, String department, int positionIndex) {
        refresh();
        TableView<Teacher> teacherTable = find("#accountTable");
        ArrayList<Teacher> original = new ArrayList<>(teacherTable.getItems());

        String gender = Gender.list[genderIndex];
        String position = Position.list[positionIndex];

        Teacher newTeacher = new Teacher(username, password, name, gender, age, department, position);

        writeTextField("#usernameSet", username);
        writeTextField("#nameSet", name);
        chooseChoiceBox("#genderSet", genderIndex);
        writeTextField("#ageSet", String.valueOf(age));
        writeTextField("#departmentSet", department);
        chooseChoiceBox("#positionSet", positionIndex);
        writeTextField("#passwordSet", password);

        Button addBtn = find("#addBtn");
        clickOn(addBtn);

        Teacher[] output = teacherTable.getItems().toArray(Teacher[]::new);
        original.add(newTeacher);
        Teacher[] expected = original.toArray(Teacher[]::new);
        assertArrayEquals(expected, output);
    }

    void addTeacher_invalid(String username, String password, String name, int genderIndex, String ageText, String department, int positionIndex) {
        refresh();
        TableView<Teacher> teacherTable = find("#accountTable");

        writeTextField("#usernameSet", username);
        writeTextField("#nameSet", name);
        chooseChoiceBox("#genderSet", genderIndex);
        writeTextField("#ageSet", ageText);
        writeTextField("#departmentSet", department);
        chooseChoiceBox("#positionSet", positionIndex);
        writeTextField("#passwordSet", password);

        Button addBtn = find("#addBtn");
        clickOn(addBtn);

        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void selected() {

    }
}