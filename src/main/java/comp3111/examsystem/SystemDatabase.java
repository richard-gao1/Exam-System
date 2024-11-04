package comp3111.examsystem;

import java.util.HashMap;

public class SystemDatabase {
    /*
    Used to store all important, shared information the system requires
     */
    // keep different account types separate
    // maps Username -> Password -> Instance of Account

    HashMap<String, HashMap<String, Student>> studentLogin;
    HashMap<String, HashMap<String, Teacher>> teacherLogin;
    HashMap<String, HashMap<String, Manager>> managerLogin;

    public Student studentRegister(String username, String password, String name, String gender, int age, String department) {
        if (studentLogin.containsKey(username)) {
            // did not register the student
            return null;
        } else {
            Student student = new Student(username, password, name, gender, age, department);
            HashMap<String, Student> passwordStudent = new HashMap<>();
            passwordStudent.put(password, student);
            studentLogin.put(username, passwordStudent);
            return student;
        }
    }

    public HashMap<String, HashMap<String, Student>> getStudentRegistry() {
        return this.studentLogin;
    }

    public Student studentLogin(String username, String password) {
        if (this.studentLogin.containsKey(username)) {
            // right username case
            // will return null if wrong password
            return this.studentLogin.get(username).get(password);
        } else {
            // wrong username case
            return null;
        }
    }
}
