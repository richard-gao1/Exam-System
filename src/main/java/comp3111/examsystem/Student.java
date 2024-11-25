package comp3111.examsystem;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A class representing a student, which extends the User class.
 */
public class Student extends User {
    private ArrayList<String> courseIDs; // = new ArrayList<>(); // Stores the IDs of the courses the student is enrolled in

    /**
     * Constructs a new Student object with the provided details.
     *
     * @param username The username for the student's account.
     * @param password The password for the student's account.
     * @param name The name of the student.
     * @param gender The gender of the student.
     * @param age The age of the student.
     * @param department The department associated with the student.
     */
    public Student(String username, String password, String name, String gender, int age, String department) {
        super(username, password, name, gender, age, department);
        this.courseIDs = new ArrayList<>();
    }

    /**
     * Retrieves the list of courseIDs of courses that the student is currently enrolled in.
     *
     * @return An ArrayList containing all the courseIDs of the courses the student is enrolled in.
     */
    public ArrayList<String> getCourseIDs() {
        return courseIDs;
    }

    /**
     * Retrieves a list of Course objects that the student is currently enrolled in.
     *
     * @return An ArrayList containing all the courses the student is enrolled in.
     */
    public ArrayList<Course> getCourses() {
        ArrayList<Course> courses = new ArrayList<>();
        for (String courseID : courseIDs) {
            Course course = SystemDatabase.getCourse(courseID);
            if (course != null) {
                courses.add(course);
            }
        }
        return courses;
    }

    /**
     * Adds a course to the student's list of enrolled courses.
     *
     * @param course The Course object to be added.
     */
    public void addCourse(Course course) {
        // Only call this method via Course class
        if (!this.courseIDs.contains(course.getCourseID())) {
            this.courseIDs.add(course.getCourseID());
        } else {
            throw new IllegalArgumentException("Already enrolled in this course");
        }
    }

    /**
     * Removes a course from the student's list of enrolled courses.
     *
     * @param course The Course object to be removed.
     */
    public void dropCourse(Course course) {
        // Only call this method via Course class
        if (courseIDs.contains(course.getCourseID())) {
            courseIDs.remove(course.getCourseID());
        } else {
            throw new IllegalArgumentException("No such course");
        }
    }

    public ArrayList<Exam> getExams(){
        ArrayList<Exam> examArrayList = new ArrayList<>();
        for (String courseID : courseIDs){
            Course course = SystemDatabase.getCourse(courseID);
            if (course != null){
                examArrayList.addAll(course.getExams());
            }
        }
        return examArrayList;
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }

    /**
     * Updates the details of a Student account.
     *
     * @param username   The new username to set.
     * @param password   The new password to set.
     * @param name       The new name to set.
     * @param gender     The new gender to set.
     * @param age        The new age to set.
     * @param department The new department to set.
     * @return This Student instance after updating the details.
     */
    public Student update(String username, String password, String name, String gender, int age, String department) {
        super.update(username, password, name, gender, age, department);
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
