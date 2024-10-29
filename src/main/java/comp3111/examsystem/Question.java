package comp3111.examsystem;

import java.util.ArrayList;

public class Question {
    private ArrayList<String> answerChoices;
    private String question;
    private int points;

    public Question(String question, ArrayList<String> answerChoices, int points) {
        this.question = question;
        this.points = points;
        this.answerChoices = answerChoices;
    }
}
