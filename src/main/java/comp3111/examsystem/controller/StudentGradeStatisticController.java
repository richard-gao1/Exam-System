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

/**
 * The StudentGradeStatisticController class is responsible for managing and displaying
 * student grade statistics of the exams.
 */
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
    private ChoiceBox<?> courseCombox;

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

    /**
     * Initializes the UI components and loads student grade data.
     *
     * @param url            The URL of the FXML file.
     * @param resourceBundle The resource bundle containing locale-specific objects.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.student = (Student) SystemDatabase.currentUser;
        barChart.setLegendVisible(false);
        categoryAxisBar.setLabel("Exam");
        numberAxisBar.setLabel("Score");

//        gradeList.add(new Grade(
//                "student",
//                "comp3111",
//                "final",
//                100,
//                100,
//                60
//        ));
        gradeTable.setItems(gradeList);
//        studentColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseNum"));
        examColumn.setCellValueFactory(new PropertyValueFactory<>("examName"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        fullScoreColumn.setCellValueFactory(new PropertyValueFactory<>("fullScore"));
        timeSpendColumn.setCellValueFactory(new PropertyValueFactory<>("timeSpend"));

        refresh();
        loadChart();
    }

    /**
     * Refreshes the UI components and data displayed in the student grade statistic view.
     */
    @FXML
    public void refresh() {
    }

    /**
     * Loads the chart data and updates the bar chart with student grades.
     */
    private void loadChart() {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();

        seriesBar.getData().clear();
        barChart.getData().clear();
        barChart.getData().add(seriesBar);

    }

    /**
     * Loads the available choices for courses and exams into their respective lists.
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
    }

    /**
     * Loads the student grades data and updates the grade list accordingly.
     */
    private void loadData() {
        gradeList.clear();
        /*
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
         */
        ArrayList<Exam> exams = new ArrayList<>();
        if (student != null) exams = student.getExams();
        System.out.println(exams);
        for (Exam exam : exams) {
            gradeList.add(exam.getStudentGrades().get(student));
        }
        System.out.println(gradeList);

        updateHashMaps(false);
    }

    /**
     * Updates the hash maps with student grades data, either filtered or from the full list.
     *
     * @param filtered Indicates whether to use the filtered grade list or the full grade
    list.
     */
    private void updateHashMaps(boolean filtered) {
        by_course.clear();
        by_exam.clear();

        if (filtered) {
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

    /**
     * Updates the filtered list of grades based on selected course filter.
     */
    private void updateFilter() {
        displayGradeList.clear();
        if (!hasFilter) {
            displayGradeList.addAll(gradeList);
            return;
        }
        courseFilter = removeNull((String)courseCombox.getSelectionModel().getSelectedItem());

        ArrayList<Grade> filteredGradeList = gradeList.stream().filter(
                g -> ((courseFilter.isEmpty() || Objects.equals(courseFilter, g.getCourseNum())))
        ).collect(Collectors.toCollection(ArrayList::new));

        displayGradeList.addAll(filteredGradeList);
        updateHashMaps(true);
    }

    /**
     * Updates the bar chart with data from the `by_course` hash map.
     */
    private void updateChart() {
        XYChart.Series<String, Number> seriesBar = new XYChart.Series<>();

        seriesBar.getData().clear();

        by_course.forEach((course, score) -> {
            seriesBar.getData().add(new XYChart.Data<>(course, score));
        });
    }

    /**
     * Handles the query action, and displays the filtered results.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void query(ActionEvent event) {

    }

    /**
     * Refreshes the data and UI components based on the current state of the student grades
     statistic view.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void refresh(ActionEvent event) {
        loadData();
        loadChoices();
        updateFilter();
        updateChart();
    }

    /**
     * Resets the UI components and data to their initial state.
     *
     * @param event The action event that triggered this method.
     */
    @FXML
    void reset(ActionEvent event) {

    }

}
