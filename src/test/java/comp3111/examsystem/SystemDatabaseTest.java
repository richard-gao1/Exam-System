package comp3111.examsystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SystemDatabaseTest {
    @Test
    void removeAllTest(){
        SystemDatabase db = new SystemDatabase();
        SystemDatabase.removeAll();
        assertFalse(
                (new File("data")).exists());
    }
    @Test
    void studentRegisterTest() {
        // normal registration
        SystemDatabase db = new SystemDatabase();
        Student student = new Student("user", "pass", "name", "male", 21, "department");
        SystemDatabase.registerStudent(student);
        assertEquals(SystemDatabase.getStudent("user"), student);

        // don't insert dup usernames
        Student student1 = new Student("user", "pass", "name", "male", 21, "department");
        assertEquals("Student username " + student1.getUsername() + " already exist",SystemDatabase.registerStudent(student1));
        SystemDatabase.removeAll();
    }

    @Test
    void studentLoginTest() {
        // normal registration
        SystemDatabase db = new SystemDatabase();
        Student registeredStudent = new Student("user", "pass", "name", "male", 21, "department");
        SystemDatabase.registerStudent(registeredStudent);

        // proper login
        assertEquals(registeredStudent, SystemDatabase.login("user","pass",AccountType.STUDENT));

        // incorrect username
        assertNull(SystemDatabase.login("wrong", "pass",AccountType.STUDENT));

        // incorrect password
        assertNull(SystemDatabase.login("user","wrong",AccountType.STUDENT));
        SystemDatabase.removeAll();
    }

    @Test
    void teacherRegisterTest() {
        // normal registration
        SystemDatabase db = new SystemDatabase();
        Teacher teacher = new Teacher("user", "pass", "name", "male", 21, "department","Professor");
        SystemDatabase.registerTeacher(teacher);
        assertEquals(SystemDatabase.getTeacher("user"), teacher);

        // don't insert dup usernames
        Teacher teacher1 = new Teacher("user", "pass", "name", "male", 21, "department","Lecturer");
        assertEquals("Teacher username " + teacher1.getUsername() + " already exist",SystemDatabase.registerTeacher(teacher1));
        SystemDatabase.removeAll();
    }

    @Test
    void teacherLoginTest() {
        // normal registration
        SystemDatabase db = new SystemDatabase();
        Teacher registeredTeacher = new Teacher("user", "pass", "name", "male", 21, "department","Chair Professor");
        SystemDatabase.registerTeacher(registeredTeacher);

        // proper login
        assertEquals(registeredTeacher, SystemDatabase.login("user","pass",AccountType.TEACHER));

        // incorrect username
        assertNull(SystemDatabase.login("wrong", "pass",AccountType.TEACHER));

        // incorrect password
        assertNull(SystemDatabase.login("user","wrong",AccountType.TEACHER));
        SystemDatabase.removeAll();
    }
}