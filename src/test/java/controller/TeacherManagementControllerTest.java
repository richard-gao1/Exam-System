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
import org.testfx.api.FxRobotInterface;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.NodeMatchers;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;

class TeacherManagementControllerTest extends ApplicationTest implements FxRobotInterface {
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
        teacher1 = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", Gender.list[0], 21, "econ", Position.list[1]);
        teacher2 = new Teacher("wktangaf", "comp3511", "Tang Wai Kin", Gender.list[0], 22, "fina", Position.list[0]);
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
    void addTeacher_valid1() {
        refresh();
        TableView<Teacher> teacherTable = find("#accountTable");
        ArrayList<Teacher> original = new ArrayList<>(teacherTable.getItems());

        String gender = Gender.list[0];
        String position = Position.list[2];

        add("kwtleung", "comp2711", "Kenneth Leung", 0, "18", "cse", 2);
        Teacher newTeacher = new Teacher("kwtleung", "comp2711", "Kenneth Leung", gender, 18, "cse", position);

        Teacher[] output = teacherTable.getItems().toArray(Teacher[]::new);
        original.add(newTeacher);
        Teacher[] expected = original.toArray(Teacher[]::new);
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
    void addTeacher_invalid1() {
        add("teacher", "comp2711", "Kenneth Leung", 0, "18a", "cse", 2);
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void addTeacher_invalid2() {
        add("teacher", "comp2711", "Kenneth Leung", -1, "18", "cse", 2);
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void addTeacher_invalid3() {
        add("teacher", "comp2711", "Kenneth Leung", 0, "18", "cse", -1);
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void addTeacher_invalid4() {
        add("whwma", "comp3111", "Ma Wai Him Wesley", 0, "21", "econ", 2);
        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    void writeSetFields(String username, String password, String name, int genderIndex, String age, String department, int positionIndex) {
        writeTextField("#usernameSet", username);
        writeTextField("#nameSet", name);
        chooseChoiceBox("#genderSet", genderIndex);
        writeTextField("#ageSet", age);
        writeTextField("#departmentSet", department);
        chooseChoiceBox("#positionSet", positionIndex);
        writeTextField("#passwordSet", password);
    }

    void add(String username, String password, String name, int genderIndex, String ageText, String department, int positionIndex) {
        writeSetFields(username, password, name, genderIndex, ageText, department, positionIndex);

        Button addBtn = find("#addBtn");
        clickOn(addBtn);
    }

    @Test
    void update_valid() {
        refresh();
        TableView<Teacher> accountTable = find("#accountTable");
        accountTable.getSelectionModel().select(0);
        Node node = lookup("#usernameColumn").nth(0).query();
        clickOn(node);

        ArrayList<Teacher> original = new ArrayList<>(accountTable.getItems());
        Teacher teacher = accountTable.getSelectionModel().getSelectedItem();
        Teacher newTeacher = new Teacher("kwtleung12", teacher.getPassword(), teacher.getName(), teacher.getGender(), teacher.getAge(), teacher.getDepartment(), teacher.getPosition());
        original.remove(0);
        original.add(newTeacher);

        update("kwtleung12", null, null, -1, null, null,-1);

        Teacher[] expected = original.toArray(Teacher[]::new);
        Teacher[] output = accountTable.getItems().toArray(Teacher[]::new);
        assertArrayEquals(expected, output);
    }

    @Test
    void update_invalid1() {
        refresh();

        update("kwtleung12", null, null, -1, "18a", null,-1);

        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    @Test
    void update_invalid2() {
        refresh();

        update(null, null, null, -1, "18a", null,-1);

        verifyThat("OK", NodeMatchers.isVisible());
        Button ok = find("OK");
        clickOn(ok);
    }

    void update(String username, String password, String name, int genderIndex, String ageText, String department, int positionIndex) {
        writeSetFields(username, password, name, genderIndex, ageText, department, positionIndex);

        Button updateBtn = find("#updateBtn");
        clickOn(updateBtn);
    }

    @Test
    void delete_valid1() {
        refresh();
        TableView<Teacher> accountTable = find("#accountTable");
        accountTable.getSelectionModel().select(0);
        Node node = lookup("#usernameColumn").nth(0).query();
        clickOn(node);

        ArrayList<Teacher> original = new ArrayList<>(accountTable.getItems());
        original.remove(0);

        delete();

        Teacher[] expected = original.toArray(Teacher[]::new);
        Teacher[] output = accountTable.getItems().toArray(Teacher[]::new);
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
}