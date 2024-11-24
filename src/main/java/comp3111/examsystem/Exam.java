package comp3111.examsystem;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Represents an Exam, storing the name of the exam, list of questions and student grades
 */
public class Exam {
    private String examName;        // The name of the exam
    private boolean isPublished;    // Stores whether the exam has been published
    private int duration;           // Duration in minutes
    private ArrayList<Question> questions = new ArrayList<>();

    private String courseID;
    private HashMap<String, Grade> studentToGrades = new HashMap<>(); // Maps student ID to their grade

    /**
     * Constructs a new Exam object with the specified exam name, associated course,
     * publication status and duration. If the course is not null, this exam will be added
     * to the course's list of exams.
     *
     * @param examName   The name of the exam.
     * @param course     The Course object associated with this exam.
     * @param isPublished A boolean indicating whether the exam has been published.
     * @param duration   The duration of the exam in minutes.
     */
    public Exam(String examName, Course course, boolean isPublished, int duration) {
        setExamName(examName);
        this.courseID = course != null ? course.getCourseID() : null; // Change: Store courseID
        this.isPublished = isPublished;
        this.duration = duration;
        if (course != null) {
            course.addExam(this);
        }
    }

    /**
     * Constructs a new Exam object with the specified exam name, associated course,
     * publication status, duration, and a list of questions. If the course is not
     * null, this exam will be added to the course's list of exams.
     *
     * @param examName   The name of the exam.
     * @param course     The Course object associated with this exam.
     * @param isPublished A boolean indicating whether the exam has been published.
     * @param duration   The duration of the exam in minutes.
     * @param questions  An ArrayList containing the Question objects included in this exam.
     */

    public Exam(String examName, Course course, boolean isPublished, int duration, ArrayList<Question> questions) {
        this(examName, course, isPublished, duration);
        if (questions != null) {
            this.questions.addAll(questions);
        }
    }

    /**
     * Constructs a new Exam object with the specified exam name, course ID,
     * publication status, duration, and a list of questions.
     *
     * @param examName   The name of the exam.
     * @param courseID   The identifier for the Course associated with this exam.
     * @param isPublished A boolean indicating whether the exam has been published.
     * @param duration   The duration of the exam in minutes.
     * @param questions  An ArrayList containing the Question objects included in this exam.
     */
    public Exam(String examName, String courseID, boolean isPublished, int duration, ArrayList<Question> questions) {
        this(examName, (Course) SystemDatabase.getCourse(courseID), isPublished, duration);
        if (questions != null) {
            this.questions.addAll(questions);
        }
    }

    /**
     * Constructs a new Exam object with the specified exam name, course ID,
     * publication status, and duration.
     *
     * @param examName   The name of the exam.
     * @param courseID   The identifier for the Course associated with this exam.
     * @param isPublished A boolean indicating whether the exam has been published.
     * @param duration   The duration of the exam in minutes.
     */
    public Exam(String examName, String courseID, boolean isPublished, int duration) {
        this(examName, (Course) SystemDatabase.getCourse(courseID), isPublished, duration);
    }

    /**
     * Returns a StringProperty representation of the exam name.
     *
     * @return A SimpleStringProperty containing the exam name.
     */
    public StringProperty examNameProperty() {
        return new SimpleStringProperty(this.examName);
    }

    /**
     * Returns a BooleanProperty indicating whether the exam is published.
     *
     * @return A SimpleBooleanProperty representing the publication status of the exam.
     */
    public BooleanProperty isPublishedProperty() {
        return new SimpleBooleanProperty(this.isPublished);
    }

    /**
     * Returns an IntegerProperty representing the duration of the exam in minutes.
     *
     * @return A SimpleIntegerProperty containing the exam duration.
     */
    public IntegerProperty durationProperty() {
        return new SimpleIntegerProperty(this.duration);
    }

    /**
     * Returns a StringProperty representation of the course ID associated with this exam.
     *
     * @return A SimpleStringProperty containing the course ID.
     */
    public StringProperty courseIDProperty() {
        return new SimpleStringProperty(this.courseID);
    }

    /**
     * Retrieves the name of the exam.
     *
     * @return The name of the exam as a String.
     */
    public String getExamName() {
        return examName;
    }

    /**
     * Sets a new name for the exam.
     *
     * @param examName The new name to be set for the exam.
     */
    public void setExamName(String examName) {
        this.examName = examName;
    }

    /**
     * Checks if the exam is published.
     *
     * @return True if the exam is published, false otherwise.
     */
    public boolean getIsPublished() {
        return isPublished;
    }

    /**
     * Sets the publication status of the exam.
     *
     * @param isPublished The new publication status to be set for the exam.
     */
    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    /**
     * Retrieves the duration of the exam in minutes.
     *
     * @return The duration of the exam as an int.
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Sets a new duration for the exam.
     *
     * @param duration The new duration to be set for the exam in minutes.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Retrieves the list of questions associated with the exam.
     *
     * @return An ArrayList containing all Question objects included in this exam.
     */
    public ArrayList<Question> getQuestions() {
        return questions;
    }

    /**
     * Sets a new list of questions for the exam.
     *
     * @param questions The new ArrayList of Question objects to be set for the exam.
     */
    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    /**
     * Retrieves the Course object associated with this exam.
     *
     * @return The Course object if courseID is not null, otherwise returns null.
     */
    public Course getCourse() {
        // Change: Dynamically retrieve the Course object using courseID
        return courseID != null ? SystemDatabase.getCourse(courseID) : null;
    }

    /**
     * Sets a new course for the exam. If there was an existing course associated with
     * this exam, it removes the exam from that course before setting the new one.
     *
     * @param course The new Course object to be set for the exam.
     */
    public void setCourse(Course course) {
        if (course != null) {
            if (this.courseID != null) {
                Course existingCourse = SystemDatabase.getCourse(this.courseID); // Fetch the current course
                if (existingCourse != null) {
                    existingCourse.dropExam(this);
                }
            }
            this.courseID = course.getCourseID(); // Change: Store courseID
            boolean haveExam = false;
            for (Exam e : course.getExams()) {
                haveExam = haveExam | Objects.equals(e.getExamName(), this.getExamName());
            }
            if (!haveExam) course.addExam(this);
            else throw new IllegalArgumentException("Already have an exam with the same name.");
        }
    }

    /**
     * Retrieves the HashMap of student grades associated with this exam.
     *
     * @return A HashMap where keys are student IDs (String) and values are their corresponding
    Grades.
     */
    public HashMap<String, Grade> getStudentGrades() {
        return studentToGrades;
    }

    /**
     * Sets a new HashMap of student grades for the exam.
     *
     * @param studentToGrades The new HashMap to be set for student grades in this exam.
     */
    public void setStudentGrades(HashMap<String, Grade> studentToGrades) {
        this.studentToGrades = studentToGrades;
    }

    /**
     * Adds a question to the list of questions for the exam. Throws an exception
     * if the question already exists in the exam.
     *
     * @param question The new Question object to be added to the exam.
     * @throws IllegalArgumentException If the question already exists in the exam.
     */
    public void addQuestion(Question question) {
        if (!questions.contains(question)) {
            questions.add(question);
        } else {
            throw new IllegalArgumentException("Question already exists in the exam");
        }
    }

    /**
     * Removes a question from the list of questions for the exam. Throws an exception
     * if the question does not exist in the exam.
     *
     * @param question The Question object to be removed from the exam.
     * @throws IllegalArgumentException If the question does not exist in the exam.
     */
    public boolean removeQuestion(Question question) {
        for (Question q: questions){
            if (q.equals(question)){
                questions.remove(q);
                return true;
            }
        }
        return false;
    }

    public boolean updateQuestion(Question question, String content, String[] options, String answer, int score, String type){
        if (questions.contains(question)){
            Question q = questions.get(questions.indexOf(question));
            q.setScore(score);
            q.setTypeChoice(type);
            q.setAnswer(answer);
            q.setOptions(options);
            q.setContent(content);

            return true;
        }
        return false;
    }

    /**
     * Calculates and returns the total score of all questions in the exam.
     *
     * @return The sum of scores of all questions in the exam.
     */
    public int getFullScore() {
        int score = 0;
        for (Question q : questions) score += q.getScore();
        return score;
    }

    /**
     * Parses a string answer by sorting its characters.
     *
     * @param answer The answer to be parsed.
     * @return A new string with sorted characters from the original answer.
     */
    public String parseAnswer(String answer) {
        char[] chars = answer.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    /**
     * Grades an exam based on a list of student answers. The grading logic differs
     * depending on whether each question is multiple-choice or single-choice.
     *
     * @param answers An ArrayList containing the student's answers for each question.
     * @return The total score obtained by the student in this exam.
     */
    public Integer grade(ArrayList<Integer> answers) {
        int score = 0;
        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            int a = answers.get(i);
            if (question.getTypeChoice() == 0) {
                if (question.getAnswer() == a) {
                    score += question.getScore();
                }
            } else {
                score += Math.max(0,
                        question.getScore()
                                * (Integer.bitCount(a & question.getAnswer())
                                - Integer.bitCount(a & ~question.getAnswer() & 15))
                                / Integer.bitCount(question.getAnswer()));
            }
        }
        return score;
    }

    /**
     * Records a student's grade for this exam. The time spent by the student
     * is capped at the duration of the exam.
     *
     * @param student The Student object whose grade is to be recorded.
     * @param examScore The score obtained by the student in this exam.
     * @param timeSpend The time (in minutes) taken by the student to complete the exam.
     */
    public void gradeStudent(Student student, Integer examScore, int timeSpend) {
        studentToGrades.put(student.getUsername(), new Grade(student.getName(), getCourse().getCourseID(), getExamName(), examScore, getFullScore(), Math.min(timeSpend, duration)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return Objects.equals(getExamName(), exam.getExamName())
                && getQuestions().containsAll(exam.getQuestions())
                && exam.getQuestions().containsAll(getQuestions())
                && Objects.equals(getCourse().getCourseID(), exam.getCourse().getCourseID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExamName(), isPublished, getDuration(), getQuestions(), getCourse(), studentToGrades);
    }
}
