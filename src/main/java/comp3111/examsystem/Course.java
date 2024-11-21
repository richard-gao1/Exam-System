package comp3111.examsystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Course which has an ID, name, department, a list of students,
 * and exams. The class manages relationships between courses, teachers, and students.
 * @author WaiKinTANG
 */
public class Course {
    private String courseID;
    private String name;
    private String department;

    // Store teacher's username instead of Teacher object
    private String teacherUsername;

    // Store student usernames instead of Student objects
    private ArrayList<String> studentUsernames = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> studentToGrade = new HashMap<>();
    private ArrayList<Exam> exams;

    /**
     * Constructs a Course with all attributes including teacher.
     *
     * @param courseID The ID of the course.
     * @param name The name of the course.
     * @param department The department offering the course.
     * @param teacher A Teacher object representing the instructor of the course.
     * @param students A list of Student objects enrolled in the course.
     * @param exams A list of Exam objects associated with the course.
     */
    public Course(String courseID, String name, String department, Teacher teacher, ArrayList<Student> students, ArrayList<Exam> exams) {
        this.courseID = courseID.replace(" ", "").trim().toUpperCase();
        this.name = name;
        this.department = department;

        // Store student usernames
        if (students != null) {
            for (Student student : students) {
                this.studentUsernames.add(student.getUsername());
                student.addCourse(this);
                studentToGrade.put(student.getUsername(), new HashMap<>());
            }
        }

        this.exams = exams == null ? new ArrayList<>() : exams;
        if (teacher != null) {
            this.teacherUsername = teacher.getUsername();
            teacher.addCourse(this);
        } else {
            this.teacherUsername = null;
        }
    }

    /**
     * Constructs a Course with ID, name, department, students.
     *
     * @param courseID The ID of the course.
     * @param name The name of the course.
     * @param department The department offering the course.
     * @param students A list of Student objects enrolled in the course.
     * @param exams A list of Exam objects associated with the course.
     */
    public Course(String courseID, String name, String department, ArrayList<Student> students, ArrayList<Exam> exams) {
        this(courseID, name, department, null, students, exams);
    }
    /**
     * Constructs a Course with ID, name, department, teacher, and students.
     *
     * @param courseID The ID of the course.
     * @param name The name of the course.
     * @param department The department offering the course.
     * @param teacher A Teacher object representing the instructor of the course.
     * @param students A list of Student objects enrolled in the course.
     */
    public Course(String courseID, String name, String department, Teacher teacher, ArrayList<Student> students) {
        this(courseID, name, department, teacher, students, null);
    }

    public Course(String courseID, String name, String department, ArrayList<Student> students) {
        this(courseID, name, department, null, students, null);
    }

    public Course(String courseID, String name, String department, Teacher teacher) {
        this(courseID, name, department, teacher, null);
    }

    /**
     * Retrieves the name of the course.
     *
     * @return The name of the course.
     */
    public String getCourseName() {
        return name;
    }

    /**
     * Retrieves the ID of the course.
     *
     * @return The ID of the course.
     */
    public String getCourseID() {
        return courseID;
    }

    /**
     * Retrieves the department offering the course.
     *
     * @return The department offering the course.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Updates the department of the course and notifies the database.
     *
     * @param department The new department for the course.
     */
    public void setDepartment(String department) {
        this.department = department;
        SystemDatabase.modifyCourse(this, courseID);
    }

    /**
     * Updates the name of the course and notifies the database.
     *
     * @param name The new name for the course.
     */
    public void setName(String name) {
        this.name = name;
        SystemDatabase.modifyCourse(this, courseID);
    }

    /**
     * Adds a student to the course if they are not already enrolled,
     * establishes bidirectional relationship, initializes their grades,
     * and updates the database.
     *
     * @param student The Student object to be added.
     * @throws IllegalArgumentException If the student is already enrolled in the course.
     */
    public void addStudent(Student student) {
        // Check for existing enrollment
        if (studentUsernames.contains(student.getUsername())) {
            throw new IllegalArgumentException("Already have this student");
        }

        // Add student's username to list
        studentUsernames.add(student.getUsername());

        // Establish bidirectional relationship with the Student object
        student.addCourse(this);

        // Initialize grades for the newly added student
        studentToGrade.put(student.getUsername(), new HashMap<>());

        // Notify database of modification
        SystemDatabase.modifyCourse(this, courseID);
    }

    /**
     * Retrieves a list of all students enrolled in the course.
     *
     * @return A List containing Student objects representing all students currently enrolled in
    the course.
     */
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        for (String username : studentUsernames) {
            Student student = SystemDatabase.getStudent(username);
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }

    /**
     * Removes a student from the course if they are enrolled,
     * breaks the bidirectional relationship with the Student object,
     * removes their grades, and updates the database.
     *
     * @param student The Student object to be removed.
     * @throws IllegalArgumentException If no such student is found in the course.
     */
    public void dropStudent(Student student) {
        if (studentUsernames.contains(student.getUsername())) {
            studentUsernames.remove(student.getUsername());
            student.dropCourse(this);
            studentToGrade.remove(student.getUsername());
            SystemDatabase.modifyCourse(this, courseID);
        } else {
            throw new IllegalArgumentException("No such student");
        }
    }

    /**
     * Assigns a teacher to the course. If an existing teacher is already assigned,
     * they are removed from this course before assigning the new one.
     * Updates the database after any changes.
     *
     * @param teacher The Teacher object to be assigned, or null if no teacher should be assigned.
     */
    public void setTeacher(Teacher teacher) {
        if (this.teacherUsername != null) {
            Teacher existingTeacher = SystemDatabase.getTeacher(this.teacherUsername);
            if (existingTeacher != null) {
                existingTeacher.dropCourse(courseID);
            }
        }
        if (teacher != null) {
            this.teacherUsername = teacher.getUsername();
            teacher.addCourse(courseID);
        } else {
            this.teacherUsername = null;
        }
        SystemDatabase.modifyCourse(this, courseID);
    }

    /**
     * Retrieves the Teacher assigned to the course.
     *
     * @return The Teacher object if a teacher is assigned; otherwise, null.
     */
    public Teacher getTeacher() {
        return teacherUsername != null ? SystemDatabase.getTeacher(teacherUsername) : null;
    }

    /**
     * Adds an Exam to the course. Ensures that the exam belongs to this course,
     * and that no other exam with the same name exists in this course.
     * Initializes grades for all students related to this new exam.
     * Updates the database after adding the exam.
     *
     * @param exam The Exam object to be added.
     * @throws IllegalArgumentException If the exam does not belong to this course
     *                                  or if an exam with the same name already exists.
     */
    public void addExam(Exam exam) {
        if (!exam.getCourse().getCourseID().equals(this.courseID)) {
            throw new IllegalArgumentException("Exam is not in this course");
        }
        for (Exam e : exams) {
            if (e.getExamName().equals(exam.getExamName())) {
                throw new IllegalArgumentException("Already have this exam");
            }
        }
        exams.add(exam);
        for (String studentUsername : studentUsernames) {
            studentToGrade.get(studentUsername).put(exam.getExamName(), null);
        }
        SystemDatabase.modifyCourse(this, courseID);
    }

    /**
     * Updates an existing exam within the course. If the new Exam object is provided,
     * it replaces the old one with the same name and may change the associated course.
     *
     * @param examName The name of the exam to be updated.
     * @param exam     The new Exam object that will replace the existing one, or null if updating
    without a new instance.
     */
    public void updateExam(String examName, Exam exam) {
        if (exam == null) {
            return;
        }
        dropExam(examName); // Drop original
        exam.getCourse().addExam(exam); // Can change the course bound to the exam
    }

    /**
     * Drops an exam from the course by Exam object.
     *
     * @param exam The Exam object to be removed.
     */
    public void dropExam(Exam exam) {
        dropExam(exam.getExamName());
    }

    /**
     * Removes an Exam from the course by its name.
     * Deletes the associated grades for all students in this course,
     * and updates the database after removal.
     *
     * @param examName The name of the Exam to be removed.
     * @throws IllegalArgumentException If no such exam exists within the course.
     */
    public void dropExam(String examName) {
        for (Exam exam : exams) {
            if (exam.getExamName().equals(examName)) {
                exams.remove(exam);
                for (String studentUsername : studentUsernames) {
                    studentToGrade.get(studentUsername).remove(examName);
                }
                SystemDatabase.modifyCourse(this, courseID);
                return;
            }
        }
        throw new IllegalArgumentException("No such exam");
    }

    /**
     * Constructs a new Course object with the specified ID, name, and department.
     * Initializes lists to hold student usernames and exams associated with this course.
     *
     * @param courseID   The unique identifier for the course.
     * @param name       The name of the course.
     * @param department The department offering the course.
     */
    public Course(String courseID, String name, String department) {
        this.courseID = courseID;
        this.name = name;
        this.department = department;
        this.studentUsernames = new ArrayList<>();
        this.exams = new ArrayList<>();
    }

    /**
     * Retrieves all exams associated with this course.
     *
     * @return An ArrayList of Exam objects representing the exams in this course.
     */
    public ArrayList<Exam> getExams() {
        return exams;
    }

    /**
     * Updates the details of the course, such as its ID, name, and department,
     * and returns the updated Course object for method chaining if necessary.
     *
     * @param courseID   The new unique identifier for the course.
     * @param name       The new name of the course.
     * @param department The new department offering the course.
     * @return This Course object after updating its details.
     */
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
