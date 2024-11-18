package comp3111.examsystem.controller;

import comp3111.examsystem.Exam;
import comp3111.examsystem.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
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
    int[] answerChoices;
    ToggleGroup answerGroup = new ToggleGroup();

    public void initialize(URL location, ResourceBundle resources) {
        answerChoiceButtons = new RadioButton[]{this.answerOne, this.answerTwo, this.answerThree, this.answerFour};
        this.answerOne.setToggleGroup(answerGroup);
        this.answerTwo.setToggleGroup(answerGroup);
        this.answerThree.setToggleGroup(answerGroup);
        this.answerFour.setToggleGroup(answerGroup);
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        this.answerChoices = new int[exam.getQuestions().size()];
        ObservableList<Question> questions = FXCollections.observableArrayList(exam.getQuestions());
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
        this.questionList.setItems(questions);
//        for (Question question : exam.getQuestions()) {
//
//        }
        Question q1 = exam.getQuestions().get(0);
        setQuestion(q1);

        this.questionList.setOnMouseClicked(new ListViewHandler(){
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                setQuestion(questionList.getSelectionModel().getSelectedItem());
            }
        });
    }

    public void setQuestion(Question question) {
        this.answerGroup.selectToggle(null);
        String[] choices = question.getOptions();
        questionTxt.setText(question.getQuestion());
        for (int i = 0; i < 4; i++) {
            answerChoiceButtons[i].setText(choices[i]);
        }
    }

    public void submit() {  // ActionEvent e
        System.out.println("Submit pressed");
    }
}
