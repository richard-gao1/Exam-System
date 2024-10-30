package comp3111.examsystem;

import java.util.ArrayList;

public class Course {
    private String name;
//    private Teacher teacher;
    private ArrayList<Student> students;
    private ArrayList<Exam> exams;
    public Course(String name, ArrayList<Student> students, ArrayList<Exam> exams) {
        this.name = name;
        this.students = students;
        this.exams = exams;
    }
}
