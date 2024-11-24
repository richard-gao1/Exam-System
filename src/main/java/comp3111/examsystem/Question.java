package comp3111.examsystem;

import javafx.beans.property.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Question, storing the question content, options, answer, and score
 */
public class Question {
    // Primitive fields for JSON serialization
    private String content; // the content of the question
    private ArrayList<String> options; // the labels of the options of the question
    private int score; // the score for this question
    private int typeChoice; // Single / Multiple Choice
    private int answer; //bitmask A-> 0001, ACD 1101

    /**
     * Constructs a new Question object with the provided details.
     *
     * @param content The content of the question.
     * @param options An ArrayList containing multiple choice options.
     * @param answer A string representing the correct answer(s) to the question.
     * @param score The score assigned to this question.
     * @param typeChoice The type of choice for the question (0 for single choice, 1 for
    multi-choice).
     */

    public Question(String content, ArrayList<String> options, String answer, int score, int typeChoice) {
        if (typeChoice == 0 && answer.length() > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        answer = answer.toUpperCase();
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

    /**
     * Constructs a new instance of the Question class with specific parameters.
     *
     * @param content The content of the question.
     * @param options An array of strings containing multiple choice options.
     * @param answer A string representing the correct answer(s) to the question.
     * @param score The score assigned to this question.
     * @param typeChoice The type of choice for the question (0 for single choice, 1 for
    multi-choice).
     */
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

    /**
     * Constructs a new instance of the Question class with specific parameters.
     *
     * @param content The content of the question.
     * @param options An ArrayList containing multiple choice options.
     * @param answer A string representing the correct answer(s) to the question.
     * @param score The score assigned to this question.
     * @param typeChoice The type of choice for the question ("Single" for single choice, other values for
    multi-choice).
     */
    public Question(String content, ArrayList<String> options, String answer, int score, String typeChoice) {
        this(content,options,answer,score, typeChoice.equals("Single")?0:1);
    }

    /**
     * Constructs a new instance of the Question class with specific parameters.
     *
     * @param content The content of the question.
     * @param options An array of strings containing multiple choice options.
     * @param answer A string representing the correct answer(s) to the question.
     * @param score The score assigned to this question.
     * @param typeChoice The type of choice for the question ("Single" for single choice, other values for
    multi-choice).
     */
    public Question(String content, String[] options, String answer, int score, String typeChoice) {
        this(content,options,answer,score, typeChoice.equals("Single")?0:1);
    }

    /**
     * Retrieves the content of the question.
     *
     * @return The content string of the question.
     */
    public String getContent() {
        return content;
    }

    /**
     * Sets the content of the question.
     *
     * @param content The new content string for the question.
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Retrieves an ArrayList containing the multiple choice options for this question.
     *
     * @return An ArrayList of Strings representing the options.
     */
    public ArrayList<String> getOptions() {
        return options;
    }

    /**
     * Sets the multiple choice options for this question using an ArrayList.
     *
     * @param options An ArrayList of Strings representing the new options.
     */
    public void setOptions(ArrayList<String> options) {
        this.options = options != null ? options : new ArrayList<>();
    }

    /**
     * Sets the multiple choice options for this question using an array of Strings.
     *
     * @param options An array of Strings representing the new options.
     */
    public void setOptions(String[] options) {
        if (options != null) {
            for (int i = 0; i < options.length && i < 4; i++) {
                this.options.set(i, options[i]);
            }
        }
    }

    /**
     * Retrieves the bit representation of the correct answer(s) to this question.
     *
     * @return An integer representing the bit pattern of the correct answers.
     */
    public int getAnswer() {
        return this.answer;
    }

    /**
     * Sets the correct answer(s) for this question using a string input.
     *
     * @param answer A String representing the new correct answer(s).
     */
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

    /**
     * Sets the correct answer(s) for this question using an integer bit pattern.
     *
     * @param answer An integer representing the new correct answers in bit form.
     */
    public void setAnswer(int answer) {
        if (this.typeChoice == 0 && answer > 1) {
            throw new IllegalArgumentException("Single choice question must have exactly one answer");
        }
        this.answer = answer;
    }


    /**
     * Retrieves the score associated with this question.
     *
     * @return The integer score of the question.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the score for this question.
     *
     * @param score The new integer score to be assigned to the question.
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Retrieves the type of choice for this question.
     *
     * @return An integer representing the type of choice (0 for single, 1 for
    multi-choice).
     */
    public int getTypeChoice() {
        return typeChoice;
    }

    /**
     * Sets the type of choice for this question using an integer value.
     *
     * @param typeChoice The integer representing the type of choice (0 for single, 1 for
     *     multi-choice).
     */
    public void setTypeChoice(int typeChoice) {
        this.typeChoice = typeChoice;
    }

    /**
     * Sets the type of choice for this question using a string input.
     *
     * @param typeChoice A String representing the new type choice ("Single" or other values for
    multi-choice).
     */
    public void setTypeChoice(String typeChoice) {
        if ("Single".equalsIgnoreCase(typeChoice)) {
            setTypeChoice(0);
        } else {
            setTypeChoice(1);
        }
    }

    /**
     * Retrieves a StringProperty representation of the content of this question.
     *
     * @return A SimpleStringProperty representing the content of the question.
     */
    public StringProperty contentProperty() {
        return new SimpleStringProperty(this.content);
    }

    /**
     * Retrieves a StringProperty representation of the correct answer(s) for this question.
     *
     * @return A SimpleStringProperty containing the letter(s) corresponding to the set bits in the
    answer integer.
     */
    public StringProperty answerProperty() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4; i++) { // Assuming options A-D (4 bits)
            if ((answer & (1 << i)) != 0) { // Check if bit `i` is set
                result.append((char) ('A' + i)); // Convert bit index to letter
            }
        }
        return new SimpleStringProperty(result.toString());
    }

    /**
     * Retrieves an IntegerProperty representation of the score associated with this question.
     *
     * @return A SimpleIntegerProperty representing the score of the question.
     */
    public IntegerProperty scoreProperty() {
        return new SimpleIntegerProperty(this.score);
    }

    /**
     * Retrieves an IntegerProperty representation of the type of choice for this question.
     *
     * @return A SimpleIntegerProperty representing the type choice of the question.
     */
    public IntegerProperty typeChoiceProperty() {
        return new SimpleIntegerProperty(this.typeChoice);
    }

    /**
     * Compares this question to another object for equality based on its content, options, score,
     type choice, and answer.
     *
     * @param obj The other object to compare with.
     * @return true if this question is equal to the specified object; false otherwise.
     */
    @Override
    public boolean equals(Object obj) { // if questions has same content and same options, they are referred as the same question.
        if (this == obj) return true; // Reference equality
        if (obj == null || getClass() != obj.getClass()) return false; // Null or type mismatch

        Question question = (Question) obj;

        // Custom equality logic
        return content.equals(question.content) &&
                options.containsAll(question.options) &&
                question.options.containsAll(options);
    }

    /**
     * Returns a hash code value for this question based on its content, options, score, type
     choice, and answer.
     *
     * @return An integer hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(content, options);
    }

    /**
     * Provides a string representation of this question including its content, options, answer,
     score, and type choice.
     *
     * @return A String describing the properties of the question.
     */
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
