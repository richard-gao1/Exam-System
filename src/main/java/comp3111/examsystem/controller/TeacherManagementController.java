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

/**
 * Controller for Teacher Management UI
 * @author whwmaust2125
 */
public class TeacherManagementController implements Initializable {
    @FXML
    private Button refreshBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button updateBtn;
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
        refresh();
    }

    /**
     * Retrieves the list of teachers from the system database based on optional filters.
     */
    private void getTeacherList() {
        String username = "";
        String name = "";
        String department = "";
        if (filtering) {
            username = usernameFilter.getText().toLowerCase().trim();
            name = nameFilter.getText().toLowerCase().trim();
            department = departmentFilter.getText().toLowerCase().trim();
        }
        List<Teacher> teachers = SystemDatabase.getTeacherList(username, name, department);
        teacherList.clear();
        teacherList.addAll(teachers);
    }

    /**
     * Refreshes the list of teachers by fetching it from the system database and updating the UI
     table.
     */
    @FXML
    public void refresh() {
        getTeacherList();
    }

    /**
     * Resets the filtering criteria and refreshes the teacher list.
     */
    @FXML
    public void reset() {
        filtering = false;
        resetFilterFields();
        getTeacherList();
    }

    /**
     * Applies the current filter criteria (username, name and department) to query the system database for teachers matching those
     criteria.
     */
    @FXML
    public void query() {
        filtering = true;
        getTeacherList();
    }

    /**
     * Clears all text fields used for filtering teachers.
     */
    private void resetFilterFields() {
        usernameFilter.setText("");
        nameFilter.setText("");
        departmentFilter.setText("");
    }

    /**
     * Clears all text fields used for setting new or updating teacher attributes.
     */
    private void resetSetFields() {
        usernameSet.setText("");
        nameSet.setText("");
        departmentSet.setText("");
        passwordSet.setText("");
        ageSet.setText("");
    }

    /**
     * Creates a new Teacher instance based on the input fields.
     *
     * @param existing A flag indicating whether an existing teacher is being updated or a new one
    is created.
     * @return The newly created or updated Teacher instance.
     */
    private Teacher setTeacher(boolean existing) {
        String username = usernameSet.getText();
        String name = nameSet.getText();
        String gender = (String) genderSet.getSelectionModel().getSelectedItem();
        if (gender == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Invalid Gender");
            alert.setHeaderText("Empty gender input");
            alert.show();
            return null;
        }
        String ageText = ageSet.getText();
        int age = 20;
        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException e) {
            // invalid age input
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Invalid Number");
            alert.setHeaderText("Invalid age input");
            alert.show();
            return null;
        }
        String department = departmentSet.getText();
        String password = passwordSet.getText();
        String position = (String) positionSet.getSelectionModel().getSelectedItem();
        if (position == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Invalid Position");
            alert.setHeaderText("Empty position input");
            alert.show();
            return null;
        }
        if (existing && updating != null) {
            return updating.update(username, password, name, gender, age, department, position);
        } else {
            return new Teacher(username, password, name, gender, age, department, position);
        }
    }

    /**
     * Adds a new teacher to the system based on the input fields.
     */
    @FXML
    public void add() {
        Teacher newTeacher = setTeacher(false);
        if (newTeacher == null) return;
        resetSetFields();
        String msg = SystemDatabase.registerTeacher(newTeacher);
        if (!msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
            alert.setTitle("Register error");
            alert.show();
        }
        refresh();
    }

    /**
     * Updates an existing teacher's details in the system using the input fields.
     */
    @FXML
    public void update() {
        if (updating == null) {
            // no teacher is selected
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Update Error");
            alert.setHeaderText("No teacher is selected.");
            alert.show();
        } else {
            String old_username = updating.getUsername();
            System.out.println("Updating teacher " + old_username);
            Teacher newTeacher = setTeacher(true);
            if (newTeacher == null) return;
            resetSetFields();
            SystemDatabase.updateTeacher(newTeacher, old_username);
            refresh();
        }
    }

    /**
     * Deletes the selected teacher from the system.
     */
    @FXML
    public void delete() {
        if (updating == null) {
            // no teacher is selected
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Update Error");
            alert.setHeaderText("No teacher is selected.");
            alert.show();
        } else {
            String username = updating.getUsername();
            SystemDatabase.updateTeacher(null, username);
            refresh();
        }
    }

    /**
     * Handles the selection of a teacher in the table view, updating related UI components and
     setting the 'updating' teacher.
     *
     * @param mouseEvent The MouseEvent associated with the selection action.
     */
    public void selected(MouseEvent mouseEvent) {
        Teacher selectedItem = (Teacher) accountTable.getSelectionModel().getSelectedItem();
        if (selectedItem != updating) {
            resetSetFields();
            updating = selectedItem;
            if (updating != null) {
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
}
