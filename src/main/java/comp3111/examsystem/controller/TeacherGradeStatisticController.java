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

public class TeacherGradeStatisticController implements Initializable {

    private Teacher teacher;
    private boolean hasFilter = false;

    public void getTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    private class AvgScore {
        private int count = 0;
        private int score = 0;
        AvgScore() {}

        public void add(int score) {
            count++;
            this.score += score;
        }
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

        refresh();
        loadChart();
    }

    private void loadData() {
        gradeList.clear();
        gradeList.add(new Grade(
                "student",
                "comp3111",
                "final",
                100,
                100,
                60
        ));
        gradeList.add(new Grade(
                "student2",
                "comp3211",
                "midterm",
                70,
                100,
                60
        ));
        ArrayList<Exam> exams = new ArrayList<>();
        if (teacher != null) exams = teacher.getExams();
        exams.forEach((e) -> {
            HashMap<Student, Grade> studentGrade = e.getStudentGrades();
            gradeList.addAll(studentGrade.values());
        });
        updateHashMaps(false);
    }

    @FXML
    public void refresh() {
        loadData();
        loadChoices();
        updateFilter();
        updateChart();
    }

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

    private void loadChoices() {
        courseList.clear();
        courseList.add("");
        courseList.addAll(by_course.keySet());

        examList.clear();
        examList.add("");
        examList.addAll(by_exam.keySet());

        studentList.clear();
        studentList.add("");
        studentList.addAll(by_student.keySet());
    }

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

    private void loadChart() {
        barChart.getData().clear();
        lineChart.getData().clear();

        /*
        seriesBar.getData().clear();
        barChart.getData().clear();
        for (int i = 0;  i < 5; i++) {
            seriesBar.getData().add(new XYChart.Data<>("COMP" + i, 50));
        }
        */
        barChart.getData().add(seriesBar);

        /*
        pieChart.getData().clear();
        for (int i = 0;  i < 4; i++) {
            pieChart.getData().add(new PieChart.Data("student" + i, 80));
        }

        seriesLine.getData().clear();
        lineChart.getData().clear();
        for (int i = 0;  i < 6; i++) {
            seriesLine.getData().add(new XYChart.Data<>("COMP3111" + "-" + "quiz" + i, 70));
        }
         */
        lineChart.getData().add(seriesLine);
    }

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

    @FXML
    public void reset() {
        hasFilter = false;
        updateFilter();
        updateChart();
    }

    @FXML
    public void query() {
        hasFilter = true;
        updateFilter();
        updateChart();
    }
}
