package comp3111.examsystem;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;

public class Question {
    // Primitive fields for JSON serialization
    private String content;
    private ArrayList<String> options;
    private int score;
    private int typeChoice;
    private int answer; //bitmask A-> 0001, ACD 1101

    // Constructor
    public Question(String content, ArrayList<String> options, String answer, int score, int typeChoice) {
        if (typeChoice == 0 && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        this.content = content;
        this.options = options != null ? options : new ArrayList<>();
        this.answer = 0;
        for (char c : answer.toCharArray()) {
            if (c < 'A' || c > 'D') {
                throw new IllegalArgumentException("Answer must be between A and D");
            }
            this.answer |= (1 << (c - 'A')); // Set the bit corresponding to the letter
        }
        this.score = score;
        this.typeChoice = typeChoice;
    }

    public Question(String content, String[] options, String answer, int score, int typeChoice) {
        if (typeChoice == 0 && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        this.content = content;
        this.options = new ArrayList<>(List.of("", "", "", "")); // Default 4 options
        if (options != null) {
            for (int i = 0; i < options.length && i < 4; i++) {
                this.options.set(i, options[i]);
            }
        }
        this.answer = 0;
        for (char c : answer.toCharArray()) {
            if (c < 'A' || c > 'D') {
                throw new IllegalArgumentException("Answer must be between A and D");
            }
            this.answer |= (1 << (c - 'A')); // Set the bit corresponding to the letter
        }
        this.score = score;
        this.typeChoice = typeChoice;
    }

    public Question(String content, ArrayList<String> options, String answer, int score, String typeChoice) {
        this(content,options,answer,score, typeChoice.equals("Single")?0:1);
    }

    public Question(String content, String[] options, String answer, int score, String typeChoice) {
        this(content,options,answer,score, typeChoice.equals("Single")?0:1);
    }

    // Getters and Setters for primitive fields
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options != null ? options : new ArrayList<>();
    }

    public void setOptions(String[] options) {
        if (options != null) {
            for (int i = 0; i < options.length && i < 4; i++) {
                this.options.set(i, options[i]);
            }
        }
    }

    public int getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        if (this.typeChoice == 0 && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        answer = answer.toUpperCase();
        this.answer = 0;
        for (char c : answer.toCharArray()) {
            if (c < 'A' || c > 'D') {
                throw new IllegalArgumentException("Answer must be between A and D");
            }
            this.answer |= (1 << (c - 'A')); // Set the bit corresponding to the letter
        }
    }
    public void setAnswer(int answer) {
        if (this.typeChoice == 0 && answer > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        this.answer = answer;
    }


    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTypeChoice() {
        return typeChoice;
    }

    public void setTypeChoice(int typeChoice) {
        this.typeChoice = typeChoice;
    }

    public void setTypeChoice(String typeChoice) {
        if ("Single".equalsIgnoreCase(typeChoice)) {
            setTypeChoice(0);
        } else {
            setTypeChoice(1);
        }
    }

    // JavaFX Property Accessors
    public StringProperty contentProperty() {
        return new SimpleStringProperty(this.content);
    }

    public StringProperty answerProperty() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) { // Assuming options A-D (4 bits)
            if ((answer & (1 << i)) != 0) { // Check if bit `i` is set
                result.append((char) ('A' + i)); // Convert bit index to letter
            }
        }
        return new SimpleStringProperty(result.toString());
    }

    public IntegerProperty scoreProperty() {
        return new SimpleIntegerProperty(this.score);
    }

    public IntegerProperty typeChoiceProperty() {
        return new SimpleIntegerProperty(this.typeChoice);
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