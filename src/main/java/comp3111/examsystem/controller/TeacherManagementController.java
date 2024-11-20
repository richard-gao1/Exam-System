package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TeacherManagementController implements Initializable {
    @FXML
    private ChoiceBox genderSet;
    @FXML
    private TextField departmentSet;
    @FXML
    private ChoiceBox positionSet;
    @FXML
    private TextField ageSet;
    @FXML
    private TextField passwordSet;
    @FXML
    private TextField usernameFilter;
    @FXML
    private TextField nameFilter;
    @FXML
    private TextField departmentFilter;
    @FXML
    private Button resetBtn;
    @FXML
    private Button filterBtn;
    @FXML
    private TableColumn usernameColumn;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableColumn genderColumn;
    @FXML
    private TableColumn ageColumn;
    @FXML
    private TableColumn positionColumn;
    @FXML
    private TableColumn departmentColumn;
    @FXML
    private TableColumn passwordColumn;

    private Teacher updating;

    @FXML
    private TableView accountTable;
    @FXML
    private TextField usernameSet;
    @FXML
    private TextField nameSet;

    private boolean filtering = false;

    ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    ObservableList<String> genderList = FXCollections.observableArrayList();
    ObservableList<String> positionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get all teachers
        getTeacherList();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountTable.setItems(teacherList);

        genderList.addAll(Gender.list);
        genderSet.setItems(genderList);

        positionList.addAll(Position.list);
        positionSet.setItems(positionList);
    }

    private void getTeacherList() {
        String username = "";
        String name = "";
        String department = "";
        if (filtering) {
            username = usernameFilter.getText();
            name = nameFilter.getText();
            department = departmentFilter.getText();
        }
        List<Teacher> teachers = SystemDatabase.getTeacherList(username, name, department);
        teacherList.clear();
        teacherList.addAll(teachers);
    }

    @FXML
    public void refresh() {
        getTeacherList();
    }

    @FXML
    public void reset() {
        filtering = false;
        getTeacherList();
    }

    @FXML
    public void query() {
        filtering = true;
        getTeacherList();
    }

    private Teacher newTeacher(boolean existing) {
        String username = usernameSet.getText();
        String name = nameSet.getText();
        String gender = (String) genderSet.getSelectionModel().getSelectedItem();
        String ageText = ageSet.getText();
        int age = 20;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            // invalid age input
        }
        String department = departmentSet.getText();
        String password = passwordSet.getText();
        String position = (String) positionSet.getSelectionModel().getSelectedItem();
        if (existing && updating != null) {
            return updating.update(username, password, name, gender, age, department, position);
        } else {
            return new Teacher(username, password, name, gender, age, department, position);
        }
    }

    @FXML
    public void add() {
        Teacher newTeacher = newTeacher(false);
        String msg = SystemDatabase.registerTeacher(newTeacher);
        if (!msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
        }
        refresh();
    }

    @FXML
    public void update() {
        if (updating == null) {
            // no teacher is selected
        } else {
            String old_username = updating.getUsername();
            System.out.println("Updating teacher " + old_username);
            Teacher newTeacher = newTeacher(true);
            SystemDatabase.updateTeacher(newTeacher, old_username);
            refresh();
        }
    }

    @FXML
    public void delete() {
        if (updating == null) {
            // no teacher is selected
        } else {
            String username = updating.getUsername();
            SystemDatabase.updateTeacher(null, username);
            refresh();
        }
    }

    public void selected(MouseEvent mouseEvent) {
        Teacher selectedItem = (Teacher) accountTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem != updating) {
            updating = selectedItem;
            usernameSet.setText(updating.getUsername());
            nameSet.setText(updating.getName());
            genderSet.getSelectionModel().select(updating.getGender());
            ageSet.setText(String.valueOf(updating.getAge()));
            positionSet.getSelectionModel().select(updating.getPosition());
            departmentSet.setText(updating.getDepartment());
            passwordSet.setText(updating.getPassword());
        }
    }
}
