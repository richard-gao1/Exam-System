package comp3111.examsystem;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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
    HashMap<String, Student> students = new HashMap<>();
    HashMap<String, Teacher> teachers = new HashMap<>();
    HashMap<String, Manager> managers = new HashMap<>();

    HashMap<String, Course> courses = new HashMap<>();

    final String data_filetype = ".json";

    private void errorMessage(String msg) {
        System.out.println(msg);
    }

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
        createFolder("data/");
        createFolder("data/account");
        createFolder("data/account/student");
        createFolder("data/account/teacher");
        createFolder("data/account/manager");
        createFolder("data/course");

        // create files if not exist
        createFile("data/account/students" + data_filetype);
        createFile("data/account/teachers" + data_filetype);
        createFile("data/account/managers" + data_filetype);
        createFile("data/courses" + data_filetype);

        // create Manager
        Manager manager = new Manager("admin", "comp3111");
        try {registerManager(manager);} catch (IOException e) { System.out.println(e); }
        try {
            readAccounts(AccountType.STUDENT);
            readAccounts(AccountType.TEACHER);
            readCourses();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
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
        return "data/account/" + folder + data_filetype;
    }

    private String getAccountFilePath(String username, AccountType type) {
        if (username.isEmpty()) return "";
        String folder = switch (type) {
            case STUDENT -> "student";
            case TEACHER -> "teacher";
            case MANAGER -> "manager";
        };
        return "data/account/" + folder + "/" + username + data_filetype;
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

    public String studentToJsonString(Student student) {
        return new Gson().toJson(student);
    }

    public Student jsonStringToStudent(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Student.class);
    }

    public Teacher jsonStringToTeacher(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Teacher.class);
    }

    public Manager jsonStringToManager(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Manager.class);
    }

    public Course jsonStringToCourse(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Course.class);
    }

    public Course getCourse(String courseID) {
        String filepath = "data/course/" + courseID + data_filetype;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filepath);
            byte[] content = fis.readAllBytes();
            if (content.length == 0) return null;
            String text = new String(content, StandardCharsets.UTF_8);
            return jsonStringToCourse(text);
        } catch (IOException e) {

        }
        return null;
    }

    private void readAccounts(AccountType type) {
        switch (type) {
            case STUDENT -> students.clear();
            case TEACHER -> teachers.clear();
            case MANAGER -> managers.clear();
        }
        String[] user_list = getUsernameList(type);
        if (user_list == null) return;
        for (String username : user_list) {
            String filepath = getAccountFilePath(username, type);
            try {
                FileInputStream fis = new FileInputStream(filepath);
                byte[] content = fis.readAllBytes();
                if (content.length == 0) continue;
                String text = new String(content, StandardCharsets.UTF_8);
                switch (type) {
                    case STUDENT:
                        Student student = jsonStringToStudent(text);
                        students.put(username, student);
                        break;
                    case TEACHER:
                        Teacher teacher = jsonStringToTeacher(text);
                        teachers.put(username, teacher);
                        break;
                    case MANAGER:
                        Manager manager = jsonStringToManager(text);
                        managers.put(username, manager);
                        break;
                }
                fis.close();
            } catch (IOException e) {

            }
        }
    }

    private String[] getCourseList() throws IOException {
        String filename = "data/courses" + data_filetype;
        FileInputStream fis = new FileInputStream(filename);
        byte[] bytes = fis.readAllBytes();
        if (bytes.length == 0) {
            return null;
        }
        String list_str = new String(bytes, StandardCharsets.UTF_8);
        fis.close();
        return list_str.split(";");
    }

    private void readCourses() throws IOException, ClassNotFoundException {
        String[] course_list = getCourseList();
        if (course_list == null) return;
        for (String courseID : course_list) {
            String filepath = "data/course/" + courseID + data_filetype;
            FileInputStream fis = new FileInputStream(filepath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Course course = (Course) ois.readObject();
            courses.put(courseID, course);
            ois.close();
            fis.close();
        }
    }

    /*
    * Functions that filter students.
    * Leave empty for no filter
    * */
    public List<Student> getStudentList(Manager manager, String username, String name, String department) {
        return students.values().stream().filter(s ->
                s.getUsername().contains(username) &&
                        s.getName().contains(name) &&
                        s.getDepartment().contains(department)
        ).toList();
    }

    /*
     * Functions that filter teachers.
     * Leave empty for no filter
     * */
    public List<Teacher> getTeacherList(Manager manager, String username, String name, String department) {
        return teachers.values().stream().filter(t ->
                t.getUsername().contains(username) &&
                        t.getName().contains(name) &&
                        t.getDepartment().contains(department)
        ).toList();
    }

    public List<Course> getCourseList(String courseID, String courseName, String department) {
        return new ArrayList<>();
    }

    /*
    * Helper function for getting the username list of students, teachers and managers.
    * The username list of students, teachers, and managers are stored in different .tmp
    * each username is separated by ';'
    * */
    private String[] getUsernameList(AccountType type) {
        try {
            String filename = getNameListFilePath(type);
            FileInputStream fis = new FileInputStream(filename);
            byte[] bytes = fis.readAllBytes();
            if (bytes.length == 0) {
                return null;
            }
            String list_str = new String(bytes, StandardCharsets.UTF_8);
            fis.close();
            return list_str.split(";");
        } catch (IOException e) {

        }
        return null;
    }

    private void writeToUsernameList(AccountType type) {
        List<String> username_list;
        String filename = getNameListFilePath(type);
        username_list = switch (type) {
            case STUDENT -> students.keySet().stream().toList();
            case TEACHER -> teachers.keySet().stream().toList();
            case MANAGER -> managers.keySet().stream().toList();
        };
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            byte[] bytes = String.join(";", username_list).getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {

        }

    }

    private void writeToCourseList() {

    }

    private void writeJson(String filepath, String text) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            byte[] content = text.getBytes(StandardCharsets.UTF_8);
            fos.write(content);
            fos.close();
        } catch (IOException e) {

        }
    }

    private void writeToStudent(Student student) {
        String username = student.getUsername();
        String filepath = getAccountFilePath(username, AccountType.STUDENT);
        createFile(filepath);
        String text = new Gson().toJson(student);
        writeJson(filepath, text);
        students.put(username, student);
        writeToUsernameList(AccountType.STUDENT);
    }

    private void writeToTeacher(Teacher teacher) {
        String username = teacher.getUsername();
        String filepath = getAccountFilePath(username, AccountType.TEACHER);
        createFile(filepath);
        String text = new Gson().toJson(teacher);
        writeJson(filepath, text);
        teachers.put(username, teacher);
        writeToUsernameList(AccountType.TEACHER);
    }

    private void writeToManager(Manager manager) {
        String username = manager.getUsername();
        String filepath = getAccountFilePath(username, AccountType.MANAGER);
        createFile(filepath);
        String text = new Gson().toJson(manager);
        writeJson(filepath, text);
        managers.put(username, manager);
        writeToUsernameList(AccountType.MANAGER);
    }

    private void writeToCourse(Course course) {
        String courseID = course.getCourseID();
        String filepath = "data/course/" + courseID + data_filetype;
        String text = new Gson().toJson(course);
        writeJson(filepath, text);
        courses.put(courseID, course);
        writeToCourseList();
    }

    /*
    * Function for updating student information
    * */
    public void updateStudent(Student newStudent, String old_username, Manager manager) {
        if (manager == null) {
            System.out.println("Require Manager account");
            return;
        }
        readAccounts(AccountType.STUDENT);
        if (!Objects.equals(old_username, newStudent.getUsername())) removeStudent(old_username, manager);
        writeToStudent(newStudent);
    }

    /*
     * Function for updating teacher information
     * */
    public void updateTeacher(Teacher newTeacher, String old_username, Manager manager) {
        if (manager == null) {
            System.out.println("Require Manager account");
            return;
        }
        readAccounts(AccountType.TEACHER);
        if (!Objects.equals(old_username, newTeacher.getUsername())) removeTeacher(old_username, manager);
        writeToTeacher(newTeacher);
    }

    public void modifyCourse(Course newCourse, String old_courseID, Manager manager) throws IOException, ClassNotFoundException {
        if (manager == null) {
            System.out.println("Require Manager account");
            return;
        }
        readCourses();
        if (!Objects.equals(old_courseID, newCourse.getCourseID())) removeCourse(old_courseID, manager);
        writeToCourseList();
    }

    /*
    * Function for removing student from database
    * */
    public void removeStudent(String username, Manager manager) {
        if (manager == null) {
            System.out.println("Require Manager Account");
            return;
        }
        if (students.get(username) != null) {
            students.remove(username);
            removeFile(getAccountFilePath(username, AccountType.STUDENT));
        }
        writeToUsernameList(AccountType.STUDENT);
    }

    /*
     * Function for removing teacher from database
     * */
    public void removeTeacher(String username, Manager manager) {
        if (manager == null) {
            System.out.println("Require Manager Account");
            return;
        }
        if (teachers.get(username) != null) {
            teachers.remove(username);
            removeFile(getAccountFilePath(username, AccountType.TEACHER));
        }
        writeToUsernameList(AccountType.TEACHER);
    }

    public void removeCourse(String courseID, Manager manager) {
        if (manager == null) {
            System.out.println("Require Manager Account");
            return;
        }
        if (courses.get(courseID) != null) {
            courses.remove(courseID);
            removeFile("data/course/" + courseID + data_filetype);
        }
        writeToCourseList();
    }

    /*
    * Called for register / add a student / teacher
    * */
    public Student registerStudent(Student student) {
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

    private Manager registerManager(Manager manager) throws IOException {
        String username = manager.getUsername();
        if (managers.get(username) != null) {
            // teacher with this username already exists
            System.out.println("Manager username " + manager.getUsername() + " already exist");
            return null;
        }
        writeToManager(manager);
        return manager;
    }

    public Course createCourse(Course course) throws IOException {
        String courseID = course.getCourseID();
        if (courses.get(courseID) != null) {
            System.out.println("Course ID " + courseID + " already exist");
            return null;
        }
        writeToCourse(course);
        return course;
    }
}