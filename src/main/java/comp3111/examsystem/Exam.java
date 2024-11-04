package comp3111.examsystem;

import java.util.ArrayList;
import java.util.HashMap;

public class Exam {

    private String examName;
    private Course course;
    private boolean isPublished;
    private int duration;
    private ArrayList<Question> questions;
    private HashMap<Student, Integer> studentToGrades = new HashMap<>();

    public Exam(String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        this.examName = examName;
        this.course = course;
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
        this.course = course;
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

    public Integer grade(ArrayList<Integer> answers){
        // TODO: implement
        return 0;
    }
}
