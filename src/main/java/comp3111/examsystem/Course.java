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

    // Store teacher's username instead of Teacher object
    private String teacherUsername;

    // Store student usernames instead of Student objects
    private ArrayList<String> studentUsernames = new ArrayList<>();
    private HashMap<String, HashMap<String, Integer>> studentToGrade = new HashMap<>();
    private ArrayList<Exam> exams;

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
        SystemDatabase.createCourse(this);
    }

    public Course(String courseID, String name, String department, ArrayList<Student> students, ArrayList<Exam> exams) {
        this(courseID, name, department, null, students, exams);
    }

    public Course(String courseID, String name, String department, Teacher teacher, ArrayList<Student> students) {
        this(courseID, name, department, teacher, students, null);
    }

    public Course(String courseID, String name, String department, ArrayList<Student> students) {
        this(courseID, name, department, null, students, null);
    }

    public Course(String courseID, String name, String department, Teacher teacher) {
        this(courseID, name, department, teacher, null);
    }

    public String getCourseName() {
        return name;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
        SystemDatabase.modifyCourse(this, courseID);
    }

    public void setName(String name) {
        this.name = name;
        SystemDatabase.modifyCourse(this, courseID);
    }

    public void addStudent(Student student) {
        if (studentUsernames.contains(student.getUsername())) {
            throw new IllegalArgumentException("Already have this student");
        }
        studentUsernames.add(student.getUsername());
        student.addCourse(this);
        studentToGrade.put(student.getUsername(), new HashMap<>());
        SystemDatabase.modifyCourse(this, courseID);
    }

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

    public void setTeacher(Teacher teacher) {
        if (this.teacherUsername != null) {
            Teacher existingTeacher = SystemDatabase.getTeacher(this.teacherUsername);
            if (existingTeacher != null) {
                existingTeacher.dropCourse(this);
            }
        }
        if (teacher != null) {
            this.teacherUsername = teacher.getUsername();
            teacher.addCourse(this);
        } else {
            this.teacherUsername = null;
        }
        SystemDatabase.modifyCourse(this, courseID);
    }

    public Teacher getTeacher() {
        return teacherUsername != null ? SystemDatabase.getTeacher(teacherUsername) : null;
    }

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

    public void updateExam(String examName, Exam exam) {
        if (exam == null) {
            return;
        }
        dropExam(examName); // Drop original
        exam.getCourse().addExam(exam); // Can change the course bound to the exam
    }

    public void dropExam(Exam exam) {
        dropExam(exam.getExamName());
    }

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

    public Course(String courseID, String name, String department) {
        this.courseID = courseID;
        this.name = name;
        this.department = department;
        this.studentUsernames = new ArrayList<>();
        this.exams = new ArrayList<>();
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

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
