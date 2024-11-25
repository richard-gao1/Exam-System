package comp3111.examsystem;

import com.google.gson.Gson;

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

    /**
     * Adds a new course to the list of courses taught by this teacher.
     *
     * @param courseID The ID of the course to be added.
     *
     */
    public void addCourse(String courseID) {
        if (!courseIDs.contains(courseID)) {
            courseIDs.add(courseID);
            SystemDatabase.updateTeacher(this);
        }
    }

    /**
     * Removes a course from the list of courses taught by this teacher.
     *
     * @param courseID The ID of the course to be removed.
     */
    public void dropCourse(String courseID) {
        courseIDs.remove(courseID);
        SystemDatabase.updateTeacher(this);
    }

    /**
     * Retrieves a list of Course objects associated with the teacher.
     *
     * @return A list of Course objects.
     */
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

    /**
     * Returns a list of course IDs taught by the teacher.
     *
     * @return A list of course IDs.
     */
    public List<String> getCourseID() {
        return new ArrayList<>(courseIDs); // Return a copy to avoid external modifications
    }

    /**
     * Creates a new exam for a specified course.
     *
     * @param examName   The name of the exam.
     * @param courseID   The ID of the course for which the exam is created.
     * @param isPublished Indicates whether the exam is published.
     * @param duration   The duration of the exam in minutes.
     * @param questions  A list of questions for the exam.
     *
     * @throws IllegalArgumentException If the teacher does not have permission to manage the
    specified course
                                        or if there is no such course.
     */
    public void createExam(String examName, String courseID, boolean isPublished, int duration, ArrayList<Question> questions) {
        Course course = SystemDatabase.getCourse(courseID); // Changes: Retrieve Course using courseID
        if (course != null) {
            if (courseIDs.contains(courseID)){
                Exam exam = new Exam(examName, course, isPublished, duration, questions);
                return;
            }
            throw new IllegalArgumentException("You are not permitted to manage this course. Please contact administrator.");
        }
        throw new IllegalArgumentException("No such course");
    }

    /**
     * Adds an existing exam to a specified course.
     *
     * @param exam     The exam object to be added.
     * @param courseID The ID of the course to which the exam should be added.
     *
     * @throws IllegalArgumentException If the teacher does not have permission to manage the
    specified course
     *                                  or if there is no such course.
     */
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

    /**
     * Deletes an exam from a specified course.
     *
     * @param examName The name of the exam to be deleted.
     * @param courseID The ID of the course from which the exam should be deleted.
     *
     * @throws IllegalArgumentException If the teacher does not have permission to manage the
    specified course
     *                                  or if there is no such course.
     */
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

    /**
     * Deletes an exam from a specified course.
     *
     * @param exam  The exam object to be deleted.
     * @param course The course object from which the exam should be deleted.
     *
     * @throws IllegalArgumentException If the teacher does not have permission to manage the
    specified course
     *                                  or if there is no such course.
     */
    public void deleteExam(Exam exam, Course course){
        if (course != null){
            if (courseIDs.contains(course.getCourseID())){
                course.dropExam(exam);
                return;
            }
            throw new IllegalArgumentException("You are not permitted to manage this course. Please contact administrator.");
        }
        throw new IllegalArgumentException("No such course");
    }

    /**
     * Updates an existing exam with new details.
     *
     * @param oldExamName The original name of the exam to be updated.
     * @param oldCourse   The original course associated with the exam.
     * @param examName    The new name for the exam.
     * @param courseID    The ID of the new course for the exam.
     * @param isPublished Indicates whether the updated exam should be published.
     * @param duration    The new duration of the exam in minutes.
     * @param questions   A list of new questions for the exam.
     *
     * @throws IllegalArgumentException If there is no such course or if the teacher does not
    have permission
     *                                  to manage the specified course.
     */
    public void updateExam(String oldExamName, Course oldCourse, String examName, String courseID, boolean isPublished, int duration, ArrayList<Question> questions) {
        Course course = SystemDatabase.getCourse(courseID); // Changes: Retrieve Course using courseID
        updateExam(oldExamName, oldCourse, examName,  course,  isPublished,  duration, questions);
    }

    /**
     * Updates an existing exam with new details.
     *
     * @param oldExamName The original name of the exam to be updated.
     * @param oldCourse   The original course associated with the exam.
     * @param examName    The new name for the exam.
     * @param course      The new course for the exam.
     * @param isPublished Indicates whether the updated exam should be published.
     * @param duration    The new duration of the exam in minutes.
     * @param questions   A list of new questions for the exam.
     *
     * @throws IllegalArgumentException If there is no such course or if the teacher does not
    have permission
     *                                  to manage the specified course.
     */
    public void updateExam(String oldExamName, Course oldCourse, String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        if (course != null) {
            if (course.getTeacher().equals(this)) {
                // Case 1: course is the same
                //if (course.getCourseID().equals(oldCourse.getCourseID()))
                    oldCourse.updateExam(oldExamName,  examName,  course,  isPublished,  duration, questions);
                // Case 2: need to update the course as well
            } else {
                throw new IllegalArgumentException("Not allowed to access this course");
            }
        }
        else throw new IllegalArgumentException("No such course");
    }

    /**
     * Retrieves a list of all exams associated with the teacher's courses.
     *
     * @return An ArrayList containing all exam objects managed by the teacher.
     */
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

    /**
     * Adds a course to the teacher's list of courses.
     *
     * @param course The Course object to be added.
     */
    public void addCourse(Course course) {
        addCourse(course.getCourseID()); // Changes: Store course ID instead of Course object
    }

    /**
     * Removes a course from the teacher's list of courses.
     *
     * @param course The Course object to be removed.
     */
    public void dropCourse(Course course) {
        dropCourse(course.getCourseID()); // Changes: Use course ID to remove course
    }

    /**
     * Retrieves the position of the teacher.
     *
     * @return A string representing the teacher's position.
     */
    public String getPosition() {
        return this.position;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other) && (Objects.equals(this.position, ((Teacher) other).position));
    }
    /**
     * Creates a new question and adds it to the teacher's question bank.
     *
     * @param content The content of the question.
     * @param options An array of strings representing the answer choices.
     * @param answer  The correct answer to the question.
     * @param score   The score assigned to this question.
     * @param type    The type of the question (e.g., multiple-choice, true/false).
     *
     * @throws IllegalArgumentException If a question with the same content and options
    already exists in the question bank.
     */
    public void createQuestion(String content, String[] options, String answer, int score, int type) {
        Question question = new Question(content, options, answer, score, type);
        for (Question q: questionBank){
            if (q.getContent().equals(question.getContent())
                    && q.getOptions().containsAll(question.getOptions())
                    && question.getOptions().containsAll(q.getOptions())){
                throw new IllegalArgumentException("This question already existed.");
            }
        }
        questionBank.add(question);
        SystemDatabase.updateTeacher(this);
    }

    /**
     * Creates a new question and adds it to the teacher's question bank.
     *
     * @param question The Question object to be added.
     *
     * @throws IllegalArgumentException If a question with the same content and options
    already exists in the question bank.
     */
    public void createQuestion(Question question) {
        for (Question q: questionBank){
            if (q.getContent().equals(question.getContent())
                    && q.getOptions().containsAll(question.getOptions())
                    && question.getOptions().containsAll(q.getOptions())){
                throw new IllegalArgumentException("This question already existed.");
            }
        }
        questionBank.add(question);
        SystemDatabase.updateTeacher(this);
    }

    /**
     * Deletes a specified question from the teacher's question bank and removes it from all
     associated exams.
     *
     * @param question The Question object to be deleted.
     */
    public void deleteQuestion(Question question) {
        String result = "no exam";
        questionBank.remove(question);
        for (Course c:getCourses()){
            c.deleteExamQuestions(question);
        }
        SystemDatabase.updateTeacher(this);
    }

    public void viewQuestionBank() {
        for (Question question : questionBank) {
            System.out.println(question.getContent());
        }
    }

    /**
     * Displays the content of all questions in the teacher's question bank.
     */
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

    /**
     * Updates the details of an existing question in the teacher's question bank.
     *
     * @param question The Question object to be updated.
     * @param content  The new content for the question.
     * @param options  An array of strings representing the new answer choices.
     * @param answer   The new correct answer to the question.
     * @param score    The new score assigned to this question.
     * @param type     The new type of the question as integer (0 - Single, 1 - Multiple).
     */
    public void updateQuestion(Question question, String content, String[] options, String answer, int score, int type) {
        ArrayList<Exam> examList = new ArrayList<>();
        for (Course course: getCourses()){
            course.updateExamQuestions(question,  content,  options,  answer,  score,  (type ==0?"Single":"Multiple"));
        }
        question.setContent(content);
        question.setOptions(options);
        question.setTypeChoice(type);
        question.setAnswer(answer);
        question.setScore(score);
        SystemDatabase.updateTeacher(this);
    }

    /**
     * Updates the details of an existing question in the teacher's question bank.
     *
     * @param question The Question object to be updated.
     * @param content  The new content for the question.
     * @param options  An array of strings representing the new answer choices.
     * @param answer   The new correct answer to the question.
     * @param score    The new score assigned to this question.
     * @param type     The new type of the question ("Multiple", "Single").
     */
    public void updateQuestion(Question question, String content, String[] options, String answer, int score, String type) {
        ArrayList<Exam> examList = new ArrayList<>();
        for (Course course: getCourses()){
                course.updateExamQuestions(question,  content,  options,  answer,  score,  type);
        }
        question.setContent(content);
        question.setOptions(options);
        question.setTypeChoice(type.equals("Multiple") ? 1 : 0);
        question.setAnswer(answer);
        question.setScore(score);
        SystemDatabase.updateTeacher(this);

    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
