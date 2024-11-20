package comp3111.examsystem;

import java.util.ArrayList;

/**
 * A class representing a student, which extends the User class.
 */
public class Student extends User {
    // Store courseIDs instead of Course objects
    private ArrayList<String> courseIDs = new ArrayList<>();

    public Student(String username, String password, String name, String gender, int age, String department) {
        super(username, password, name, gender, age, department);
    }

    // Getter: Retrieve Course objects dynamically
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

    // Add course by course object
    public void addCourse(Course course) {
        // Only call this method via Course class
        if (!courseIDs.contains(course.getCourseID())) {
            courseIDs.add(course.getCourseID());
        } else {
            throw new IllegalArgumentException("Already enrolled in this course");
        }
    }

    // Drop course by course object
    public void dropCourse(Course course) {
        // Only call this method via Course class
        if (courseIDs.contains(course.getCourseID())) {
            courseIDs.remove(course.getCourseID());
        } else {
            throw new IllegalArgumentException("No such course");
        }
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
}
