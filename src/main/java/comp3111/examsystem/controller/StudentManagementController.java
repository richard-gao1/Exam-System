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

/**
 * Controller for Student Management UI
 * @author whwmaust2125
 */
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
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        accountTable.setItems(studentList);

        genderList.addAll(Gender.list);
        genderSet.setItems(genderList);
        refresh();
    }

    /**
     * Retrieves the list of students from the system database based on optional filters.
     */
    private void getStudentList() {
        String username = "";
        String name = "";
        String department = "";
        if (filtering) {
            username = usernameFilter.getText().toLowerCase().trim();
            name = nameFilter.getText().toLowerCase().trim();
            department = departmentFilter.getText().toLowerCase().trim();
        }
        List<Student> students = SystemDatabase.getStudentList(username, name, department);
        studentList.clear();
        studentList.addAll(students);
    }

    /**
     * Refreshes the list of students by fetching it from the system database and updating the UI
     table.
     */
    @FXML
    public void refresh() {
        getStudentList();
    }

    /**
     * Resets the filtering criteria and refreshes the student list.
     */
    @FXML
    public void reset() {
        filtering = false;
        resetFilterFields();
        getStudentList();
    }

    /**
     * Applies the current filter criteria (username, name and department) to query the system database for students matching those
     criteria.
     */
    @FXML
    public void query() {
        filtering = true;
        getStudentList();
    }

    /**
     * Clears all text fields used for filtering students.
     */
    private void resetFilterFields() {
        usernameFilter.setText("");
        nameFilter.setText("");
        departmentFilter.setText("");
    }

    /**
     * Clears all text fields used for setting new student attributes.
     */
    private void resetSetFields() {
        usernameSet.setText("");
        nameSet.setText("");
        departmentSet.setText("");
        passwordSet.setText("");
        ageSet.setText("");
    }

    /**
     * Creates a new Student instance based on the input fields.
     *
     * @param existing A flag indicating whether an existing student is being updated or a new one
    is created.
     * @return The newly created or updated Student instance.
     */
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

    /**
     * Adds a new student to the system based on the input fields.
     */
    @FXML
    public void add() {
        Student newStudent = newStudent(false);
        resetSetFields();
        String msg = SystemDatabase.registerStudent(newStudent);
        if (!msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
        }
        refresh();
    }

    /**
     * Updates an existing student's details in the system using the input fields.
     */
    @FXML
    public void update() {
        if (updating == null) {
            // no student is selected
        } else {
            String old_username = updating.getUsername();
            System.out.println("Updating student " + old_username);
            Student newStudent = newStudent(true);
            resetSetFields();
            SystemDatabase.updateStudent(newStudent, old_username);
            refresh();
        }
    }

    /**
     * Deletes the selected student from the system.
     */
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

    /**
     * Handles the selection of a student in the table view, updating related UI components and
     setting the 'updating' student.
     *
     * @param mouseEvent The MouseEvent associated with the selection action.
     */
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
