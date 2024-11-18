package comp3111.examsystem;

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
    private ArrayList<Question> questionBank = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private HashMap<String, Double> course_scores;
    private HashMap<String, Double> student_scores;
    private HashMap<String, Double> exam_scores;

    public void createExam(String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        createExam(examName, course.getCourseName(), isPublished, duration, questions);
    }

    public void createExam(String examName, String courseName, boolean isPublished, int duration, ArrayList<Question> questions) {
        for (Course course: courses){
            if (course.getCourseName().equals(courseName)){
                Exam exam = new Exam(examName, course, isPublished, duration, questions);
                return;
            }
        }
        throw new IllegalArgumentException("No such course");
    }

    public Teacher update(String username, String password, String name, String gender, int age, String department, String position) {
        super.update(username, password, name, gender, age, department);
        this.position = position;
        return this;
    }

    public void addCourse(Course course){
        if (!courses.contains(course)) courses.add(course);
    }

    public void addCourse(String courseID){
        Course c = SystemDatabase.getCourse(courseID);
        if (c == null) return;
        if (!courses.contains(c)){
            courses.add(c);// only call this method via Course class
        }
    }

    public void dropCourse(Course course){
        dropCourse(course.getCourseID());
            // only call this method via Course class
    }

    public void dropCourse(String courseID){
        courses.remove(courseID);
        // only call this method via Course class
    }

    public void addExam(Exam exam, String courseName){
        for (Course course: courses){
            if (course.getCourseName().equals(courseName)){
                course.addExam(exam);
                return;
            }
        }
        throw new IllegalArgumentException("No such course");
    }

    public void deleteExam(String examName, String courseName) {
        for (Course course: courses){
            if (course.getCourseName().equals(courseName)){
                course.dropExam(examName);
                return;
            }
        }
        throw new IllegalArgumentException("No such course");
    }

    public void updateExam(String examName, Exam exam, String courseName) {
        for (Course course: courses){
            if (course.getCourseName().equals(courseName)){
                course.updateExam(examName, exam);
                return;
            }
        }
        throw new IllegalArgumentException("No such course");
    }

    public void updateExam(String examName, Exam exam, Course course) {
        if (course.getTeacher().equals(this)){
            course.updateExam(examName, exam);
        }
        else{
            throw new IllegalArgumentException("Not allowed to access this course");
        }
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
            System.out.println(question.getContent());
        }
    }

    public void createQuestion(String content, String[] options, String answer, int score, int type) {
        Question question = new Question(content, options, answer, score, type);
        questionBank.add(question);
    }

    public void createQuestion(Question question){
        questionBank.add(question);
    }

    public void deleteQuestion(Question question) {
        questionBank.remove(question);
    }

    public void updateQuestion() {
        // TODO: implement
    }

    public ArrayList<Question> getQuestionBank() {
        return questionBank;
    }

    public void viewQuestionBank() {
        for (Question question : questionBank){
            System.out.println(question.getContent());
        }
    }

    public List<Exam> getExams() {
        List<Exam> exams = new ArrayList<>();
        for (Course course : courses) {
            exams.addAll(course.getExams());
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
        return courses;
    }

    public List<String> getCourseID(){
        ArrayList<String> a = new ArrayList<>();
        for (Course c: courses) {
            a.add(c.getCourseID());
        }
        return a;
    }
}
