package comp3111.examsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Course {
    private String courseID;
    private String name;
    private Teacher teacher;
    private ArrayList<Student> students;
    private ArrayList<Exam> exams;

    public Course(String courseID, String name, ArrayList<Student> students, ArrayList<Exam> exams) {
        this.courseID = courseID;
        this.name = name;
        this.students = students;
        this.exams = exams;
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public String getCourseID() { return this.courseID; }
}
