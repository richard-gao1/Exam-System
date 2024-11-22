package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StudentManagementController implements Initializable {
    @FXML
    private ChoiceBox genderSet;
    @FXML
    private TextField departmentSet;
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
    private TableColumn departmentColumn;
    @FXML
    private TableColumn passwordColumn;

    private Manager manager;
    private Student updating;

    @FXML
    private TableView accountTable;
    @FXML
    private TextField usernameSet;
    @FXML
    private TextField nameSet;

    private boolean filtering = false;

    ObservableList<Student> studentList = FXCollections.observableArrayList();
    ObservableList<String> genderList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get all students
        getStudentList();
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountTable.setItems(studentList);

        genderList.addAll(Gender.list);
        genderSet.setItems(genderList);
    }

    public void getManager(Manager manager) {
        this.manager = manager;
    }

    private void getStudentList() {
        String username = "";
        String name = "";
        String department = "";
        if (filtering) {
            username = usernameFilter.getText();
            name = nameFilter.getText();
            department = departmentFilter.getText();
        }
        List<Student> students = SystemDatabase.getStudentList(username, name, department);
        studentList.clear();
        studentList.addAll(students);
    }

    @FXML
    public void refresh() {
        getStudentList();
    }

    @FXML
    public void reset() {
        filtering = false;
        getStudentList();
    }

    @FXML
    public void query() {
        filtering = true;
        getStudentList();
    }

    private Student newStudent(boolean existing) {
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
        if (existing && updating != null) {
            return updating.update(username, password, name, gender, age, department);
        } else {
            return new Student(username, password, name, gender, age, department);
        }
    }

    @FXML
    public void add() {
        Student newStudent = newStudent(false);
        String msg = SystemDatabase.registerStudent(newStudent);
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
            // no student is selected
        } else {
            String old_username = updating.getUsername();
            System.out.println("Updating student " + old_username);
            Student newStudent = newStudent(true);
            SystemDatabase.updateStudent(newStudent, old_username);
            refresh();
        }
    }

    @FXML
    public void delete() {
        if (updating == null) {
            // no student is selected
        } else {
            String username = updating.getUsername();
            SystemDatabase.removeStudent(username);
            refresh();
        }
    }

    public void selected(MouseEvent mouseEvent) {
        Student selectedItem = (Student) accountTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem != updating) {
            updating = selectedItem;
            usernameSet.setText(updating.getUsername());
            nameSet.setText(updating.getName());
            genderSet.getSelectionModel().select(updating.getGender());
            ageSet.setText(String.valueOf(updating.getAge()));
            departmentSet.setText(updating.getDepartment());
            passwordSet.setText(updating.getPassword());
        }
    }
}
