package comp3111.examsystem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class SystemDatabaseTest {
    
    @BeforeEach
    void construct() {
        removeAll();
        SystemDatabase systemDatabase = new SystemDatabase();
    }

    @AfterEach
    void removeAll() {
        SystemDatabase.removeAll();
    }

    @Test
    void registerStudent() {
        
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        String output = SystemDatabase.registerStudent(student);
        assertEquals("", output);
    }

    @Test
    void registerStudent_sameUsername() {
        
        String username = "whwma";
        Student student1 = new Student(username, "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student1);
        Student student2 = new Student(username, "comp3511", "Ma Wai Him Wesley", "M", 21, "cse");
        String output = SystemDatabase.registerStudent(student2);
        assertEquals("Student username " + username + " already exist", output);
    }

    @Test
    void registerTeacher() {
        
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        String output = SystemDatabase.registerTeacher(teacher);
        assertEquals("", output);
    }

    @Test
    void registerTeacher_sameUsername() {
        String username = "whwma";
        Teacher teacher1 = new Teacher(username, "comp3111", "Ma Wai Him", "F", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher1);
        Teacher teacher2 = new Teacher(username, "comp3511", "Ma Wai Him", "F", 21, "cse", "Professor");
        String output = SystemDatabase.registerTeacher(teacher2);
        assertEquals("Teacher username " + username + " already exist", output);
    }

    @Test
    void registerManager() {
        
        Manager manager = new Manager("whwma", "comp3111");
        String output = SystemDatabase.registerManager(manager);
        assertEquals("", output);
    }

    @Test
    void registerManager_sameUsername() {
        
        String username = "whwma";
        Manager manager1 = new Manager(username, "comp3111");
        SystemDatabase.registerManager(manager1);
        Manager manager2 = new Manager(username, "comp3511");
        String output = SystemDatabase.registerManager(manager2);
        assertEquals("Manager username " + username + " already exist", output);
    }

    @Test
    void createCourse() {
        
        Course course = new Course("COMP3111", "Software Engineering", "cse");
        String output = SystemDatabase.createCourse(course);
        assertEquals("", output);
    }

    @Test
    void createCourse_sameCourseID() {
        
        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        SystemDatabase.createCourse(course1);
        Course course2 = new Course("COMP3111", "Software Engineering", "cse");
        String output = SystemDatabase.createCourse(course2);
        assertEquals("Course ID COMP3111 already exist", output);
    }

    @Test
    void updateStudent() {
        
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student);
        Student newStudent = student.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse");
        SystemDatabase.updateStudent(newStudent);
        student = SystemDatabase.getStudent("whwma");
        assertEquals(newStudent, student);
    }

    @Test
    void updateStudent2_sameUsername() {
        
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student);
        Student newStudent = student.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse");
        SystemDatabase.updateStudent(newStudent, newStudent.getUsername());
        student = SystemDatabase.getStudent("whwma");
        assertEquals(newStudent, student);
    }

    @Test
    void updateStudent2_changeUsername() {
        
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
        
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher);
        Teacher newTeacher = teacher.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse", "TA");
        SystemDatabase.updateTeacher(newTeacher);
        teacher = SystemDatabase.getTeacher("whwma");
        assertEquals(newTeacher, teacher);
    }

    @Test
    void updateTeacher2_sameUsername() {
        
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher);
        Teacher newTeacher = teacher.update("whwma", "comp3211", "Ma Wai Him", "F", 22, "cse", "TA");
        SystemDatabase.updateTeacher(newTeacher, newTeacher.getUsername());
        teacher = SystemDatabase.getTeacher("whwma");
        assertEquals(newTeacher, teacher);
    }

    @Test
    void updateTeacher2_changeUsername() {
        
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
    void getStudent() {
        
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student);
        Student output = SystemDatabase.getStudent(student.getUsername());
        assertEquals(student, output);
    }

    @Test
    void getTeacher() {
        
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher);
        Teacher output = SystemDatabase.getTeacher(teacher.getUsername());
        assertEquals(teacher, output);
    }
    
    @Test
    void getManager() {
        
        Manager manager = new Manager("whwma", "comp3111");
        SystemDatabase.registerManager(manager);
        Manager output = SystemDatabase.getManager(manager.getUsername());
        assertEquals(manager, output);
    }

    @Test
    void loginStudent() {
        
        Student student = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        SystemDatabase.registerStudent(student);
        Account output = SystemDatabase.login(student.getUsername(), student.getPassword(), AccountType.STUDENT);
        assertEquals(student, output);
    }

    @Test
    void loginTeacher() {
        
        Teacher teacher = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ", "Professor");
        SystemDatabase.registerTeacher(teacher);
        Account output = SystemDatabase.login(teacher.getUsername(), teacher.getPassword(), AccountType.TEACHER);
        assertEquals(teacher, output);
    }

    @Test
    void loginManager() {
        
        Manager manager = new Manager("whwma", "comp3111");
        SystemDatabase.registerManager(manager);
        Account output = SystemDatabase.login(manager.getUsername(), manager.getPassword(), AccountType.MANAGER);
        assertEquals(manager, output);
    }

    @Test
    void modifyCourse1() {
        
        Course course = new Course("COMP3111", "Software Engineering", "cse");
        SystemDatabase.createCourse(course);
        course.update("COMP3111", "Operating Systems", "cse");
        SystemDatabase.modifyCourse(course, course.getCourseID());
        Course output = SystemDatabase.getCourse(course.getCourseID());
        assertEquals(output, course);
    }

    @Test
    void modifyCourse2() {
        
        Course course = new Course("COMP3111", "Software Engineering", "cse");
        SystemDatabase.createCourse(course);
        course.update("ECON3113", "Microeconomics Theory I", "econ");
        SystemDatabase.modifyCourse(course, "COMP3111");
        Course output1 = SystemDatabase.getCourse("COMP3111");
        Course output2 = SystemDatabase.getCourse(course.getCourseID());
        assertNull(output1);
        assertEquals(output2, course);
    }

    @Test
    void getStudentList() {
        
        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "M", 21, "econ");
        Student student2 = new Student("wktangaf", "comp3211", "Tang Wai Kin", "M", 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "M", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);
        Student[] output = SystemDatabase.getStudentList("", "", "").toArray(Student[]::new);
        Student[] expected = {student1, student2, student3};
        assertArrayEquals(expected, output);
    }

    @Test
    void getStudentList_filter1() {
        
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
