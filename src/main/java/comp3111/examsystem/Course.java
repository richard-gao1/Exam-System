package comp3111.examsystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

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

    public Course(String courseID, String name, String department) {
        this.courseID = courseID;
        this.name = name;
        this.department = department;
        this.students = new ArrayList<>();
        this.exams = new ArrayList<>();
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public String getCourseID() { return this.courseID; }

    public String getCourseName() { return this.name; }

    public String getDepartment() { return this.department; }

    public Course update(String courseID, String name, String department) {
        this.courseID = courseID;
        this.name = name;
        this.department = department;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        return Objects.equals(this.courseID, ((Course) obj).getCourseID()) &&
                Objects.equals(this.name, ((Course) obj).getCourseName()) &&
                Objects.equals(this.department, ((Course) obj).getDepartment());
    }
}
