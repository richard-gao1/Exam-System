package comp3111.examsystem;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    private Teacher teacher;
    private Course course;
    private Exam exam;
    private Question question;

    @BeforeEach
    void setUp() {
        SystemDatabase database = new SystemDatabase();
        teacher = new Teacher("kwleung","1234","kenneth","male",30,"CSE","Assistant Professor");
        course = new Course("Software Engineering","COMP3111", "CSE",teacher);
        SystemDatabase.createCourse(course);

        question = new Question("What is 2+2?", new String[]{"1", "2", "3", "4"}, "4", 0, 5);
        teacher.getQuestionBank().add(question);

        exam = new Exam("Midterm", course, false, 120);
        exam.addQuestion(question);
        //teacher.addExam(exam, "COMP3111");
    }

    @AfterEach
    void clear(){
        SystemDatabase.removeAll();
    }

    @Test
    void testCreateExam() {
        Exam newExam = new Exam("Final", course, true, 180);
        teacher.addExam(newExam, "COMP3111");

        assertTrue(teacher.getExams().contains(newExam), "The exam should be added to the teacher's exam list.");
    }

    @Test
    void testAddCourse() {
        Course newCourse = new Course("DBMS","COMP3311", "CSE");
        teacher.addCourse(newCourse);

        assertTrue(teacher.getCourses().contains(newCourse), "The course should be added to the teacher's course list.");
    }

    @Test
    void testDropCourse() {
        teacher.dropCourse(course);

        assertFalse(teacher.getCourses().contains(course), "The course should be removed from the teacher's course list.");
    }

    @Test
    void testDeleteExam() {
        teacher.deleteExam(exam.getExamName(), course.getCourseID());

        assertFalse(teacher.getExams().contains(exam), "The exam should be removed from the teacher's exam list.");
    }

    @Test
    void testUpdateExam() {
        Exam updatedExam = new Exam("Midterm", "COMP3111", true, 150);
        teacher.updateExam(exam.getExamName(), updatedExam, course);

        assertFalse(teacher.getExams().contains(exam), "The old exam should be removed from the teacher's exam list.");
        assertTrue(teacher.getExams().contains(updatedExam), "The updated exam should be added to the teacher's exam list.");
    }

    @Test
    void testCreateQuestion() {
        Question newQuestion = new Question("What is 3+3?", new String[]{"1", "2", "3", "6"}, "6", 0, 5);
        teacher.getQuestionBank().add(newQuestion);

        assertTrue(teacher.getQuestionBank().contains(newQuestion), "The question should be added to the teacher's question bank.");
    }

    @Test
    void testDeleteQuestion() {
        teacher.getQuestionBank().remove(question);

        assertFalse(teacher.getQuestionBank().contains(question), "The question should be removed from the teacher's question bank.");
    }

    @Test
    void testUpdateQuestion() {
        Question updatedQuestion = new Question("What is 2+3?", new String[]{"1", "2", "3", "5"}, "5", 0, 5);
        teacher.getQuestionBank().remove(question);
        teacher.getQuestionBank().add(updatedQuestion);

        assertFalse(teacher.getQuestionBank().contains(question), "The old question should be removed from the teacher's question bank.");
        assertTrue(teacher.getQuestionBank().contains(updatedQuestion), "The updated question should be added to the teacher's question bank.");
    }

    @Test
    void testViewQuestionBank() {
        ArrayList<Question> questionBank = teacher.getQuestionBank();

        assertNotNull(questionBank, "The question bank should not be null.");
        assertTrue(questionBank.contains(question), "The question bank should contain the added question.");
    }


}
