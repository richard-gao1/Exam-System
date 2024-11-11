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

    private Manager manager;
    private Teacher updating;

    SystemDatabase systemDatabase;

    @FXML
    private TableView accountTable;
    @FXML
    private TextField usernameSet;
    @FXML
    private TextField nameSet;

    ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    ObservableList<String> genderList = FXCollections.observableArrayList();
    ObservableList<String> positionList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        systemDatabase = new SystemDatabase();
        // get all teachers
        getTeacherList(false);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        positionColumn.setCellValueFactory(new PropertyValueFactory<>("position"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

        genderList.addAll(Gender.list);
        genderSet.setItems(genderList);

        positionList.addAll(Position.list);
        positionSet.setItems(positionList);
    }

    public void getManager(Manager manager) {
        this.manager = manager;
    }

    private void getTeacherList(boolean haveFilter) {
        String username = "";
        String name = "";
        String department = "";
        if (haveFilter) {
            username = usernameFilter.getText();
            name = nameFilter.getText();
            department = departmentFilter.getText();
        }
        List<Teacher> teachers = systemDatabase.getTeacherList(manager, username, name, department);
        teacherList.addAll(teachers);
        accountTable.setItems(teacherList);
    }

    @FXML
    public void refresh() {
        List<Teacher> teachers = systemDatabase.getTeacherList(manager, "", "", "");
        teacherList.addAll(teachers);
    }

    @FXML
    public void reset() {
        getTeacherList(false);
    }

    @FXML
    public void query() {
        getTeacherList(true);
    }

    private Teacher newTeacher() {
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
        return new Teacher(username, password, name, gender, age, department, position);
    }

    @FXML
    public void add() {
        Teacher newTeacher = newTeacher();
        systemDatabase.registerTeacher(newTeacher);
    }

    @FXML
    public void update() {
        if (updating == null) {
            // no teacher is selected
        } else {
            String old_username = updating.username;
            Teacher newTeacher = newTeacher();
            systemDatabase.updateTeacher(newTeacher, old_username, manager);
            getTeacherList(true);
        }
    }

    @FXML
    public void delete() {
        if (updating == null) {
            // no teacher is selected
        } else {
            String username = updating.username;
            systemDatabase.removeTeacher(username, manager);
            getTeacherList(true);
        }
    }

    public void selected(MouseEvent mouseEvent) {
        Teacher updating = (Teacher) accountTable.getSelectionModel().getSelectedItem();
        if (updating != null) {
            usernameSet.setText(updating.getUsername());
        }
    }
}
