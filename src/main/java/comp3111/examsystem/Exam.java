package comp3111.examsystem;

import java.util.ArrayList;
import java.util.HashMap;

public class Exam {
    private String examName;
    private String courseID;
    private boolean isPublished;
    private int duration;
    private ArrayList<Question> questions;
    private HashMap<Student, Grade> studentGrades;

    public Exam(String examName, String courseID, boolean isPublished, int duration, ArrayList<Question> questions) {
        this.examName = examName;
        this.courseID = courseID;
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
        return SystemDatabase.getCourse(this.courseID);
    }

    public void setCourse(String courseID) {
        this.courseID = courseID;
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

    public HashMap<Student, Grade> getStudentGrades() {
        return studentGrades;
    }
}