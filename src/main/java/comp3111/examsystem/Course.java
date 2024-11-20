package comp3111.examsystem;

import java.util.ArrayList;
import java.util.HashMap;

public class Course {
    private String courseID;
    private String name;
    private String department;
    private Teacher teacher;
    private ArrayList<Student> students;
    private HashMap<Student, HashMap<String, Integer>> studentToGrade = new HashMap<>();
    private ArrayList<Exam> exams;
    public Course(String name, String department, ArrayList<Student> students, ArrayList<Exam> exams) {
        this(name, null,department, students, exams);
    }


    public Course() {
        this.courseID = "COMP3111";
        this.name = "Software Engineering";
        this.department = "cse";
        this.students = new ArrayList<>();
    }

    public Course(String courseID, String name, String department, Teacher teacher,ArrayList<Student> students, ArrayList<Exam> exams) {
        this.courseID = courseID;
        this.name = name;
        this.department = department;
        this.students = students;
        if (students != null) {
            for (Student student: students){
                student.addCourse(this);
            }
        }
        this.exams = exams;
        if (teacher != null){
            this.teacher = teacher;
            teacher.addCourse(this);
        }
        else{
            this.teacher = null;
        }
        for (Student student: students){
            studentToGrade.put(student,new HashMap<>());
        }
    }

    public Course(String name, String courseID, String department ,ArrayList<Student> students, ArrayList<Exam> exams) {
        this(name, courseID, department, null, students, exams);
    }

    public Course(String name, String courseID, String department, Teacher teacher, ArrayList<Student> students) {
        this(name, courseID, department , teacher, students,null);
    }


    public Course(String name, String courseID, String department, ArrayList<Student> students) {
        this(name, courseID, department, null, students, null);
    }

    public Course(String name, String courseID, String department, Teacher teacher) {
        this(name, courseID, department, teacher, null);
    }

    public Course(String name, String courseID, String department) {
        this(name, courseID, department,null, null, null);
    }

    public String getCourseName(){
        return name;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getDepartment(){
        return department;
    }

    public void setDepartment(String department){
        this.department= department;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addStudent(Student student){
        if (students.contains(student)){
            throw new IllegalArgumentException("Already have this student");
        }
        students.add(student);
        student.addCourse(this);
        studentToGrade.put(student,new HashMap<>());
    }

    public ArrayList<Student> getStudents(){
        return students;
    }

    public void dropStudent(Student student){
        if (students.contains(student)){
            students.remove(student);
            student.dropCourse(this);
            studentToGrade.remove(student);
        }
        else{
            throw new IllegalArgumentException("No such student");
        }
    }

    public void setTeacher(Teacher teacher){
        if (this.teacher != null){
            this.teacher.dropCourse(this.getCourseID());
        }
        if (teacher != null){
            this.teacher = teacher;
            teacher.addCourse(this);
        }
        else{
            this.teacher = null;
        }
    }

    public Teacher getTeacher(){
        return teacher;
    }

    public void addExam(Exam exam){
        if (!exam.getCourse().getCourseID().equals(this.courseID)){
            throw new IllegalArgumentException("Exam is not in this course");
        }
        for (Exam e : exams){
            if (e.getExamName().equals(exam.getExamName())){
                throw new IllegalArgumentException("Already have this exam");
            }
        }
        exams.add(exam);
        for (Student student: students){
            studentToGrade.get(student).put(exam.getExamName(), null);
        }
    }

    public void updateExam(String examName, Exam exam){
        // examName: Original examName, exam: Updated exam (with any attributes changed)
        if (exam == null){
            return;
        }
        dropExam(examName); // Drop original
        exam.getCourse().addExam(exam); // Can change the course bound to the exam
    }

    public void dropExam(Exam exam){
        dropExam(exam.getExamName());
    }

    public void dropExam(String examName){
        for (Exam exam: exams){
            if (exam.getExamName().equals(examName)){
                exams.remove(exam);
                for (Student student: students){
                    studentToGrade.get(student).remove(examName);
                }
                return;
            }
        }
        throw new IllegalArgumentException("No such exam");
    }

    public ArrayList<Exam> getExams(){
        return exams;
    }


}
