package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controller for Teacher Grade Statistics UI
 */
public class TeacherGradeStatisticController implements Initializable {
    private Teacher currentTeacher;
    private boolean hasFilter = false;

    /**
     * A class to calculate and store the average score
     */
    private class AvgScore {
        private int count = 0; // Number of score added
        private int score = 0; // Total score
        AvgScore() {}

        /**
         * Adds a new score to the average calculation.
         *
         * @param score The score to be added.
         */
        public void add(int score) {
            count++;
            this.score += score;
        }

        /**
         * Calculates and returns the average score.
         *
         * @return The average score as a double. Returns 0.0 if no scores have been added.
         */
        public double getValue() {
            if (count == 0) return 0.0;
            return ((double) score) / ((double) count);
        }
    }

    @FXML
    private ChoiceBox<String> courseCombox;
    @FXML
    private ChoiceBox<String> examCombox;
    @FXML
    private ChoiceBox<String> studentCombox;
    @FXML
    private TableView<Grade> gradeTable;
    @FXML
    private TableColumn<Grade, String> studentColumn;
    @FXML
    private TableColumn<Grade, String> courseColumn;
    @FXML
    private TableColumn<Grade, String> examColumn;
    @FXML
    private TableColumn<Grade, String> scoreColumn;
    @FXML
    private TableColumn<Grade, String> fullScoreColumn;
    @FXML
    private TableColumn<Grade, String> timeSpendColumn;
    @FXML
    BarChart<String, Number> barChart;
    @FXML
    CategoryAxis categoryAxisBar;
    @FXML
    NumberAxis numberAxisBar;
    @FXML
    LineChart<String, Number> lineChart;
    @FXML
    CategoryAxis categoryAxisLine;
    @FXML
    NumberAxis numberAxisLine;
    @FXML
    PieChart pieChart;
    private String courseFilter = "";
    private String examFilter = "";
    private String studentFilter = "";

    private final ObservableList<String> courseList = FXCollections.observableArrayList();
    private final ObservableList<String> examList = FXCollections.observableArrayList();
    private final ObservableList<String> studentList = FXCollections.observableArrayList();

    private final HashMap<String, AvgScore> by_course = new HashMap<>();
    private final HashMap<String, AvgScore> by_student = new HashMap<>();
    private final HashMap<String, AvgScore> by_exam = new HashMap<>();

    private final XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();;
    private final XYChart.Series<String, Number> seriesLine = new XYChart.Series<>();;

    private ArrayList<Grade> gradeList = new ArrayList<>();
    private final ObservableList<Grade> displayGradeList = FXCollections.observableArrayList();

    /**
     * Removes a null value from a string, replacing it with an empty string if necessary.
     *
     * @param input The input string that may be null.
     * @return An empty string if the input is null, otherwise returns the original string.
     */
    private String removeNull(String input) { return (input == null) ? "" : input; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Course");
        numberAxisBar.setLabel("Avg. Score");
        pieChart.setLegendVisible(false);
        pieChart.setTitle("Student Scores");
        lineChart.setLegendVisible(false);
        categoryAxisLine.setLabel("Exam");
        numberAxisLine.setLabel("Avg. Score");

        gradeTable.setItems(displayGradeList);
        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        courseCombox.setItems(courseList);
        examCombox.setItems(examList);
        studentCombox.setItems(studentList);

        currentTeacher = (Teacher) SystemDatabase.currentUser;

        refresh();
        loadChart();
    }

    /**
     * Loads data into the grade list based on the current teacher's exams.
     */
    private void loadData() {
        gradeList.clear();
        ArrayList<Exam> exams = new ArrayList<>();
        List<Course> courses = currentTeacher.getCourses();
        if (courses != null) {
            for (Course course : courses) {
                exams.addAll(course.getExams());
            }
        }
        /*
         add grades from teachers
         */
        for (Exam exam : exams) {
            HashMap<String, Grade> studentGrade = exam.getStudentGrades();
            gradeList.addAll(studentGrade.values());
        }
        updateHashMaps(false);
    }

    /**
     * Refreshes the UI components by loading data, updating choices, filtering grades, and
     updating charts.
     */
    @FXML
    public void refresh() {
        loadData();
        loadChoices();
        updateFilter();
        updateChart();
    }

    /**
     * Updates hash maps that group grades by course, exam, and student.
     *
     * @param filtered A flag indicating whether to update with filtered or full grade list.
     */
    private void updateHashMaps(boolean filtered) {
        by_course.clear();
        by_student.clear();
        by_exam.clear();

        if (filtered) {
            displayGradeList.forEach((grade) -> {
                by_course.computeIfAbsent(grade.getCourseNum(), k -> new AvgScore()).add(grade.getScore());
                by_exam.computeIfAbsent(grade.getExamName(), k -> new AvgScore()).add(grade.getScore());
                by_student.computeIfAbsent(grade.getStudentName(), k -> new AvgScore()).add(grade.getScore());
            });
        } else {
            gradeList.forEach((grade) -> {
                by_course.computeIfAbsent(grade.getCourseNum(), k -> new AvgScore()).add(grade.getScore());
                by_exam.computeIfAbsent(grade.getExamName(), k -> new AvgScore()).add(grade.getScore());
                by_student.computeIfAbsent(grade.getStudentName(), k -> new AvgScore()).add(grade.getScore());
            });
        }
    }

    /**
     * Loads available choices for courses, exams, and students into respective UI components.
     */
    private void loadChoices() {
        courseList.clear();
        courseList.add("");
        Set<String> courseIDs = by_course.keySet();
        for (String courseID : courseIDs) {
            Course course = SystemDatabase.getCourse(courseID);
            if (course == null) courseList.add(courseID);
            else courseList.add(course.getCourseID() + " " + course.getCourseName());
        }

        examList.clear();
        examList.add("");
        examList.addAll(by_exam.keySet());

        studentList.clear();
        studentList.add("");
        studentList.addAll(by_student.keySet());
    }

    /**
     * Updates the bar, line, and pie charts with current data from hash maps.
     */
    private void updateChart() {
        seriesBar.getData().clear();
        pieChart.getData().clear();
        seriesLine.getData().clear();

        by_course.forEach((course, score) -> {
            seriesBar.getData().add(new XYChart.Data<>(course, score.getValue()));
        });
        by_exam.forEach((exam, score) -> {
            seriesLine.getData().add(new XYChart.Data<>(exam, score.getValue()));
        });
        by_student.forEach((student, score) -> {
            pieChart.getData().add(new PieChart.Data(student, score.getValue()));
        });
    }

    /**
     * Initializes or clears the chart components in preparation for data updates.
     */
    private void loadChart() {
        barChart.getData().clear();
        barChart.setAnimated(false);
        lineChart.getData().clear();
        lineChart.setAnimated(false);

        barChart.getData().add(seriesBar);

        lineChart.getData().add(seriesLine);
    }

    /**
     * Applies filters to the grade list based on selected course, exam, and student choices.
     */
    private void updateFilter() {
        displayGradeList.clear();
        if (!hasFilter) {
            displayGradeList.addAll(gradeList);
            return;
        }
        courseFilter = removeNull(courseCombox.getSelectionModel().getSelectedItem());
        examFilter = removeNull(examCombox.getSelectionModel().getSelectedItem());
        studentFilter = removeNull(studentCombox.getSelectionModel().getSelectedItem());

        ArrayList<Grade> filteredGradeList = gradeList.stream().filter(
                g -> ((courseFilter.isEmpty() || Objects.equals(courseFilter, g.getCourseNum())) &&
                        (examFilter.isEmpty() || Objects.equals(examFilter, g.getExamName())) &&
                        (studentFilter.isEmpty() || Objects.equals(studentFilter, g.getStudentName()))
                )
        ).collect(Collectors.toCollection(ArrayList::new));

        displayGradeList.addAll(filteredGradeList);
        updateHashMaps(true);
    }

    /**
     * Resets the filter settings and refreshes the displayed grades and charts.
     */
    @FXML
    public void reset() {
        hasFilter = false;
        updateFilter();
        updateChart();
    }

    /**
     * Sets the filter settings based on current selections and updates the displayed grades and
     charts.
     */
    @FXML
    public void query() {
        hasFilter = true;
        updateFilter();
        updateChart();
    }
}
