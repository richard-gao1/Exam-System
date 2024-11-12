package comp3111.examsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Student extends User {
    private ArrayList<Course> courses = new ArrayList<>();

    public Student(String username, String password, String name, String gender, int age, String department){
        super(username, password, name, gender, age, department);
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }

    public void addCourse(Course course){
        // need to check for dups
        courses.add(course);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    public Student update(String username, String password, String name, String gender, int age, String department) {
        super.update(username, password, name, gender, age, department);
        return this;
    }
}
