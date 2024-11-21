package comp3111.examsystem.controller;

import comp3111.examsystem.Course;
import comp3111.examsystem.Manager;
import comp3111.examsystem.SystemDatabase;
import comp3111.examsystem.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for Course Management UI
 * @author whwmaust2125
 */
public class CourseManagementController implements Initializable {
    @FXML
    private TableColumn usernameColumn;
    @FXML
    private TableColumn nameColumn;
    @FXML
    private TableView teacherTable;
    @FXML
    private TextField courseIDSet;
    @FXML
    private TextField courseNameSet;
    @FXML
    private TextField departmentSet;
    @FXML
    private TableView accountTable;
    @FXML
    private TableColumn courseIDColumn;
    @FXML
    private TableColumn courseNameColumn;
    @FXML
    private TableColumn departmentColumn;
    @FXML
    private TextField courseIDFilter;
    @FXML
    private TextField courseNameFilter;
    @FXML
    private TextField departmentFilter;
    private Course updating;
    private boolean filtering = false;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
        accountTable.setItems(courseList);
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        teacherTable.setItems(teacherList);
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    /**
     * Retrieves the list of courses from the system database based on optional filters.
     */
    private void getCourseList() {
        String courseID = "";
        String name = "";
        String department = "";
        if (filtering) {
            courseID = courseIDFilter.getText().toLowerCase().trim();
            name = courseNameFilter.getText().toLowerCase().trim();
            department = departmentFilter.getText().toLowerCase().trim();
        }
        List<Course> courses = SystemDatabase.getCourseList(courseID, name, department);
        courseList.clear();
        courseList.addAll(courses);
    }

    /**
     * Retrieves the list of teachers from the system database.
     */
    private void getTeacherList() {
        teacherList.clear();
        teacherList.addAll(SystemDatabase.getTeacherList("", "", ""));
    }

    /**
     * Refreshes both the course and teacher lists by fetching them from the system database and
     updating the UI tables.
     */
    @FXML
    public void refresh() {
        getCourseList();
        getTeacherList();
    }

    /**
     * Resets the filtering criteria and refreshes the course list.
     */
    @FXML
    public void reset() {
        filtering = false;
        resetFilterFields();
        getCourseList();
    }

    /**
     * Applies the current filter criteria to query the system database for courses matching those
     criteria.
     */
    @FXML
    public void query() {
        filtering = true;
        getCourseList();
    }

    /**
     * Creates a new Course instance based on the input fields, and assign teacher into the course.
     *
     * @param existing A flag indicating whether an existing course is being updated or a new one
    is created.
     * @return The newly created or updated Course instance.
     */
    private Course newCourse(boolean existing) {
        String courseID = courseIDSet.getText();
        String name = courseNameSet.getText();
        String department = departmentSet.getText();
        Course course = (existing) ?
                updating.update(courseID, name, department) :
                new Course(courseID, name, department, new ArrayList<>(), new ArrayList<>());
        Teacher teacher = (Teacher) teacherTable.getSelectionModel().getSelectedItem();
        course.setTeacher(teacher);
        return course;
    }

    /**
     * Adds a new course to the system based on the input fields.
     */
    @FXML
    public void add() {
        Course newCourse = newCourse(false);
        resetSetFields();
        String msg = SystemDatabase.createCourse(newCourse);
        if (!msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
            alert.setTitle("Creation error");
            alert.show();
        }
        refresh();
    }

    /**
     * Updates an existing course's details in the system using the input fields.
     */
    @FXML
    public void modify() {
        if (updating == null) {
            // no student is selected
        } else {
            String old_courseID = updating.getCourseID();
            resetSetFields();
            System.out.println("Updating course " + old_courseID);
            Course newCourse = newCourse(true);
            SystemDatabase.modifyCourse(newCourse, old_courseID);
            refresh();
        }
    }

    /**
     * Deletes the selected course from the system.
     */
    @FXML
    public void delete() {
        if (updating == null) {
            // no student is selected
        } else {
            String courseID = updating.getCourseID();
            SystemDatabase.modifyCourse(null, courseID);
            refresh();
        }
    }

    /**
     * Handles the selection of a course in the table view, updating related UI components and
     setting the 'updating' course.
     *
     * @param mouseEvent The MouseEvent associated with the selection action.
     */
    public void selected(MouseEvent mouseEvent) {
        Course selectedItem = (Course) accountTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem != updating) {
            updating = selectedItem;
            courseIDSet.setText(updating.getCourseID());
            courseNameSet.setText(updating.getCourseName());
            departmentSet.setText(updating.getDepartment());
            Teacher teacher = updating.getTeacher();
            if (teacherList.contains(teacher)) {
                teacherTable.getSelectionModel().select(teacher);
            }
        } 
        if (selectedItem == null) {
            resetSetFields();
        }
    }

    /**
     * Clears all text fields used for setting new or updating course attributes.
     */
    private void resetSetFields() {
        courseIDSet.setText("");
        courseNameSet.setText("");
        departmentSet.setText("");
    }

    /**
     * Clears all text fields used for filtering teachers.
     */
    private void resetFilterFields() {
        courseIDFilter.setText("");
        courseNameFilter.setText("");
        departmentSet.setText("");
    }

    /**
     * Handles the selection of a teacher in the table view.
     *
     * @param mouseEvent The MouseEvent associated with the selection action.
     */
    public void teacherSelected(MouseEvent mouseEvent) {

    }

    /**
     * Removes the selected teacher from the UI table.
     */
    public void removeTeacher() {
        teacherTable.getSelectionModel().clearSelection();
    }
}
