package comp3111.examsystem.controller;

import comp3111.examsystem.Course;
import comp3111.examsystem.Manager;
import comp3111.examsystem.Student;
import comp3111.examsystem.SystemDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CourseManagementController implements Initializable {
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
    private Manager manager;
    private boolean filtering = false;
    
    private SystemDatabase systemDatabase;
    
    private ObservableList<Course> courseList = FXCollections.observableArrayList();

    public void getManager(Manager manager) {
        this.manager = manager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.systemDatabase = new SystemDatabase();
        getCourseList();
        accountTable.setItems(courseList);
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
        List<Course> courses = systemDatabase.getCourseList(courseID, name, department);
        courseList.clear();
        courseList.addAll(courses);
    }

    @FXML
    public void refresh() {
        getCourseList();
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

    private Course newCourse() {
        String courseID = courseIDSet.getText();
        String name = courseNameSet.getText();
        String department = departmentSet.getText();
        return new Course(courseID, name, department, new ArrayList<>(), new ArrayList<>());
    }

    @FXML
    public void add() {
        Course newCourse = newCourse();
        systemDatabase.createCourse(newCourse);
        refresh();
    }

    @FXML
    public void modify() {
        if (updating == null) {
            // no student is selected
        } else {
            String old_courseID = updating.getCourseID();
            System.out.println("Updating course " + old_courseID);
            Course newCourse = newCourse();
            systemDatabase.modifyCourse(newCourse, old_courseID, manager);
            refresh();
        }
    }

    @FXML
    public void delete() {
        if (updating == null) {
            // no student is selected
        } else {
            String courseID = updating.getCourseID();
            systemDatabase.removeCourse(courseID, manager);
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
        }
    }
}
