package comp3111.examsystem;

import java.util.ArrayList;

import java.util.HashMap;

public class Course {
    private String name;
    private Teacher teacher;
    private ArrayList<Student> students;
    private HashMap<Student, HashMap<String, Integer>> studentToGrade = new HashMap<>();
    private ArrayList<Exam> exams;
    public Course(String name, ArrayList<Student> students, ArrayList<Exam> exams) {
        this(name, null, students, exams);
    }

    public Course(String name, Teacher teacher, ArrayList<Student> students, ArrayList<Exam> exams) {
        this.name = name;
        this.students = students;
        for (Student student: students){
            student.addCourse(this);
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

    public String getName(){
        return name;
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
            this.teacher.dropCourse(this);
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
        if (exam.getCourse() != this){
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
