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

    private void getTeacherList() {
        teacherList.clear();
        teacherList.addAll(SystemDatabase.getTeacherList("", "", ""));
    }

    @FXML
    public void refresh() {
        getCourseList();
        getTeacherList();
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
        Course course = updating.update(courseID, name, department);
        Teacher teacher = (Teacher) teacherTable.getSelectionModel().getSelectedItem();
        course.setTeacher(teacher);
        return course;
    }

    private Course newCourse() {
        String courseID = courseIDSet.getText();
        String name = courseNameSet.getText();
        String department = departmentSet.getText();
        Course course = new Course(courseID, name, department, new ArrayList<>(), new ArrayList<>());
        Teacher teacher = (Teacher) teacherTable.getSelectionModel().getSelectedItem();
        course.setTeacher(teacher);
        return course;
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
            SystemDatabase.modifyCourse(null, courseID);
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
            Teacher teacher = updating.getTeacher();
            if (teacherList.contains(teacher)) {
                teacherTable.getSelectionModel().select(teacher);
            }
        }
    }

    public void teacherSelected(MouseEvent mouseEvent) {

    }

    public void removeTeacher() {
        teacherTable.getSelectionModel().clearSelection();
    }
}
