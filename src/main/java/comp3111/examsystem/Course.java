package comp3111.examsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Course implements Serializable {
    private String courseID;
    private String name;
    private Teacher teacher;
    private ArrayList<Student> students;
    private HashMap<Student, HashMap<String, Integer>> studentToGrade = new HashMap<>();
    private ArrayList<Exam> exams;
    public Course(String courseID, String name, ArrayList<Student> students, ArrayList<Exam> exams) {
        this.courseID = courseID;
        this.name = name;
        this.students = students;
        this.exams = exams;
    }

    public String getCourseID() { return this.courseID; }
}
