package comp3111.examsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CourseTest {
    @BeforeEach
    void start(){
        SystemDatabase.removeAll();
        SystemDatabase database = new SystemDatabase();
    }

    @Test
    void getName() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        assertEquals("software engineering", c.getCourseName());
    }

    @Test
    void getCourseID() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        assertEquals("COMP3111", c.getCourseID());
    }

    @Test
    void setName() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        c.setName("Software Engineering");
        assertEquals("Software Engineering", c.getCourseName());
    }

    @Test
    void setDepartment(){
        Student s = new Student("s12345", "password", "John Doe", "M", 20, "COMP");
        ArrayList<Student> students = new ArrayList<>();
        students.add(s);
        Course c = new Course("COMP3111", "software engineering", "COMP",students);
        c.setDepartment("CSE");
        assertEquals("CSE",c.getDepartment());
    }

    @Test
    void addStudent() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        Student s = new Student("s12345", "password", "John Doe", "M", 20, "COMP");
        SystemDatabase.registerStudent(s);
        c.addStudent(s);

        List<Student> students = c.getStudents();
        assertEquals(1, students.size());
        assertEquals("John Doe", students.get(0).getName());
    }

    @Test
    void addStudents() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        Student s1 = new Student("s12345", "password", "John Doe", "M", 20, "COMP");
        Student s2 = new Student("s67890", "password", "Jane Smith", "F", 21, "COMP");
        SystemDatabase.registerStudent(s1);
        SystemDatabase.registerStudent(s2);
        List<Student> studentsToAdd = List.of(s1, s2);
        c.addStudents(studentsToAdd);

        List<Student> students = c.getStudents();
        assertEquals(2, students.size());
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("John Doe")));
        assertTrue(students.stream().anyMatch(s -> s.getName().equals("Jane Smith")));
    }

    @Test
    void dropStudent() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        Student s = new Student("s12345", "password", "John Doe", "M", 20, "COMP");
        c.addStudent(s);

        c.dropStudent(s);

        List<Student> students = c.getStudents();
        assertEquals(0, students.size());
    }

    @Test
    void setTeacher() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        Teacher t = new Teacher("t12345", "password", "Prof. Smith", "F", 40, "COMP","TA");

        c.setTeacher(t);

        assertEquals("Prof. Smith", c.getTeacher().getName());
    }



    @Test
    void getTeacher() {
        Teacher t = new Teacher("t12345", "password", "Prof. Smith", "F", 40, "COMP","TA");
        Course c = new Course("COMP3111", "software engineering", "COMP",t);

        Teacher assignedTeacher = c.getTeacher();
        assertNotNull(assignedTeacher);
        assertEquals("Prof. Smith", assignedTeacher.getName());
    }

    @Test
    void addExam() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Exam e = new Exam("Midterm", c, true, 120);

        assertEquals(1, c.getExams().size());
        assertEquals("Midterm", c.getExams().get(0).getExamName());
    }

    @Test
    void addExam_1() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Exam e = new Exam("Midterm", "COMP3211", true, 120);
        IllegalArgumentException thrown =  assertThrows(IllegalArgumentException.class, () -> c.addExam(e),"Expected addStudent to throw, but it didn't");
        assertTrue(thrown.getMessage().equals("Exam is not in this course"));
    }

    @Test
    void updateExam() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Exam e = new Exam("Midterm", c, true, 120);

        ArrayList<Question> newQuestions = new ArrayList<>();
        newQuestions.add(new Question("What is Java?", new String[]{"Language", "Tool", "Framework", "OS"}, "A", 10, 0));
        c.updateExam("Midterm", "Final", c, false, 180, newQuestions);

        Exam updatedExam = c.getExams().get(0);
        assertEquals("Final", updatedExam.getExamName());
        assertEquals(180, updatedExam.getDuration());
        assertEquals(1, updatedExam.getQuestions().size());
    }

    @Test
    void dropExam() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Exam e = new Exam("Midterm", c, true, 120);

        c.dropExam(e);

        assertEquals(0, c.getExams().size());
    }

    @Test
    void getExams() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Exam e1 = new Exam("Midterm", c, true, 120);
        Exam e2 = new Exam("Final", c, false, 180);

        ArrayList<Exam> exams = c.getExams();
        assertEquals(2, exams.size());
        assertTrue(exams.stream().anyMatch(e -> e.getExamName().equals("Midterm")));
        assertTrue(exams.stream().anyMatch(e -> e.getExamName().equals("Final")));
    }

    @Test
    void testEquality() {
        Course c1 = new Course("COMP3111", "software engineering", "COMP");
        Course c2 = new Course("COMP3111", "software engineering", "COMP");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

    @Test
    void updateGrade() {
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Exam e = new Exam("Midterm", c, true, 120);
        Exam a = new Exam("Final",c, false,180);
        c.updateGrade(e);
    }

    @Test
    void deleteExamQuestions(){
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Question q1 = new Question("test",new String[]{"a","b","c","d"},"A",10,0);
        Exam e = new Exam("mid",c,false,100,new ArrayList<>());
        e.addQuestion(q1);
        assertEquals(e.getQuestions().getFirst(),q1);
        c.deleteExamQuestions(q1);
        assertTrue(e.getQuestions().isEmpty());
    }

    @Test
    void updateExamQuestions(){
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Question q1 = new Question("test",new String[]{"a","b","c","d"},"A",10,0);
        Exam e = new Exam("mid",c,false,100,new ArrayList<>());
        e.addQuestion(q1);
        Question q2 = new Question("none",new String[]{"1","11","111","1111"},"BC",100,1);
        c.updateExamQuestions(q1,q2.getContent(),q2.getOptions().toArray(new String[4]), q2.answerProperty().get(),q2.getScore(),"Multiple");
        assertFalse(e.getQuestions().isEmpty());
        assertEquals(q2,e.getQuestions().getFirst());
    }

    @Test
    void update(){
        Course c = new Course("COMP3111", "software engineering", "COMP");
        c.update("COMP3211","AI","CSE");
        assertEquals(c.getCourseID(),"COMP3211");
        assertEquals(c.getCourseName(),"AI");
        assertEquals(c.getDepartment(),"CSE");
    }

    @Test
    void dropnullExam(){
        Course c = new Course("COMP3111", "software engineering", "COMP");
        assertThrows(IllegalArgumentException.class ,()->c.dropExam("Midterm"));
    }

    @Test
    void updateAnotherExam(){
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Question q1 = new Question("test",new String[]{"a","b","c","d"},"A",10,0);
        Exam e = new Exam("mid",c,false,100,new ArrayList<>());
        e.addQuestion(q1);
        Course c2 = new Course("COMP3211", "AI","CSE");
        SystemDatabase.createCourse(c2);
        assertTrue(c2.getExams().isEmpty());
        c.updateExam("mid","Midterm",c2,true,60,new ArrayList<>());
        assertFalse(c2.getExams().isEmpty());
        assertTrue(c.getExams().isEmpty());
    }

    @Test
    void deletenullquestion(){
        Course c = new Course("COMP3111", "software engineering", "COMP");
        SystemDatabase.createCourse(c);
        Question q1 = new Question("test",new String[]{"a","b","c","d"},"A",10,0);
        Exam e = new Exam("mid",c,false,100,new ArrayList<>());
        Question q2 = new Question("none",new String[]{"1","11","111","1111"},"BC",100,1);
        e.addQuestion(q1);
        c.deleteExamQuestions(q2);
        assertFalse(c.getExams().getFirst().getQuestions().isEmpty());
    }
}
