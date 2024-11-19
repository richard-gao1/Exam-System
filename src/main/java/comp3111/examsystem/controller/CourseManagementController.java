package comp3111.examsystem.controller;

import comp3111.examsystem.Course;
import comp3111.examsystem.Manager;
import comp3111.examsystem.SystemDatabase;
import comp3111.examsystem.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CourseManagementController implements Initializable {
    @FXML
    private TableColumn usernameColumn;
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
    private Teacher selectedTeacher;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        refresh();
        accountTable.setItems(courseList);
        teacherTable.setItems(teacherList);
        teacherTable.getSelectionModel().setSelectionMode(
                SelectionMode.MULTIPLE
        );
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        courseIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
    }

    private void getCourseList() {
        String courseID = "";
        String name = "";
        String department = "";
        if (filtering) {
            courseID = courseIDFilter.getText();
            name = courseNameFilter.getText();
            department = departmentFilter.getText();
        }
        List<Course> courses = SystemDatabase.getCourseList(courseID, name, department);
        courseList.clear();
        courseList.addAll(courses);
    }

    @FXML
    public void refresh() {
        getCourseList();
        teacherList.clear();
        teacherList.addAll(SystemDatabase.getTeacherList("", "", ""));
    }

    @FXML
    public void reset() {
        filtering = false;
        getCourseList();
    }

    @FXML
    public void query() {
        filtering = true;
        getCourseList();
    }

    private Course updateCourse() {
        String courseID = courseIDSet.getText();
        String name = courseNameSet.getText();
        String department = departmentSet.getText();
        if (selectedTeacher != null) {
            updating.setTeacher(selectedTeacher);
        }
        updating.update(courseID, name, department);
        return updating;
    }

    @FXML
    public void add() {
        Course newCourse = newCourse();
        String msg = SystemDatabase.createCourse(newCourse);
        if (!msg.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, msg, ButtonType.OK);
            alert.setTitle("Creation error");
            alert.show();
        }
        refresh();
    }

    private Course newCourse() {
        String courseID = courseIDSet.getText();
        String name = courseNameSet.getText();
        String department = departmentSet.getText();
        Course course = new Course(courseID, name, department);
        if (selectedTeacher != null) {
            course.setTeacher(selectedTeacher);
        }
        return course;
    }

    @FXML
    public void modify() {
        if (updating == null) {
            // no student is selected
        } else {
            String old_courseID = updating.getCourseID();
            System.out.println("Updating course " + old_courseID);
            Course newCourse = updateCourse();
            SystemDatabase.modifyCourse(newCourse, old_courseID);
            refresh();
        }
    }

    @FXML
    public void delete() {
        if (updating == null) {
            // no student is selected
        } else {
            String courseID = updating.getCourseID();
            SystemDatabase.removeCourse(courseID);
            refresh();
        }
    }

    public void selected(MouseEvent mouseEvent) {
        Course selectedItem = (Course) accountTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null && selectedItem != updating) {
            updating = selectedItem;
            courseIDSet.setText(updating.getCourseID());
            courseNameSet.setText(updating.getCourseName());
            departmentSet.setText(updating.getDepartment());
            teacherTable.getSelectionModel().clearSelection();
            if (SystemDatabase.getTeacher(updating.getTeacher().getUsername()) == null) updating.setTeacher(null);
            if (updating.getTeacher() != null) teacherTable.getSelectionModel().select(updating.getTeacher());
        } else {
            teacherList.clear();
        }
    }

    public void teacherSelected(MouseEvent mouseEvent) {
        selectedTeacher = (Teacher) teacherTable.getSelectionModel().getSelectedItem();
    }
}
