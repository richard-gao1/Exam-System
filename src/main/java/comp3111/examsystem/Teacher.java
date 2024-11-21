package comp3111.examsystem;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * A class representing a teacher, which extends the User class.
 */
public class Teacher extends User {
    private String position;

    // Changes: Replace `courses` with `courseIDs`
    private ArrayList<String> courseIDs = new ArrayList<>(); // Store course IDs instead of Course objects

    private ArrayList<Question> questionBank = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private HashMap<String, Double> course_scores;
    private HashMap<String, Double> student_scores;
    private HashMap<String, Double> exam_scores;
    // private List<Grade> grades;

    /**
     * Constructs a new Teacher account with the provided details.
     *
     * @param username   The new username to set.
     * @param password   The new password to set.
     * @param name       The new name to set.
     * @param gender     The new gender to set.
     * @param age        The new age to set.
     * @param department The new department to set.
     * @param position   The new position to set.
     * @return This Teacher instance after updating the details.
     */
    public Teacher(String username, String password, String name, String gender, int age, String department, String position) {
        super(username, password, name, gender, age, department);
        this.position = position;
    }

    /**
     * Updates the details of a Teacher account.
     *
     * @param username   The new username to set.
     * @param password   The new password to set.
     * @param name       The new name to set.
     * @param gender     The new gender to set.
     * @param age        The new age to set.
     * @param department The new department to set.
     * @param position   The new position to set.
     * @return This Teacher instance after updating the details.
     */
    public Teacher update(String username, String password, String name, String gender, int age, String department, String position) {
        super.update(username, password, name, gender, age, department);
        this.position = position;
        return this;
    }

    public void addCourse(String courseID) {
        if (!courseIDs.contains(courseID)) {
            courseIDs.add(courseID);
            SystemDatabase.updateTeacher(this);
        }
    }

    public void dropCourse(String courseID) {
        courseIDs.remove(courseID);
        SystemDatabase.updateTeacher(this);
    }

    public List<Course> getCourses() {
        // Changes: Retrieve Course objects dynamically using SystemDatabase
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseID : courseIDs) {
            Course course = SystemDatabase.getCourse(courseID);
            if (course != null) {
                courses.add(course);
            }
        }
        return courses;
    }

    public List<String> getCourseID() {
        return new ArrayList<>(courseIDs); // Return a copy to avoid external modifications
    }

    public void createExam(String examName, String courseID, boolean isPublished, int duration, ArrayList<Question> questions) {
        if (courseIDs.contains(courseID)){
            Course course = SystemDatabase.getCourse(courseID); // Changes: Retrieve Course using courseID
            if (course != null) {
                Exam exam = new Exam(examName, course, isPublished, duration, questions);
                return;
            }
            throw new IllegalArgumentException("No such course");
        }
        throw new IllegalArgumentException("You are not permitted to manage this course. Please contact administrator.");
    }

    public void addExam(Exam exam, String courseID) {
        // Exams are added to courses when they are constructed.
        // This method is called when the exam is not initialized with a course.
        if (courseIDs.contains(courseID)) {
            Course course = SystemDatabase.getCourse(courseID); // Changes: Retrieve Course using courseID
            if (course != null) {
                exam.setCourse(course);
                return;
            }
            throw new IllegalArgumentException("No such course");
        }
        throw new IllegalArgumentException("You are not permitted to manage this course. Please contact administrator.");
    }

    public void deleteExam(String examName, String courseID) {
        if (courseIDs.contains(courseID)) {
            Course course = SystemDatabase.getCourse(courseID); // Changes: Retrieve Course using courseID
            if (course != null) {
                course.dropExam(examName);
                return;
            }
            throw new IllegalArgumentException("No such course");
            }
        throw new IllegalArgumentException("You are not permitted to manage this course. Please contact administrator.");
    }

    public void updateExam(String examName, Exam exam, String courseID) {
        Course course = SystemDatabase.getCourse(courseID); // Changes: Retrieve Course using courseID
        updateExam(examName,exam,course);
    }

    public void updateExam(String examName, Exam exam, Course course) {
        if (course != null) {
            if (course.getTeacher().equals(this)) {
                course.updateExam(examName, exam);
            } else {
                throw new IllegalArgumentException("Not allowed to access this course");
            }
        }
        throw new IllegalArgumentException("No such course");
    }

    public ArrayList<Exam> getExams() {
        ArrayList<Exam> exams = new ArrayList<>();
        for (String courseID : courseIDs) {
            Course course = SystemDatabase.getCourse(courseID); // Changes: Retrieve Course using courseID
            if (course != null) {
                exams.addAll(course.getExams());
            }
        }
        return exams;
    }

    public void addCourse(Course course) {
        addCourse(course.getCourseID()); // Changes: Store course ID instead of Course object
    }

    public void dropCourse(Course course) {
        dropCourse(course.getCourseID()); // Changes: Use course ID to remove course
    }

    public String getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && (Objects.equals(this.position, ((Teacher) other).position));
    }

    public void viewQuestion() {
        for (Question question : questionBank) {
            System.out.println(question.getContent());
        }
    }

    public void createQuestion(String content, String[] options, String answer, int score, int type) {
        Question question = new Question(content, options, answer, score, type);
        questionBank.add(question);
        SystemDatabase.updateTeacher(this);
    }

    public void createQuestion(Question question) {
        questionBank.add(question);
        SystemDatabase.updateTeacher(this);
    }

    public void deleteQuestion(Question question) {
        questionBank.remove(question);
        SystemDatabase.updateTeacher(this);
    }

    public void viewQuestionBank() {
        for (Question question : questionBank) {
            System.out.println(question.getContent());
        }
    }

    public ArrayList<Question> getQuestionBank() {
        return questionBank;
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

    public void updateQuestion(Question question, String content, String[] options, String answer, int score, int type) {
        question.setContent(content);
        question.setOptions(options);
        question.setAnswer(answer);
        question.setScore(score);
        question.setTypeChoice(type);
        SystemDatabase.updateTeacher(this);
    }
    public void updateQuestion(Question question, String content, String[] options, String answer, int score, String type) {
        question.setContent(content);
        question.setOptions(options);
        question.setAnswer(answer);
        question.setScore(score);
        question.setTypeChoice(type.equals("Multiple") ? 1 : 0);
        SystemDatabase.updateTeacher(this);
    }
}
