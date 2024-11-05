package comp3111.examsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Teacher extends User {
    String position;
    public Teacher(String username, String password, String name, String gender, int age, String department, String position) {
        super(username, password, name, gender, age, department);
        this.position = position;
    }
    private ArrayList<Question> questionBank;
    private ArrayList<Course> courses;
    private HashMap<String, Double> course_scores;
    private HashMap<String, Double> student_scores;
    private HashMap<String, Double> exam_scores;
    // private List<Grade> grades;

    public void createExam(String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        Exam exam = new Exam(examName, course, isPublished, duration, questions);
        //SystemDatabase.addExam(exam);
    }

    public void addCourse(Course course){
        courses.add(course);
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

    /* public List<Grade> getGradeList() {

    }
     */

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && (Objects.equals(this.position, ((Teacher) other).position));
    }
}
