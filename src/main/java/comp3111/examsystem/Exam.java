package comp3111.examsystem;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Exam {
    private StringProperty examName = new SimpleStringProperty();
    private BooleanProperty isPublished = new SimpleBooleanProperty();
    private IntegerProperty duration = new SimpleIntegerProperty();
    private ObservableList<Question> questions = FXCollections.observableArrayList();

    // Keep course and studentToGrades as standard fields
    private Course course;
    private HashMap<Student, Integer> studentToGrades = new HashMap<>();

    // Constructors
    public Exam(String examName, Course course, boolean isPublished, int duration) {
        this.examName.set(examName);
        if (course != null) {
            this.course = course;
            course.addExam(this);
        }
        this.isPublished.set(isPublished);
        this.duration.set(duration);
    }

    public Exam(String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        this(examName, course, isPublished, duration);
        if (questions != null) {
            this.questions.addAll(questions);
        }
    }

    public Exam(String examName, String courseID, boolean isPublished, int duration, ArrayList<Question> questions) {
        this(examName, SystemDatabase.getCourse(courseID), isPublished, duration);
        if (questions != null) {
            this.questions.addAll(questions);
        }
    }

    public Exam(String examName, String courseID, boolean isPublished, int duration) {
        this(examName, SystemDatabase.getCourse(courseID), isPublished, duration);
    }

    // Property Getters
    public StringProperty examNameProperty() {
        return examName;
    }

    public BooleanProperty isPublishedProperty() {
        return isPublished;
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public ObservableList<Question> getQuestions() {
        return questions;
    }

    // Standard Getters and Setters for non-property fields
    public String getExamName() {
        return examName.get();
    }

    public void setExamName(String examName) {
        this.examName.set(examName);
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        if (course != null){
            if (this.course != null){
                // remove exam from the original course if needed
                this.course.dropExam(this);
            }
            this.course = course;
            course.addExam(this);
        }
    }

    public boolean getIsPublished() {
        return isPublished.get();
    }

    public void setIsPublished(boolean isPublished) {
        this.isPublished.set(isPublished);
    }

    public int getDuration() {
        return duration.get();
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public StringProperty courseIDProperty(){
        return new SimpleStringProperty(course.getCourseID());
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions.addAll(questions);
    }

    public void setStudentGrades(HashMap<Student, Integer> studentToGrades) {
        this.studentToGrades = studentToGrades;
    }

    // Add Questions
    public void addQuestion(Question question) {
        if (this.questions == null) {
            this.questions = FXCollections.observableArrayList();
        }
        if (!questions.contains(question)) {
            questions.add(question);
        } else {
            throw new IllegalArgumentException("Question already exists in the exam");
        }
    }

    public void removeQuestion(Question question) {
        if (!this.questions.contains(question)) {
            throw new IllegalArgumentException("Question does not exist in the exam");
        }
        this.questions.remove(question);
    }

    public HashMap<Student, Integer> getStudentGrades() {
        return studentToGrades;
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