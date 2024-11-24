package comp3111.examsystem.controller;

import comp3111.examsystem.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

/**
 * This class is the controller for the quiz page.
 * It provides methods for initializing the controller, setting the exam, and updating the question list.
 */
public class QuizController implements Initializable {

    @FXML
    private RadioButton answerFour;

    @FXML
    private RadioButton answerOne;

    @FXML
    private RadioButton answerThree;

    @FXML
    private RadioButton answerTwo;

    @FXML
    private Label questionTxt;

    @FXML
    private ListView<Question> questionList;

    @FXML
    private VBox mcqBox;

    @FXML
    private VBox sataBox;

    @FXML
    private CheckBox sataFour;

    @FXML
    private CheckBox sataOne;

    @FXML
    private CheckBox sataThree;

    @FXML
    private CheckBox sataTwo;

    @FXML
    private Text timer;

    @FXML
    private Button submitButton;

    Exam exam;
    RadioButton[] answerChoiceButtons;
    CheckBox[] sataChoiceButtons;
    ArrayList<Integer> answerChoices;
    ToggleGroup answerGroup = new ToggleGroup();
    Question currentQuestion;
    HashMap<RadioButton, Integer> buttonMap = new HashMap<>();
    HashMap<Integer, RadioButton> revButtonMap = new HashMap<>();
    HashMap<CheckBox, Integer> sataMap = new HashMap<>();
    HashMap<Question, Integer> questionMap = new HashMap<>();
    Timeline timeline;

    Time time = new Time(0, 0, 0);

    /**
     * Initializes the controller.
     *
     * @param location the location of the FXML file
     * @param resources the resources used by the FXML file
     */
    public void initialize(URL location, ResourceBundle resources) {
        answerChoiceButtons = new RadioButton[]{this.answerOne, this.answerTwo, this.answerThree, this.answerFour};
        sataChoiceButtons = new CheckBox[]{this.sataOne, this.sataTwo, this.sataThree, this.sataFour};
        for(RadioButton button : answerChoiceButtons){
            button.setToggleGroup(answerGroup);
        }
        for (int i = 0; i < 4; i++) {
            this.buttonMap.put(answerChoiceButtons[i], i + 1);
            this.revButtonMap.put(i + 1, answerChoiceButtons[i]);
            this.sataMap.put(sataChoiceButtons[i], i + 1);
        }

        // timer set up
        timeline = new Timeline(
            new KeyFrame(Duration.seconds(1),
                e -> {
                    time.oneSecondPassed();
                    timer.setText(time.getCurrentTime());
                    if (time.getTotalTime() >= exam.getDuration()) {
                        submitButton.fire();
//                        submit(e);
                    }
                }));
        timer.setText(time.getCurrentTime());
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Sets the exam and initializes various data structures based on the questions in the exam.
     *
     * @param exam the exam to set
     * @throws NullPointerException if the exam is null
     */
    public void setExam(Exam exam) {
        this.exam = exam;
        ArrayList<Question> questions = exam.getQuestions();
        this.answerChoices = new ArrayList<>();
        for(int i = 0; i < questions.size(); i++) {
            this.questionMap.put(questions.get(i), i);
            this.answerChoices.add(0);
        }
        this.questionList.setCellFactory(param -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getContent() == null) {
                    setText(null);
                } else {
                    setText(item.getContent());
                }
            }
        });
        this.questionList.setItems(FXCollections.observableArrayList(questions));
//        for (Question question : exam.getQuestions()) {
//
//        }
        Question q1 = exam.getQuestions().getFirst();
        setQuestion(q1);

        this.questionList.setOnMouseClicked(new ListViewHandler(){
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (currentQuestion != null){
                    if (currentQuestion.getTypeChoice() == 0 && answerGroup.getSelectedToggle() != null) {
                        answerChoices.set(questionMap.get(currentQuestion), 1 << (buttonMap.get(answerGroup.getSelectedToggle()) - 1));
                        System.out.println(answerChoices.toString());
                    } else if (currentQuestion.getTypeChoice() == 1) {
                        int answer = 0;
                        for (CheckBox sata: sataChoiceButtons) {
                            if (sata.isSelected()) {
                                answer += 1 << (sataMap.get(sata) - 1);
                            }
                        }
                        answerChoices.set(questionMap.get(currentQuestion), answer);
                        System.out.println(answerChoices.toString());
                    }
                }
                setQuestion(questionList.getSelectionModel().getSelectedItem());
            }
        });
    }

    /**
     * Sets the current question and updates the UI based on the type of the question.
     *
     * @param question the question to set as the current question
     * @throws NullPointerException if the question is null
     */
    public void setQuestion(Question question) {
        this.currentQuestion = question;
        if (this.currentQuestion.getTypeChoice() == 0){
            System.out.println("0 question type");
            this.mcqBox.setManaged(true);
            this.mcqBox.setVisible(true);
            this.sataBox.setManaged(false);
            this.sataBox.setVisible(false);
            // set the selected to the previously saved answer
            int answer = this.answerChoices.get(this.questionMap.get(this.currentQuestion));
            if (answer == 8) {
                answer = 4;
            } else if (answer == 4) {
                answer = 3;
            }
            this.answerGroup.selectToggle(this.revButtonMap.get(answer));
            List<String> choices = question.getOptions();
            questionTxt.setText(question.getContent());
            for (int i = 0; i < 4; i++) {
                answerChoiceButtons[i].setText(choices.get(i));
            }
        } else {
            System.out.println("1 question type");
            this.sataBox.setManaged(true);
            this.sataBox.setVisible(true);
            this.mcqBox.setManaged(false);
            this.mcqBox.setVisible(false);
            // save the selected to be the previously saved answers
            for (CheckBox sata: sataChoiceButtons) {
                sata.setSelected(false);
            }
            int answer = this.answerChoices.get(this.questionMap.get(this.currentQuestion));
            for (int i = 3; i >= 0; i--) {
                if (answer - (1 << i) >= 0) {
//                    System.out.println(answer + " - " + (1 << i));
                    this.sataChoiceButtons[i].setSelected(true);
                    answer -= (1 << i);
                }
            }
            List<String> choices = question.getOptions();
            questionTxt.setText(question.getContent());
            for (int i = 0; i < 4; i++) {
                sataChoiceButtons[i].setText(choices.get(i));
            }
        }
    }

    /**
     * Saves the answers to the current question (if applicable), calculates the score of the exam, and displays an alert with the score.
     *
     * @param e the action event that triggered the submission of the exam
     * @throws NullPointerException if the current question is null
     */
    public void submit(ActionEvent e) {  // ActionEvent e
        // in case last question's answer choice hasn't been saved
        if (currentQuestion != null){
            if (currentQuestion.getTypeChoice() == 0 && answerGroup.getSelectedToggle() != null) {
                answerChoices.set(questionMap.get(currentQuestion), 1 << (buttonMap.get(answerGroup.getSelectedToggle()) - 1));
                System.out.println(answerChoices.toString());
            } else if (currentQuestion.getTypeChoice() == 1) {
                int answer = 0;
                for (CheckBox sata: sataChoiceButtons) {
                    if (sata.isSelected()) {
                        answer += 1 << (sataMap.get(sata) - 1);
                    }
                }
                answerChoices.set(questionMap.get(currentQuestion), answer);
                System.out.println(answerChoices.toString());
            }
        }

        int score = this.exam.grade(this.answerChoices);
        this.exam.gradeStudent((Student) SystemDatabase.currentUser, score, time.getTotalTime());

        this.timeline.stop();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You scored: " + score + "/" + this.exam.getFullScore(), ButtonType.OK);
        alert.setTitle("Grade");
        alert.show();
        ((Stage) ((Button) e.getSource()).getScene().getWindow()).close();
    }
}
