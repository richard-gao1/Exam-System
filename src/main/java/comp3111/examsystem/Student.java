package comp3111.examsystem;

import java.util.ArrayList;

public class Student extends User {
    private ArrayList<Course> courses = new ArrayList<>();

    public Student(String username, String password, String name, String gender, int age, String department){
        super(username, password, name, gender, age, department);
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }

    public void addCourse(Course course){
        // only call this method via Course class
        if (!courses.contains(course)){
            courses.add(course);
        }
        else{
            throw new IllegalArgumentException("Already enrolled in this course");
        }
    }

    public ArrayList<Exam> getExams(){
        ArrayList<Exam> exams = new ArrayList<>();
        for (Course course : courses){
            exams.addAll(course.getExams());
        }
        return exams;
    }

    public void dropCourse(Course course){
        // only call this method via Course class
        if (courses.contains(course)){
            courses.remove(course);
        }
        else{
            throw new IllegalArgumentException("No such course");
        }
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
