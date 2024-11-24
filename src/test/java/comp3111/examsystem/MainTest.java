package comp3111.examsystem;

import org.junit.jupiter.api.Test;

class MainTest {
    @Test
    void addItems() {
        SystemDatabase.removeAll();
        new SystemDatabase();
        Student student1 = new Student("whwma", "comp3111", "Ma Wai Him Wesley", "Male", 21, "econ");
        Student student2 = new Student("wktangaf", "comp3211", "Tang Wai Kin", "Male", 22, "fina");
        Student student3 = new Student("rdgao", "comp3311", "GAO, Richard Daniel", "Male", 21, "cse");
        SystemDatabase.registerStudent(student1);
        SystemDatabase.registerStudent(student2);
        SystemDatabase.registerStudent(student3);

        Teacher teacher1 = new Teacher("whwma", "comp3111", "Ma Wai Him Wesley", "Male", 21, "econ", "TA");
        Teacher teacher2 = new Teacher("kwtleung", "comp3511", "Leung Wai Ting Kenneth", "Male", 18, "cse", "Assistant professor");
        SystemDatabase.registerTeacher(teacher1);
        SystemDatabase.registerTeacher(teacher2);

        Course course1 = new Course("COMP3111", "Software Engineering", "cse");
        Course course2 = new Course("COMP3511", "Operating Systems", "cse");
        Course course3 = new Course("COMP3711", "Design and Analysis of Algorithms", "cse");
        SystemDatabase.createCourse(course1);
        SystemDatabase.createCourse(course2);
        SystemDatabase.createCourse(course3);


    }
}