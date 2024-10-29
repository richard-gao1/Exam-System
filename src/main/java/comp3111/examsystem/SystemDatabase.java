package comp3111.examsystem;

import java.util.HashMap;

public class SystemDatabase {
    /*
    Used to store all important, shared information the system requires
     */
    // keep different account types separate
    // maps Username -> Password -> Instance of Account
    private HashMap<String,HashMap<String, Student>> studentLoginMap = new HashMap<>();
//    HashMap<String, HashMap<String, Teacher>> teacherLogin;
//    HashMap<String, HashMap<String, Manager>> managerLogin;


    public Student studentRegister(String username, String password, String name, String gender, int age, String department) {
        if (studentLoginMap.containsKey(username)) {
            // did not register the student
            return null;
        } else {
            Student student = new Student(username, password, name, gender, age, department);
            HashMap<String, Student> passwordStudent = new HashMap<>();
            passwordStudent.put(password, student);
            studentLoginMap.put(username, passwordStudent);
            return student;
        }
    }

    public HashMap<String, HashMap<String, Student>> getStudentRegistry() {
        return studentLoginMap;
    }

    public Student studentLogin(String username, String password) {
        if (studentLoginMap.containsKey(username)) {
            // right username case
            // will return null if wrong password
            return studentLoginMap.get(username).get(password);
        } else {
            // wrong username case
            return null;
        }
    }
}
