package comp3111.examsystem;

import javafx.beans.property.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class Exam {
    private String examName; // Primitive for JSON serialization
    private boolean isPublished; // Primitive for JSON serialization
    private int duration; // Primitive for JSON serialization
    private ArrayList<Question> questions = new ArrayList<>(); // For JSON serialization

    private String courseID; // Store course as courseID (Change)
    private HashMap<String, Grade> studentToGrades = new HashMap<>();

    // Constructors
    public Exam(String examName, Course course, boolean isPublished, int duration) {
        setExamName(examName);
        this.courseID = course != null ? course.getCourseID() : null; // Change: Store courseID
        this.isPublished = isPublished;
        this.duration = duration;
        if (course != null) {
            course.addExam(this);
        }
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
        return new SimpleStringProperty(this.examName);
    }

    public BooleanProperty isPublishedProperty() {
        return new SimpleBooleanProperty(this.isPublished);
    }

    public IntegerProperty durationProperty() {
        return new SimpleIntegerProperty(this.duration);
    }

    public StringProperty courseIDProperty() {
        return new SimpleStringProperty(this.courseID);
    }

    // Getters and Setters for primitive fields
    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public boolean getIsPublished() {
        return isPublished;
    }

    public void setPublished(boolean isPublished) {
        this.isPublished = isPublished;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public Course getCourse() {
        // Change: Dynamically retrieve the Course object using courseID
        return courseID != null ? SystemDatabase.getCourse(courseID) : null;
    }

    public void setCourse(Course course) {
        if (course != null) {
            if (this.courseID != null) {
                Course existingCourse = SystemDatabase.getCourse(this.courseID); // Fetch the current course
                if (existingCourse != null) {
                    existingCourse.dropExam(this);
                }
            }
            this.courseID = course.getCourseID(); // Change: Store courseID
            course.addExam(this);
        }
    }

    public HashMap<String, Grade> getStudentGrades() {
        return studentToGrades;
    }

    public void setStudentGrades(HashMap<String, Grade> studentToGrades) {
        this.studentToGrades = studentToGrades;
    }

    // Other Methods
    public void addQuestion(Question question) {
        if (!questions.contains(question)) {
            questions.add(question);
        } else {
            throw new IllegalArgumentException("Question already exists in the exam");
        }
    }

    public void removeQuestion(Question question) {
        if (questions.contains(question)) {
            questions.remove(question);
        } else {
            throw new IllegalArgumentException("Question does not exist in the exam");
        }
    }

    public int getFullScore() {
        int score = 0;
        for (Question q : questions) score += q.getScore();
        return score;
    }

    public String parseAnswer(String answer) {
        char[] chars = answer.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

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

    public void gradeStudent(Student student, Integer examScore, int timeSpend) {
        studentToGrades.put(student.getUsername(), new Grade(student.getName(), getCourse().getCourseID(), getExamName(), examScore, getFullScore(), Math.min(timeSpend, duration)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exam exam = (Exam) o;
        return isPublished == exam.isPublished && getDuration() == exam.getDuration() && Objects.equals(getExamName(), exam.getExamName()) && getQuestions().containsAll(exam.getQuestions())&& exam.getQuestions().containsAll(getQuestions()) && Objects.equals(getCourse().getCourseID(), exam.getCourse().getCourseID()) && Objects.equals(studentToGrades, exam.studentToGrades);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getExamName(), isPublished, getDuration(), getQuestions(), getCourse(), studentToGrades);
    }
}
