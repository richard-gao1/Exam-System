package comp3111.examsystem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Teacher extends User {
    private String position;
    public Teacher(String username, String password, String name, String gender, int age, String department, String position) {
        super(username, password, name, gender, age, department);
        this.position = position;
    }
    private ArrayList<Question> questionBank;
    private ArrayList<String> courseIDs;
    private HashMap<String, Double> course_scores;
    private HashMap<String, Double> student_scores;
    private HashMap<String, Double> exam_scores;

    public void createExam(String examName, String courseID, boolean isPublished, int duration, ArrayList<Question> questions) {
        Exam exam = new Exam(examName, courseID, isPublished, duration, questions);
        //SystemDatabase.addExam(exam);
    }

    public Teacher update(String username, String password, String name, String gender, int age, String department, String position) {
        super.update(username, password, name, gender, age, department);
        this.position = position;
        return this;
    }

    public void addCourse(String courseID){
        courseIDs.add(courseID);
    }

    public void viewExam() {
        // TODO
    }

    public void deleteExam(String examName) {
        // TODO
    }

    public void updateExam(String examName, Exam exam) {
        // TODO
    }

    public void viewStudent() {
        // TODO: implement
    }

    public void viewStudentAnswer() {
        // TODO: implement
    }

    public void gradeStudentAnswer() {
        // TODO: implement
    }

    public void viewQuestion() {
        for (Question question : questionBank) {
            System.out.println(question.getQuestion());
        }
    }

    public void createQuestion(String content, String[] options, String answer, int score, int type) {
        Question question = new Question(content, options, answer, score, type);
        questionBank.add(question);
    }

    public void deleteQuestion() {
        // TODO: implement
    }

    public void updateQuestion() {
        // TODO: implement
    }

    public void viewQuestionBank() {
        // TODO: implement
    }

    public List<Exam> getExams() {
        List<Exam> exams = new ArrayList<>();
        for (String courseID : this.courseIDs) {
            Course course = SystemDatabase.getCourse(courseID);
            if (course != null){
                exams.addAll(course.getExams());
            }
        }
        return exams;
    }

    public String getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && (Objects.equals(this.position, ((Teacher) other).position));
    }

    public List<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseID : this.courseIDs) {
            Course course = SystemDatabase.getCourse(courseID);
            if (course != null){
                courses.add(course);
            }
        }
        return courses;
    }
}
