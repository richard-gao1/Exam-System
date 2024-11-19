package comp3111.examsystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class SystemDatabaseTest {

    @Test
    void construct() {
        removeAll();
        SystemDatabase systemDatabase = new SystemDatabase();
    }

    @Test
        SystemDatabase.removeAll();
        assertFalse(
                (new File("data")).exists());
    }

    @Test
    void registerStudent() {
        construct();
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        String output = SystemDatabase.registerStudent(student);
        assertEquals("", output);
    }

    @Test
    void registerStudent_sameUsername() {
        construct();
        String username = "whwma";
        Student student1 = new Student(username, "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student1);
        Student student2 = new Student(username, "comp3511", "Ma Wai Him Wesley", "M", 21, "cse");
        String output = SystemDatabase.registerStudent(student2);
        assertEquals("Student username " + username + " already exist", output);
    }

    @Test
    void registerTeacher() {
        construct();
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        String output = SystemDatabase.registerTeacher(teacher);
        assertEquals("", output);
    }

    @Test
    void registerTeacher_sameUsername() {
        construct();
        String username = "whwma";
        Teacher teacher1 = new Teacher(username, "comp3111", "Ma Wai Him", "F", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher1);
        Teacher teacher2 = new Teacher(username, "comp3511", "Ma Wai Him", "F", 21, "cse", "Professor");
        String output = SystemDatabase.registerTeacher(teacher2);
        assertEquals("Teacher username " + username + " already exist", output);
    }

    @Test
    void registerManager() {
        construct();
        Manager manager = new Manager("whwma", "comp3111");
        String output = SystemDatabase.registerManager(manager);
        assertEquals("", output);
    }

    @Test
    void registerManager_sameUsername() {
        construct();
        String username = "whwma";
        Manager manager1 = new Manager(username, "comp3111");
        SystemDatabase.registerManager(manager1);
        Manager manager2 = new Manager(username, "comp3511");
        String output = SystemDatabase.registerManager(manager2);
        assertEquals("Manager username " + username + " already exist", output);
    }

    @Test
    void createCourse() {
        construct();
        Course course = new Course("COMP3111", "Software Engineering", "cse");
        String output = SystemDatabase.createCourse(course);
        assertEquals("", output);
    }

    @Test
    void createCourse_sameCourseID() {
        construct();
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        SystemDatabase.createCourse(course1);
        Course course2 = new Course("COMP3111", "Software Engineering", "cse");
        String output = SystemDatabase.createCourse(course2);
        assertEquals("Course ID COMP3111 already exist", output);
    }

    @Test
    void updateStudent() {
        construct();
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student);
        Student newStudent = student.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse");
        SystemDatabase.updateStudent(newStudent);
        student = SystemDatabase.getStudent("whwma");
        assertEquals(newStudent, student);
    }

    @Test
    void updateStudent2_sameUsername() {
        construct();
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student);
        Student newStudent = student.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse");
        SystemDatabase.updateStudent(newStudent, newStudent.getUsername());
        student = SystemDatabase.getStudent("whwma");
        assertEquals(newStudent, student);
    }

    @Test
    void updateStudent2_changeUsername() {
        construct();
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student);
        student.update("wktangaf", "comp3511", "Tang Wai Kin", "M", 22, "fina");
        SystemDatabase.updateStudent(student, "whwma");
        Student student1 = SystemDatabase.getStudent("whwma");
        Student student2 = SystemDatabase.getStudent("wktangaf");
        assertNull(student1);
        assertEquals(student2, student);
    }

    @Test
    void updateTeacher() {
        construct();
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher);
        Teacher newTeacher = teacher.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse", "TA");
        SystemDatabase.updateTeacher(newTeacher);
        teacher = SystemDatabase.getTeacher("whwma");
        assertEquals(newTeacher, teacher);
    }

    @Test
    void updateTeacher2_sameUsername() {
        construct();
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher);
        Teacher newTeacher = teacher.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse", "TA");
        SystemDatabase.updateTeacher(newTeacher, newTeacher.getUsername());
        teacher = SystemDatabase.getTeacher("whwma");
        assertEquals(newTeacher, teacher);
    }

    @Test
    void updateTeacher2_changeUsername() {
        construct();
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Associate Professor");
        SystemDatabase.registerTeacher(teacher);
        teacher.update("wktangaf", "comp3511", "Tang Wai Kin", "M", 22, "fina", "Professor");
        SystemDatabase.updateTeacher(teacher, "whwma");
        Teacher teacher1 = SystemDatabase.getTeacher("whwma");
        Teacher teacher2 = SystemDatabase.getTeacher("wktangaf");
        assertNull(teacher1);
        assertEquals(teacher2, teacher);
    }

    @Test
        SystemDatabase.registerStudent(student);

    }

    @Test
    }

    @Test
        SystemDatabase.registerTeacher(teacher);
    }

    @Test



    }

    @Test
    void getStudentList_filter1() {
        construct();
        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        Student student2 = new Student("wktangaf", "comp3211", "Tang Wai Kin", "M", 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "M", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);
        Student[] output = SystemDatabase.getStudentList("wktangaf", "", "").toArray(Student[]::new);
        Student[] expected = {student2};
        assertArrayEquals(expected, output);
    }

    @Test
    void getStudentList_filter2() {
        construct();
        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        Student student2 = new Student("wktangaf", "comp3211", "Tang Wai Kin", "M", 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "M", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);
        Student[] output = SystemDatabase.getStudentList("", "Wai", "").toArray(Student[]::new);
        Student[] expected = {student1, student2};
        assertArrayEquals(expected, output);
    }

    @Test
    void getStudentList_filter3() {
        construct();
        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        Student student2 = new Student("wktangaf", "comp3211", "Tang Wai Kin", "M", 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "M", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);
        Student[] output = SystemDatabase.getStudentList("", "Ma Wai Him Wesley", "").toArray(Student[]::new);
        Student[] expected = {student1};
        assertArrayEquals(expected, output);
    }

    @Test
    void getStudentList_filter4() {
        construct();
        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        Student student2 = new Student("wktangaf", "comp3211", "Tang Wai Kin", "M", 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "M", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);
        Student[] output = SystemDatabase.getStudentList("", "", "cse").toArray(Student[]::new);
        Student[] expected = {student3};
        assertArrayEquals(expected, output);
    }

    @Test
    void getTeacherList() {
        construct();
        Teacher teacher1 = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Associate Professor");
        Teacher teacher2 = new Teacher("wktangaf", "comp3511", "Tang Wai Kin", "M", 22, "fina", "Professor");
        SystemDatabase.registerTeacher(teacher1);
        SystemDatabase.registerTeacher(teacher2);
        Teacher[] output = SystemDatabase.getTeacherList("", "", "").toArray(Teacher[]::new);
        Teacher[] expected = {teacher1, teacher2};
        assertArrayEquals(expected, output);
    }

    @Test
    void getCourseList() {
        construct();
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        Course course2 = new Course("COMP3511", "Operating Systems", "cse");
        Course course3 = new Course("COMP3711", "Design and Analysis of Algorithms", "cse");

        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.createCourse(course3);
        Course[] output = SystemDatabase.getCourseList("", "", "").toArray(Course[]::new);
        Course[] expected = {course1, course2, course3};
        assertArrayEquals(expected, output);
    }

    @Test
    void getCourseList_filter1() {
        construct();
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        Course course2 = new Course("COMP3511", "Operating Systems", "cse");
        Course course3 = new Course("COMP3711", "Design and Analysis of Algorithms", "cse");
        Course course4 = new Course("ECON3113", "Microeconomics Theory I", "econ");

        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.createCourse(course3);
        SystemDatabase.createCourse(course4);

        Course[] output = SystemDatabase.getCourseList("COMP", "", "").toArray(Course[]::new);
        Course[] expected = {course1, course2, course3};
        assertArrayEquals(expected, output);
    }

    @Test
    void getCourseList_filter2() {
        construct();
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        Course course2 = new Course("COMP3511", "Operating Systems", "cse");
        Course course3 = new Course("COMP3711", "Design and Analysis of Algorithms", "cse");
        Course course4 = new Course("ECON3113", "Microeconomics Theory I", "econ");

        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.createCourse(course3);
        SystemDatabase.createCourse(course4);

        Course[] output = SystemDatabase.getCourseList("COMP3711", "", "").toArray(Course[]::new);
        Course[] expected = {course3};
        assertArrayEquals(expected, output);
    }

    @Test
    void getCourseList_filter3() {
        construct();
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        Course course2 = new Course("COMP3511", "Operating Systems", "cse");
        Course course3 = new Course("COMP3711", "Design and Analysis of Algorithms", "cse");
        Course course4 = new Course("ECON3113", "Microeconomics Theory I", "econ");

        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.createCourse(course3);
        SystemDatabase.createCourse(course4);

        Course[] output = SystemDatabase.getCourseList("", "Software Engineering", "").toArray(Course[]::new);
        Course[] expected = {course1};
        assertArrayEquals(expected, output);
    }

    @Test
    void getCourseList_filter4() {
        construct();
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        Course course2 = new Course("COMP3511", "Operating Systems", "cse");
        Course course3 = new Course("COMP3711", "Design and Analysis of Algorithms", "cse");
        Course course4 = new Course("ECON3113", "Microeconomics Theory I", "econ");

        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.createCourse(course3);
        SystemDatabase.createCourse(course4);

        Course[] output = SystemDatabase.getCourseList("", "", "econ").toArray(Course[]::new);
        Course[] expected = {course4};
        assertArrayEquals(expected, output);
    }
}