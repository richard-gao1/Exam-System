package comp3111.examsystem.controller;

import comp3111.examsystem.Exam;
import comp3111.examsystem.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;

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

    Exam exam;
    RadioButton[] answerChoiceButtons;
    ArrayList<Integer> answerChoices;
    ToggleGroup answerGroup = new ToggleGroup();
    Question currentQuestion;
    HashMap<RadioButton, Integer> buttonMap = new HashMap<>();
    HashMap<Question, Integer> questionMap = new HashMap<>();

    public void initialize(URL location, ResourceBundle resources) {
        answerChoiceButtons = new RadioButton[]{this.answerOne, this.answerTwo, this.answerThree, this.answerFour};
        this.answerOne.setToggleGroup(answerGroup);
        this.answerTwo.setToggleGroup(answerGroup);
        this.answerThree.setToggleGroup(answerGroup);
        this.answerFour.setToggleGroup(answerGroup);
        this.buttonMap.put(answerOne, 1);
        this.buttonMap.put(answerTwo, 2);
        this.buttonMap.put(answerThree, 3);
        this.buttonMap.put(answerFour, 4);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        ArrayList<Question> questions = exam.getQuestions();
        this.answerChoices = new ArrayList<>();
        for(int i = 0; i < questions.size(); i++) {
            this.questionMap.put(questions.get(i), i);
            this.answerChoices.add(0);
        }
        ObservableList<Question> questionsList = FXCollections.observableArrayList(questions);
        this.questionList.setCellFactory(param -> new ListCell<Question>() {
            @Override
            protected void updateItem(Question item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null || item.getQuestion() == null) {
                    setText(null);
                } else {
                    setText(item.getQuestion());
                }
            }
        });
        this.questionList.setItems(questionsList);
//        for (Question question : exam.getQuestions()) {
//
//        }
        Question q1 = exam.getQuestions().get(0);
        setQuestion(q1);

        this.questionList.setOnMouseClicked(new ListViewHandler(){
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                if (currentQuestion != null && answerGroup.getSelectedToggle() != null){
                    answerChoices.set(questionMap.get(currentQuestion), buttonMap.get(answerGroup.getSelectedToggle()));
                    System.out.println(answerChoices.toString());
                }
                setQuestion(questionList.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void setQuestion(Question question) {
        this.currentQuestion = question;
        this.answerGroup.selectToggle(null);
        String[] choices = question.getOptions();
        questionTxt.setText(question.getQuestion());
        for (int i = 0; i < 4; i++) {
            answerChoiceButtons[i].setText(choices[i]);
        }
    }

    public void submit() {  // ActionEvent e
        // in case last question's answer choice hasn't been saved
        if (currentQuestion != null && answerGroup.getSelectedToggle() != null){
            answerChoices.set(questionMap.get(currentQuestion), buttonMap.get(answerGroup.getSelectedToggle()));
            System.out.println(answerChoices.toString());
        }
        System.out.println("Submit pressed");
        // TODO: add grade popup and put grade into database
        this.exam.grade(this.answerChoices);
    }
}
