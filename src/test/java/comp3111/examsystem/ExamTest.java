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
        assertEquals(exam.examNameProperty().get(), exam.getExamName());
    }

    @Test
    void isPublishedProperty() {
        assertEquals(exam.isPublishedProperty().get(),exam.getIsPublished());
    }

    @Test
    void durationProperty() {
        assertEquals(exam.durationProperty().get(),exam.getDuration());
    }

    @Test
    void courseIDProperty() {
        assertEquals(exam.courseIDProperty().get(),exam.getCourse().getCourseID());
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
    void setnullCourse() {
        exam.setCourse((Course)null);

    }

    @Test
    void setCourse() {
        Course c = new Course("COMP5111","software engineering ii","CSE");
        SystemDatabase.createCourse(c);
        exam.setCourse(c);
        assertFalse(SystemDatabase.getCourse("COMP3111").getExams().contains(exam));
        assertTrue(SystemDatabase.getCourse("COMP5111").getExams().contains(exam));

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
        assertEquals(exam.getFullScore(),exam.getQuestions().getFirst().getScore());
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