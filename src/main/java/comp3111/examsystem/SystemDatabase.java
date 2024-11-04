package comp3111.examsystem;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

enum AccountType {
    STUDENT, TEACHER, MANAGER
}

public class SystemDatabase {
    /*
    Used to store all important, shared information the system requires
     */
    // keep different account types separate
    // maps Username -> Instance of Account
    HashMap<String, Student> students = new HashMap<>();
    HashMap<String, Teacher> teachers = new HashMap<>();
    HashMap<String, Manager> managers = new HashMap<>();

    final String data_filetype = ".txt";

    private boolean createFolder(String directory) {
        File folder = new File(directory);
        if (!folder.exists()) return folder.mkdir();
        return true;
    }

    private boolean createFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) return true;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    private boolean removeFile(String filepath) {
        File file = new File(filepath);
        return file.delete();
    }

    public SystemDatabase() {
        // create folders if not exist
        createFolder("account");
        createFolder("account/student");
        createFolder("account/teacher");
        createFolder("account/manager");

        // create files if not exist
        createFile("account/students" + data_filetype);
        createFile("account/teachers" + data_filetype);
        createFile("account/managers" + data_filetype);

        // create Manager
        Manager manager = new Manager("admin", "comp3111");
        try {registerManager(manager);} catch (IOException e) { System.out.println(e); };
    }

    private AccountType getAccountType(Account account) {
        if (account instanceof Student) return AccountType.STUDENT;
        if (account instanceof Teacher) return AccountType.TEACHER;
        return AccountType.MANAGER;
    }

    private String getNameListFilePath(AccountType type) {
        String folder = switch (type) {
            case STUDENT -> "students";
            case TEACHER -> "teachers";
            case MANAGER -> "managers";
        };
        return "account/" + folder + data_filetype;
    }

    private String getAccountFilePath(String username, AccountType type) {
        if (username.isEmpty()) return "";
        String folder = switch (type) {
            case STUDENT -> "student";
            case TEACHER -> "teacher";
            case MANAGER -> "manager";
        };
        return "account/" + folder + "/" + username + data_filetype;
    }

    // perhaps login is done through the system.
    public Account login(String username, String password, AccountType type) throws IOException, ClassNotFoundException {
        readAccounts(type);
        Account account = switch (type) {
            case STUDENT -> students.get(username);
            case TEACHER -> teachers.get(username);
            case MANAGER -> managers.get(username);
        };
        if (account == null || !Objects.equals(account.getPassword(), password)) return null;
        return account;
    }

    public void readAccounts(AccountType type) throws IOException, ClassNotFoundException {
        switch (type) {
            case STUDENT -> students.clear();
            case TEACHER -> teachers.clear();
            case MANAGER -> managers.clear();
        }
        String[] user_list = getUsernameList(type);
        for (String username : user_list) {
            String filepath = getAccountFilePath(username, type);
            FileInputStream fis = new FileInputStream(filepath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            switch (type) {
                case STUDENT:
                    Student student = (Student) ois.readObject();
                    students.put(username, student);
                    break;
                case TEACHER:
                    Teacher teacher = (Teacher) ois.readObject();
                    teachers.put(username, teacher);
                    break;
                case MANAGER:
                    Manager manager = (Manager) ois.readObject();
                    managers.put(username, manager);
                    break;
            }
            ois.close();
            fis.close();
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
    private String[] getUsernameList(AccountType type) throws IOException, ClassNotFoundException {
        String filename = getNameListFilePath(type);
        FileInputStream fis = new FileInputStream(filename);
        byte[] bytes = fis.readAllBytes();
        String list_str = new String(bytes, StandardCharsets.UTF_8);
        fis.close();
        return list_str.split(";");
    }

    private void writeToUsernameList(AccountType type) throws IOException {
        List<String> username_list;
        String filename = getNameListFilePath(type);
        username_list = switch (type) {
            case STUDENT -> students.keySet().stream().toList();
            case TEACHER -> teachers.keySet().stream().toList();
            case MANAGER -> managers.keySet().stream().toList();
        };
        FileOutputStream fos = new FileOutputStream(filename);
        byte[] bytes = String.join(";", username_list).getBytes(StandardCharsets.UTF_8);
        fos.write(bytes);
        fos.close();
    }

    private void writeToStudent(Student student) throws IOException {
        String username = student.getUsername();
        String filepath = getAccountFilePath(username, AccountType.STUDENT);
        students.put(username, student);
        FileOutputStream fos = new FileOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(student);
        oos.close();
        fos.close();
        writeToUsernameList(AccountType.STUDENT);
    }

    private void writeToTeacher(Teacher teacher) throws IOException {
        String username = teacher.getUsername();
        String filepath = getAccountFilePath(username, AccountType.TEACHER);
        teachers.put(username, teacher);
        createFile(filepath);
        FileOutputStream fos = new FileOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(teacher);
        oos.close();
        fos.close();
        writeToUsernameList(AccountType.TEACHER);
    }

    private void writeToManager(Manager manager) throws IOException {
        String username = manager.getUsername();
        String filepath = getAccountFilePath(username, AccountType.MANAGER);
        managers.put(username, manager);
        createFile(filepath);
        FileOutputStream fos = new FileOutputStream(filepath);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(manager);
        oos.close();
        fos.close();
        writeToUsernameList(AccountType.MANAGER);
    }

    /*
    * Function for updating student information
    * */
    private void updateStudent(Student student, String old_username) throws IOException, ClassNotFoundException {
        readAccounts(AccountType.STUDENT);
        if (!Objects.equals(old_username, student.getUsername())) removeStudent(old_username);
        writeToStudent(student);
    }

    /*
     * Function for updating teacher information
     * */
    private void updateTeacher(Teacher teacher, String old_username) throws IOException, ClassNotFoundException {
        readAccounts(AccountType.TEACHER);
        if (!Objects.equals(old_username, teacher.getUsername())) removeTeacher(old_username);
        writeToTeacher(teacher);
    }

    /*
    * Function for removing student from database
    * */
    private void removeStudent(String username) throws IOException {
        if (students.get(username) != null) {
            students.remove(username);
            removeFile("account/student/" + username + data_filetype);
        }
        writeToUsernameList(AccountType.STUDENT);
    }

    /*
     * Function for removing teacher from database
     * */
    private void removeTeacher(String username) throws IOException {
        if (teachers.get(username) != null) {
            teachers.remove(username);
            removeFile("account/teacher/" + username + data_filetype);
        }
        writeToUsernameList(AccountType.TEACHER);
    }

    /*
    * Called for register / add a student / teacher
    * */
    public Student registerStudent(Student student) throws IOException {
        String username = student.getUsername();
        if (students.get(username) != null) {
            // teacher with this username already exists
            System.out.println("Student username " + student.getUsername() + " already exist");
            return null;
        }
        writeToStudent(student);
        return student;
    }

    public Teacher registerTeacher(Teacher teacher) throws IOException {
        String username = teacher.getUsername();
        if (teachers.get(username) != null) {
            // teacher with this username already exists
            System.out.println("Teacher username " + teacher.getUsername() + " already exist");
            return null;
        }
        writeToTeacher(teacher);
        return teacher;
    }

    public Manager registerManager(Manager manager) throws IOException {
        String username = manager.getUsername();
        if (managers.get(username) != null) {
            // teacher with this username already exists
            System.out.println("Teacher username " + manager.getUsername() + " already exist");
            return null;
        }
        writeToManager(manager);
        return manager;
    }
}
