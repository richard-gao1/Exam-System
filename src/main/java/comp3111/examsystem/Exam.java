package comp3111.examsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Exam {
    private String examName;
    private Course course;
    private boolean isPublished;
    private int duration;
    private ArrayList<Question> questions = new ArrayList<>();
    private HashMap<Student, Integer> studentToGrades = new HashMap<>();

    public Exam(String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        this.examName = examName;
        if (course != null){
            this.course = course;
            course.addExam(this);
        }
        this.isPublished = isPublished;
        this.duration = duration;
        this.questions = questions;
    }

    public String getExamName() {
        return this.examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        if (this.course != null){
            this.course.dropExam(this);
        }
        if (course != null){
            this.course = course;
            course.addExam(this);
        }
    }

    public boolean getIsPublished() {
        return this.isPublished;
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<Question> getQuestions() {
        return this.questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void addQuestion(Question question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        if (this.questions.contains(question)) {
            throw new IllegalArgumentException("Question already exists in the exam");
        }
        this.questions.add(question);
    }

    public void removeQuestion(Question question) {
        if (!this.questions.contains(question)) {
            throw new IllegalArgumentException("Question does not exist in the exam");
        }
        this.questions.remove(question);
    }

    public String parseAnswer(String answer){
        char[] chars = answer.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public Integer grade(ArrayList<String> answers){
        int score = 0;
        for (int i = 0; i < this.questions.size(); i++){
            Question question = this.questions.get(i);
            if (question.getTypeChoice() == 0){
                if (question.getAnswer().equals(parseAnswer(answers.get(i)))){
                    score += question.getScore();
                }
            } else {
                // for each wrong answer, score -= question.getScore() / number of correct answers
                // if yes, score += question.getScore()
                int correct = 0;
                for (int j = 0; j < question.getAnswer().length(); j++){
                    if (answers.get(i).indexOf(question.getAnswer().charAt(j)) != -1){
                        correct++;
                    }
                }
                score += Math.max(0, question.getScore()*(correct/question.getAnswer().trim().length() - (answers.get(i).trim().length() - correct)/question.getAnswer().trim().length()));
            }
        }
        return score;
    }

    public void gradeStudent(){
        // TODO: Implement
    }
}