package comp3111.examsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExamTest {
    private Teacher teacher;
    private Course course;
    private Exam exam;
    private Question question;

    @BeforeEach
    void setUp() {
        SystemDatabase.removeAll();
        SystemDatabase database = new SystemDatabase();
        teacher = new Teacher("kwleung","1234","kenneth","male",30,"CSE","Assistant Professor");
        course = new Course("COMP3111", "Software Engineering","CSE",teacher);
        SystemDatabase.createCourse(course);

        question = new Question("What is 2+2?", new String[]{"1", "2", "3", "4"}, "D", 10, 0);
        teacher.createQuestion(question);

        exam = new Exam("Midterm", course, false, 120);
        exam.addQuestion(question);
    }

    @Test
    void examNameProperty() {
    }

    @Test
    void isPublishedProperty() {
    }

    @Test
    void durationProperty() {
    }

    @Test
    void courseIDProperty() {
    }

    @Test
    void getExamName() {
    }

    @Test
    void setExamName() {
    }

    @Test
    void getIsPublished() {
    }

    @Test
    void setPublished() {
    }

    @Test
    void getDuration() {
    }

    @Test
    void setDuration() {
    }

    @Test
    void getQuestions() {
    }

    @Test
    void setQuestions() {
    }

    @Test
    void getCourse() {
    }

    @Test
    void setCourse() {
    }

    @Test
    void getStudentGrades() {
    }

    @Test
    void setStudentGrades() {
    }

    @Test
    void addQuestion() {
        assertTrue(exam.getQuestions().contains(question));
    }

    @Test
    void updateQuestion() {
    }

    @Test
    void removeQuestion() {
        assertTrue(exam.removeQuestion(question));
    }

    @Test
    void getFullScore() {
    }

    @Test
    void parseAnswer() {
    }

    @Test
    void grade() {
    }

    @Test
    void gradeStudent() {
    }

    @Test
    void testEquals() {
    }
}