package comp3111.examsystem.controller;

import comp3111.examsystem.Exam;
import comp3111.examsystem.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.*;

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

    public void initialize(URL location, ResourceBundle resources) {
        answerChoiceButtons = new RadioButton[]{this.answerOne, this.answerTwo, this.answerThree, this.answerFour};
        sataChoiceButtons = new CheckBox[]{this.sataOne, this.sataTwo, this.sataThree, this.sataFour};
//        this.answerOne.setToggleGroup(answerGroup);
//        this.answerTwo.setToggleGroup(answerGroup);
//        this.answerThree.setToggleGroup(answerGroup);
//        this.answerFour.setToggleGroup(answerGroup);
        for(RadioButton button : answerChoiceButtons){
            button.setToggleGroup(answerGroup);
        }
//        this.buttonMap.put(answerOne, 1);
//        this.buttonMap.put(answerTwo, 2);
//        this.buttonMap.put(answerThree, 3);
//        this.buttonMap.put(answerFour, 4);
//        this.sataMap.put(this.sataOne, 1);
//        this.sataMap.put(this.sataTwo, 2);
//        this.sataMap.put(this.sataThree, 3);
//        this.sataMap.put(this.sataFour, 4);
        for (int i = 0; i < 4; i++) {
            this.buttonMap.put(answerChoiceButtons[i], i + 1);
            this.revButtonMap.put(i + 1, answerChoiceButtons[i]);
            this.sataMap.put(sataChoiceButtons[i], i + 1);
        }
    }

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

    public void submit() {  // ActionEvent e
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
        System.out.println("Submit pressed");
        // TODO: add grade popup and put grade into database
        System.out.println(this.exam.grade(this.answerChoices));
    }
}
