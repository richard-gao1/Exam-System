package comp3111.examsystem;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {

    @Test
    void getName() {
        Course c = new Course("software engineering","COMP3111","COMP");
        assertEquals(c.getCourseName(), "software engineering");
    }

    @Test
    void getCourseID() {
        Course c = new Course("software engineering","COMP3111","COMP");
        assertEquals(c.getCourseID(), "COMP3111");
    }

    @Test
    void setName() {
        Course c = new Course("software engineering","COMP3111","COMP");
        c.setName("Software Engineering");
        assertEquals(c.getCourseID(), "Software Engineering");
    }

    @Test
    void addStudent() {

    }

    @Test
    void getStudents() {
    }

    @Test
    void dropStudent() {
    }

    @Test
    void setTeacher() {
    }

    @Test
    void getTeacher() {
    }

    @Test
    void addExam() {
    }

    @Test
    void updateExam() {
    }

    @Test
    void dropExam() {
    }

    @Test
    void testDropExam() {
    }

    @Test
    void getExams() {
    }
}