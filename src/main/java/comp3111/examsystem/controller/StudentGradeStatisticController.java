package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class StudentGradeStatisticController implements Initializable {

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private CategoryAxis categoryAxisBar;

    @FXML
    private CategoryAxis categoryAxisLine;

    @FXML
    private TableColumn<Grade, String> courseColumn;

    @FXML
    private ChoiceBox<String> courseCombox;

    @FXML
    private TableColumn<Grade, String> examColumn;

    @FXML
    private Button filterBtn;

    @FXML
    private TableColumn<Grade, String> fullScoreColumn;

    @FXML
    private TableView<Grade> gradeTable;

    @FXML
    private NumberAxis numberAxisBar;

    @FXML
    private NumberAxis numberAxisLine;

    @FXML
    private Button resetBtn;

    @FXML
    private TableColumn<Grade, String> scoreColumn;

    @FXML
    private TableColumn<Grade, String> timeSpendColumn;

    private final ObservableList<Grade> gradeList = FXCollections.observableArrayList();
    private final ObservableList<Grade> displayGradeList = FXCollections.observableArrayList();

    private final ObservableList<String> courseList = FXCollections.observableArrayList();
    private final ObservableList<String> examList = FXCollections.observableArrayList();

    private final HashMap<String, Integer> by_course = new HashMap<>();
    private final HashMap<String, Integer> by_exam = new HashMap<>();

    private final XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();;

    private String courseFilter = "";

    private Student student;
    private boolean hasFilter = false;

    private String removeNull(String input) { return (input == null) ? "" : input; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.student = (Student) SystemDatabase.currentUser;
        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Exam");
        numberAxisBar.setLabel("Score");

        gradeTable.setItems(displayGradeList);
        courseCombox.setItems(courseList);

        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        refresh();
        loadChart();
    }

    private void loadChart() {
        barChart.getData().clear();
        barChart.setAnimated(false);
        barChart.getData().add(seriesBar);

    }

    private void loadChoices() {
        courseList.clear();
        courseList.add("");
        Set<String> courseIDs = new HashSet<>(student.getCourseIDs());
        for (String courseID : courseIDs) {
            Course course = SystemDatabase.getCourse(courseID);
            if (course == null) courseList.add(courseID);
            else courseList.add(course.getCourseID() + ": " + course.getCourseName());
        }

        examList.clear();
        examList.add("");
        examList.addAll(by_exam.keySet());
    }

    private void loadData() {
        gradeList.clear();
        ArrayList<Exam> exams = new ArrayList<>();
        if (student != null) exams = student.getExams();
        for (Exam exam : exams) {
            Grade grade = exam.getStudentGrades().get(this.student.getUsername());
            if (grade != null) {
                gradeList.add(grade);
            }
        }

        updateHashMaps();
    }

    private void updateHashMaps() {
        by_course.clear();
        by_exam.clear();

        if (hasFilter) {
            displayGradeList.forEach((grade) -> {
                by_course.put(grade.getCourseNum(), grade.getScore());
                by_exam.put(grade.getExamName(), grade.getScore());
            });
        } else {
            gradeList.forEach((grade) -> {
                by_course.put(grade.getCourseNum(), grade.getScore());
                by_exam.put(grade.getExamName(), grade.getScore());
            });
        }
    }

    private void updateFilter(String courseFilter) {
        displayGradeList.clear();
        if (!hasFilter) {
            this.courseFilter = "";
            displayGradeList.addAll(gradeList);
            return;
        }
        this.courseFilter = courseFilter;

        ArrayList<Grade> filteredGradeList = gradeList.stream().filter(
                g -> ((courseFilter.isEmpty() || Objects.equals(courseFilter, g.getCourseNum())))
        ).collect(Collectors.toCollection(ArrayList::new));

        displayGradeList.addAll(filteredGradeList);
        updateHashMaps();
    }

    private void updateChart() {
        seriesBar.getData().clear();

        by_exam.forEach((exam, score) -> {
            seriesBar.getData().add(new XYChart.Data<>(exam, score));
        });
    }

    @FXML
    void query(ActionEvent event) {
        hasFilter = true;
        updateFilter(removeNull((String)courseCombox.getSelectionModel().getSelectedItem()).split(":")[0]);
        updateChart();
    }

    @FXML
    void refresh() { //ActionEvent event) {
        loadData();
        loadChoices();
        updateFilter(courseFilter);
        updateChart();
    }

    @FXML
    void reset(ActionEvent event) {
        hasFilter = false;
        refresh();
    }

}
