package comp3111.examsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Course {
    private String courseID;
    private String name;
    private String department;
    private Teacher teacher;
    private ArrayList<Student> students;
    private ArrayList<Exam> exams;

    public Course(String courseID, String name, String department, ArrayList<Student> students, ArrayList<Exam> exams) {
        this.courseID = courseID;
        this.name = name;
        this.department = department;
        this.students = students;
        if (students != null) {
            for (Student student: students) {
                student.addCourse(this);
            }
        }
        this.exams = exams;
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public String getCourseID() { return this.courseID; }

    public String getCourseName() { return this.name; }

    public String getDepartment() { return this.department; }
}
