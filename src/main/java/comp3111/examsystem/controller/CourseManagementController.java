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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for Course Management UI
 * @author whwmaust2125
 */
public class CourseManagementController implements Initializable {
    @FXML
    private Button addStudentBtn;
    @FXML
    private Button modifyBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button refreshBtn;
    @FXML
    private Button filterBtn;
    @FXML
    private Button resetBtn;
    @FXML
    private Button removeStudentBtn;
    @FXML
    private TableView enrollTable;
    @FXML
    private TableView notEnrollTable;
    @FXML
    private TableColumn enrollStudentUsername;
    @FXML
    private TableColumn enrollStudentName;
    @FXML
    private TableColumn notEnrollStudentUsername;
    @FXML
    private TableColumn notEnrollStudentName;
    @FXML
    private TableColumn teacherUsername;
    @FXML
    private TableColumn teacherName;
    @FXML
    private TableView teacherTable;
    @FXML
    private TextField courseIDSet;
    @FXML
    private TextField courseNameSet;
    @FXML
    private TextField departmentSet;
    @FXML
    private TableView courseTable;
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
    private Course updating = null;
    private boolean filtering = false;

    private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private ObservableList<Teacher> teacherList = FXCollections.observableArrayList();
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private ObservableList<Student> enrollList = FXCollections.observableArrayList();
    private ObservableList<Student> notEnrollList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
        courseTable.setItems(courseList);
        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        teacherTable.setItems(teacherList);
        teacherUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        teacherName.setCellValueFactory(new PropertyValueFactory<>("name"));

        enrollTable.setItems(enrollList);
        enrollStudentUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        enrollStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));

        notEnrollTable.setItems(notEnrollList);
        notEnrollStudentUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        notEnrollStudentName.setCellValueFactory(new PropertyValueFactory<>("name"));

        resetStudentList();
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
     * Retrieves the list of students from the system database.
     */
    private void getStudentList() {
        studentList.clear();
        studentList.addAll(SystemDatabase.getStudentList("", "", ""));
    }

    /**
     * Refreshes both the course and teacher lists by fetching them from the system database and
     updating the UI tables.
     */
    @FXML
    public void refresh() {
        getCourseList();
        getTeacherList();
        getStudentList();
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
    private Course setCourse(boolean existing) {
        String courseID = courseIDSet.getText();
        String name = courseNameSet.getText();
        String department = departmentSet.getText();
        Course course = (existing) ?
                updating.update(courseID, name, department) :
                new Course(courseID, name, department);
        Teacher teacher = (Teacher) teacherTable.getSelectionModel().getSelectedItem();
        course.setTeacher(teacher);
        course.addStudents(enrollList);
        return course;
    }

    /**
     * Adds a new course to the system based on the input fields.
     */
    @FXML
    public void add() {
        Course newCourse = setCourse(false);
        System.out.println(newCourse.getCourseName());
        if (newCourse == null) return;
        resetSetFields();
        resetStudentList();
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
            // no course is selected
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Update Error");
            alert.setHeaderText("No course is selected.");
            alert.show();
        } else {
            String old_courseID = updating.getCourseID();
            System.out.println("Updating course " + old_courseID);
            Course newCourse = setCourse(true);
            if (newCourse == null) return;
            resetSetFields();
            resetStudentList();
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
            // no course is selected
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Delete Error");
            alert.setHeaderText("No course is selected.");
            alert.show();
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
        Course selectedItem = (Course) courseTable.getSelectionModel().getSelectedItem();
        if (selectedItem != updating) {
            updating = selectedItem;
            if (updating != null) {
                courseIDSet.setText(updating.getCourseID());
                courseNameSet.setText(updating.getCourseName());
                departmentSet.setText(updating.getDepartment());
                Teacher teacher = updating.getTeacher();
                if (teacherList.contains(teacher)) {
                    teacherTable.getSelectionModel().select(teacher);
                }
                enrollList.clear();
                enrollList.addAll(updating.getStudents());
                notEnrollList.removeAll(updating.getStudents());
            } else {
                resetSetFields();
                resetStudentList();
            }

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
     * Puts all students into the not enrolled list
     */
    private void resetStudentList() {
        enrollList.clear();
        notEnrollList.clear();
        notEnrollList.addAll(studentList);
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

    /**
     * Handles the selection of a student in the table view.
     *
     * @param mouseEvent The MouseEvent associated with the selection action.
     */
    public void studentSelected(MouseEvent mouseEvent) {

    }

    /**
     * Adds the selected student from the "not enrolled" table to the "enrolled" table.
     *
     * @param actionEvent the action event that triggered this method
     */
    public void addStudent(ActionEvent actionEvent) {
        Student student = (Student) notEnrollTable.getSelectionModel().getSelectedItem();
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Add Student Error");
            alert.setHeaderText("No student is selected.");
            alert.show();
        }        enrollList.add(student);
        notEnrollList.remove(student);
    }

    /**
     * Removes the selected student from the "enrolled" table and adds it to the "not enrolled" table.
     *
     * @param actionEvent the action event that triggered this method
     */
    public void removeStudent(ActionEvent actionEvent) {
        Student student = (Student) enrollTable.getSelectionModel().getSelectedItem();
        if (student == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, null, ButtonType.OK);
            alert.setTitle("Remove Student Error");
            alert.setHeaderText("No student is selected.");
            alert.show();
        }
        notEnrollList.add(student);
        enrollList.remove(student);
    }
}
