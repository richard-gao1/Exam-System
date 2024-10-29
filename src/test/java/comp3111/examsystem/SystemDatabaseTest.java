package comp3111.examsystem;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SystemDatabaseTest {
    @Test
    void studentRegisterTest() {
        // normal registration
        SystemDatabase db = new SystemDatabase();
        Student student = db.studentRegister("user", "pass", "name", "male", 21, "department");
        HashMap<String, HashMap<String, Student>> studentRegistry = db.getStudentRegistry();
        assertEquals(studentRegistry.get("user").get("pass"), student);

        // don't insert dup usernames
        Student student1 = db.studentRegister("user", "pass", "name", "male", 21, "department");
        assertNull(student1);
    }

    @Test
    void studentLoginTest() {
        // normal registration
        SystemDatabase db = new SystemDatabase();
        Student registeredStudent = db.studentRegister("user", "pass", "name", "male", 21, "department");
        Student loggedInStudent = db.studentLogin("user", "pass");
        assertEquals(registeredStudent, loggedInStudent);
    }
}