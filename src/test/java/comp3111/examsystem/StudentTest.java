package comp3111.examsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class StudentTest {
    Student student;
    Course course;
    Exam exam;
    Question question;

    @BeforeEach
    void setup(){
        SystemDatabase.removeAll();
        SystemDatabase a = new SystemDatabase();
        student = new Student("wktangaf", "1234","Kenny Tang",Gender.list[0],18,"CSE" );
        SystemDatabase.registerStudent(student);
        course = new Course("COMP3111","software engineering","CSE");
        SystemDatabase.createCourse(course);
        course.addStudent(student);
        exam = new Exam("Midterm",course,false,100);
        question = new Question("Test",new String[]{"1","2","3","4"},"AD",10,1);
    }

    @Test
    void getCourseIDs() {
        assertTrue(student.getCourseIDs().contains("COMP3111"));
    }

    @Test
    void getCourses() {
        assertTrue(student.getCourses().contains(course));
    }

    @Test
    void addCourse() {
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
                ()->student.addCourse(course));
        assertEquals(thrown.getMessage(),"Already enrolled in this course");
    }

    @Test
    void dropCourse() {
        student.dropCourse(course);
        assertTrue(student.getCourses().isEmpty());
    }

    @Test
    void getExams() {
        assertTrue(student.getExams().contains(exam));
    }
}