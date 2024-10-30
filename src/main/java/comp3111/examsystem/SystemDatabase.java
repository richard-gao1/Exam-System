package comp3111.examsystem;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SystemDatabase {
    /*
    Used to store all important, shared information the system requires
     */
    // keep different account types separate
    // maps Username -> Instance of Account
    HashMap<String, Student> students;
    HashMap<String, Teacher> teachers;
    HashMap<String, Manager> managers;

    public SystemDatabase() {}

    // perhaps login is done through the system.
    public Account login(String username, String password, AccountType type) throws IOException, ClassNotFoundException {
        Account account = null;
        switch (type) {
            case STUDENT:
                readStudents();
                account = students.get(username);
                break;
            case TEACHER:
                readTeachers();
                account = teachers.get(username);
                break;
            case MANAGER:
                readManagers();
                account = managers.get(username);
                break;
        }
        if (account == null || !Objects.equals(account.getPassword(), password)) return null;
        return account;
    }

    public void readStudents() throws IOException, ClassNotFoundException {
        students.clear();
        String[] user_list = getUsernameList("account/students.txt");
        for (String username : user_list) {
            FileInputStream fis = new FileInputStream("account/student/" + username + ".txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Student student = (Student) ois.readObject();
            ois.close();
            fis.close();
            students.put(username, student);
        }
    }

    public void readTeachers() throws IOException, ClassNotFoundException {
        teachers.clear();
        String[] user_list = getUsernameList("account/teachers.txt");
        for (String username : user_list) {
            FileInputStream fis = new FileInputStream("account/teacher/" + username + ".txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Teacher teacher = (Teacher) ois.readObject();
            ois.close();
            fis.close();
            teachers.put(username, teacher);
        }
    }

    public void readManagers() throws IOException, ClassNotFoundException {
        managers.clear();
        String[] user_list = getUsernameList("account/managers.txt");
        for (String username : user_list) {
            FileInputStream fis = new FileInputStream("account/manager/" + username + ".txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Manager manager = (Manager) ois.readObject();
            ois.close();
            fis.close();
            managers.put(username, manager);
        }
    }

    public List<Teacher> getTeacherList(Manager manager) {
        return teachers.values().stream().toList();
    }

    public List<Student> getStudentList(Manager manager) {
        return students.values().stream().toList();
    }

    /*
    * Helper function for getting the username list of students, teachers and managers.
    * The username list of students, teachers, and managers are stored in different .tmp
    * each username is separated by ';'
    * */
    private String[] getUsernameList(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream ois = new ObjectInputStream(fis);
        String list_str = (String) ois.readObject();
        ois.close();
        fis.close();
        return list_str.split(";");
    }

    private void writeUsernameList(AccountType type) throws IOException {
        List<String> username_list = new ArrayList<>();
        String filename = switch (type) {
            case STUDENT -> {
                username_list = students.keySet().stream().toList();
                yield "account/students.tmp";
            }
            case TEACHER -> {
                username_list = teachers.keySet().stream().toList();
                yield "account/teachers.tmp";
            }
            case MANAGER -> {
                username_list = managers.keySet().stream().toList();
                yield "account/managers.tmp";
            }
        };
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(String.join(";", username_list));
        oos.close();
        fos.close();
    }

    /*
    * Function for updating (add, modify) student information
    * */
    private void updateStudent(Student student, String old_username) throws IOException, ClassNotFoundException {
        readStudents();
        if (students.get(old_username) != null) {
            students.remove(old_username);
            new File("account/student/" + old_username + ".txt").delete();
        }
        String username = student.getUsername();
        students.put(username, student);
        FileOutputStream fos = new FileOutputStream("account/student/" + username + ".txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(student);
        oos.close();
        fos.close();
        writeUsernameList(AccountType.STUDENT);
    }

    /*
     * Function for updating (add, modify) teacher information
     * */
    private void updateTeacher(Teacher teacher, String old_username) throws IOException, ClassNotFoundException {
        readTeachers();
        if (teachers.get(old_username) != null) {
            teachers.remove(old_username);
            new File("account/teacher/" + old_username + ".txt").delete();
        }
        String username = teacher.getUsername();
        teachers.put(username, teacher);
        FileOutputStream fos = new FileOutputStream("account/teacher/" + username + ".txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(teacher);
        oos.close();
        fos.close();
        writeUsernameList(AccountType.TEACHER);
    }

    /*
    * Function for removing student from database
    * */
    private void removeStudent(String username) throws IOException {
        if (students.get(username) != null) {
            students.remove(username);
            new File("account/student/" + username + ".txt").delete();
        }
        writeUsernameList(AccountType.STUDENT);
    }

    /*
     * Function for removing teacher from database
     * */
    private void removeTeacher(String username) throws IOException {
        if (students.get(username) != null) {
            students.remove(username);
            new File("account/teacher/" + username + ".txt").delete();
        }
        writeUsernameList(AccountType.TEACHER);
    }

    /*
    * Called for register / add a student / teacher
    * */
    public Student registerStudent(Student student) throws IOException, ClassNotFoundException {
        String username = student.getUsername();
        if (students.get(username) != null) {
            // teacher with this username already exists
            return null;
        }
        updateStudent(student, "");
        return student;
    }

    public Teacher registerTeacher(Teacher teacher) throws IOException, ClassNotFoundException {
        String username = teacher.getUsername();
        if (teachers.get(username) != null) {
            // teacher with this username already exists
            return null;
        }
        updateTeacher(teacher, "");
        return teacher;
    }
}
