package comp3111.examsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {
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
        SystemDatabase.modifyCourse(course,course.getCourseID());
        System.out.println("end setup");
    }

//    @AfterEach
//    void tearDown(){
//        SystemDatabase.removeAll();
//    }


    @Test
    void testCreateExam() {
        teacher.createExam("Final", course.getCourseID(), true, 180,null);
        assertEquals(2, teacher.getExams().size(), "The exam should be added to the teacher's exam list.");
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


    }

    @Test
    void testCreateQuestion() {
        Question newQuestion = new Question("What is the first positive even number?", new String[]{"1", "2", "3", "6"}, "b" , 0, 5);
        teacher.getQuestionBank().add(newQuestion);

        assertTrue(teacher.getQuestionBank().contains(newQuestion), "The question should be added to the teacher's question bank.");
    }

    @Test
    void testDeleteQuestion() {
        System.out.println(question.toString());
        System.out.println(exam.getQuestions().getFirst().toString());
        teacher.deleteQuestion(question);

        assertFalse(teacher.getQuestionBank().contains(question), "The question should be removed from the teacher's question bank.");
        assertFalse(teacher.getCourses().getFirst().getExams().getFirst().getQuestions().contains(question),"The question should be removed from exam.");

    }

    @Test
    void testUpdateQuestion() {
        for (Question q: teacher.getCourses().getFirst().getExams().getFirst().getQuestions()){
            System.out.println(q.toString());
        }
        //assertTrue(teacher.getCourses().getFirst().getExams().getFirst().getQuestions().contains(question), "teacher.getCourses().getFirst().getExams().getFirst().getQuestions().getFirst().getContent()");
        teacher.updateQuestion(question, "What is 2+3?", new String[]{"1", "2", "3", "5"}, "D", 0, "Single");
        assertEquals(question.getContent(),"What is 2+3?" );
        for (Question q: teacher.getCourses().getFirst().getExams().getFirst().getQuestions()){
            System.out.println(q.toString());
        }
        assertFalse(teacher.getQuestionBank().getFirst().getContent().equals("What is 2+2?"), "The old question should be removed from the teacher's question bank.");

        assertTrue(teacher.getCourses().getFirst().getExams().getFirst().getQuestions().contains(question), teacher.getCourses().getFirst().getExams().getFirst().getQuestions().getFirst().getContent());
    }

    @Test
    void testViewQuestionBank() {
        ArrayList<Question> questionBank = teacher.getQuestionBank();

        assertNotNull(questionBank, "The question bank should not be null.");
        assertTrue(questionBank.contains(question), "The question bank should contain the added question.");
    }


    @Test
    void update() {
    }

    @Test
    void addCourse() {
    }

    @Test
    void dropCourse() {
    }

    @Test
    void getCourses() {
        boolean a = false;
        for (Course c: teacher.getCourses()){
            System.out.println(course.getExams().containsAll(c.getExams()));
            System.out.println(course.getExams().getFirst().equals(c.getExams().getFirst()));
            System.out.println(course.getExams().getFirst());
            System.out.println(c.getExams().getFirst());
            if (c.equals(course)) a = true;
        }
        assertTrue(a);
    }

    @Test
    void getCourseID() {
    }

    @Test
    void createExam() {
    }

    @Test
    void addExam() {
    }

    @Test
    void deleteExam() {
    }

    @Test
    void updateExam() {
    }

    @Test
    void getExams() {
    }

    @Test
    void getPosition() {
    }

    @Test
    void viewQuestion() {
    }

    @Test
    void createQuestion() {
    }


    @Test
    void deleteQuestion() {
    }

    @Test
    void viewQuestionBank() {
    }

    @Test
    void getQuestionBank() {
    }

    @Test
    void viewStudent() {
    }

    @Test
    void viewStudentAnswer() {
    }

    @Test
    void gradeStudentAnswer() {
    }

    @Test
    void updateQuestion() {
    }

}
