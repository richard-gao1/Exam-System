package comp3111.examsystem;

import java.util.ArrayList;

public class Teacher extends User {
    public Teacher(String username, String password, String name, String gender, int age, String department) {
        super(username, password, name, gender, age, department);
    }
    private ArrayList<Question> questionBank;
    private ArrayList<Course> courses;

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


}
