package comp3111.examsystem;

import java.util.ArrayList;

public class Teacher extends User {
    String position;
    public Teacher(String username, String password, String name, String gender, int age, String department, String position) {
        super(username, password, name, gender, age, department);
        this.position = position;
    }
    private ArrayList<Question> questionBank = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();

    public void createExam(String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        createExam(examName, course.getName(), isPublished, duration, questions);
    }

    public void createExam(String examName, String courseName, boolean isPublished, int duration, ArrayList<Question> questions) {
        for (Course course: courses){
            if (course.getName().equals(courseName)){
                Exam exam = new Exam(examName, course, isPublished, duration, questions);
                return;
            }
        }
        throw new IllegalArgumentException("No such course");
    }

    public void addCourse(Course course){
        if (!courses.contains(course)){
            courses.add(course);
            // only call this method via Course class
        }
    }

    public void dropCourse(Course course){
            courses.remove(course);
            // only call this method via Course class
    }

    public void viewExam() {
        for (Course course: courses){
            for (Exam exam: course.getExams()){
                System.out.println(exam.getExamName());
                System.out.println(exam.getCourse());
                System.out.println(exam.getDuration());
                System.out.println(exam.getIsPublished());
                System.out.println("Questions:");
                for (Question question: exam.getQuestions()){
                    System.out.println(question.getQuestion());
                }
            }
        }
    }

    public void deleteExam(String examName, String courseName) {
        for (Course course: courses){
            if (course.getName().equals(courseName)){
                course.dropExam(examName);
                return;
            }
        }
        throw new IllegalArgumentException("No such course");
    }

    public void updateExam(String examName, Exam exam, String courseName) {
        for (Course course: courses){
            if (course.getName().equals(courseName)){
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
