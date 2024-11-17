package comp3111.examsystem;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.List;

public class Question {
    public enum TypeChoice {
        SINGLE, MULTIPLE
    }

    private final StringProperty content;
    private final ObjectProperty<List<String>> options;
    private final StringProperty answer;
    private final IntegerProperty score;
    private final IntegerProperty typeChoice;

    // Constructor
    public Question(String content, List<String> options, String answer, int score, int typeChoice) {
        if (typeChoice == 0 && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        this.content = new SimpleStringProperty(content);
        this.options = new SimpleObjectProperty<>(options != null ? options : new ArrayList<>());
        this.answer = new SimpleStringProperty(answer);
        this.score = new SimpleIntegerProperty(score);
        this.typeChoice = new SimpleIntegerProperty(typeChoice);

        // Validation for single choice questions

    }

    // Default Constructor
    public Question() {
        this("", new ArrayList<>(), "", 0, 0);
        // Don't use!!!
    }

    public Question(String content, String[] options, String answer, int score, int typeChoice) {
        // Validation for single choice questions
        if (typeChoice == 0 && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        this.content = new SimpleStringProperty(content);
        this.options = new SimpleObjectProperty<>(new ArrayList<>(List.of("", "", "", ""))); // Default 4 options
        if (options != null) {
            for (int i = 0; i < options.length && i < 4; i++) {
                this.options.get().set(i, options[i]);
            }
        }
        this.answer = new SimpleStringProperty(answer);
        this.score = new SimpleIntegerProperty(score);
        this.typeChoice = new SimpleIntegerProperty(typeChoice);
    }


    // Getters and Setters for 'content'
    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
    }

    public StringProperty contentProperty() {
        return content;
    }

    // Getters and Setters for 'options'
    public List<String> getOptions() {
        return options.get();
    }

    public void setOptions(List<String> options) {
        this.options.set(options != null ? options : new ArrayList<>());
    }

    public void setOptions(String[] options){
        if (options != null) {
            for (int i = 0; i < options.length && i < 4; i++) {
                this.options.get().set(i, options[i]);
            }
        }
    }

    public ObjectProperty<List<String>> optionsProperty() {
        return options;
    }

    // Getters and Setters for 'answer'
    public String getAnswer() {
        return answer.get();
    }

    public void setAnswer(String answer) {
        if (this.typeChoice.get() == 0 && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        this.answer.set(answer);
    }

    public StringProperty answerProperty() {
        return answer;
    }

    // Getters and Setters for 'score'
    public int getScore() {
        return score.get();
    }

    public void setScore(int score) {
        this.score.set(score);
    }

    public IntegerProperty scoreProperty() {
        return score;
    }

    // Getters and Setters for 'typeChoice'
    public int getTypeChoice() {
        return typeChoice.get();
    }

    public void setTypeChoice(int typeChoice) {
        this.typeChoice.set(typeChoice);
    }

    public void setTypeChoice(String typeChoice) {
        if (typeChoice.equals("Single")){
            this.typeChoice.set(0);
        }
        else{
            this.typeChoice.set(1);
        };
    }

    public IntegerProperty typeChoiceProperty() {
        return typeChoice;
    }

    @Override
    public String toString() {
        return "Question{" +
                "content='" + getContent() + '\'' +
                ", options=" + getOptions() +
                ", answer='" + getAnswer() + '\'' +
                ", score=" + getScore() +
                ", typeChoice=" + getTypeChoice() +
                '}';
    }
}
