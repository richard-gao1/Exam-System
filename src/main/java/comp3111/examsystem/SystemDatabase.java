package comp3111.examsystem;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class SystemDatabase {
    /*
    Used to store all important, shared information the system requires
     */
    // keep different account types separate

    // maps Username -> Instance of Account
    static final String data_filetype = ".json";
    public static User currentUser;

    private boolean createFolder(String directory) {
        File folder = new File(directory);
        if (!folder.exists()) return folder.mkdir();
        return true;
    }

    private static boolean createFile(String filepath) {
        File file = new File(filepath);
        if (file.exists()) return true;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    private static boolean removeFile(String filepath) {
        File file = new File(filepath);
        return file.delete();
    }

    public static void removeAll() {
        try {
            FileUtils.deleteDirectory(new File("data"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        registerManager(manager);
    }

    private static String getNameListFilePath(AccountType type) {
        String folder = switch (type) {
            case STUDENT -> "students";
            case TEACHER -> "teachers";
            case MANAGER -> "managers";
        };
        return "data/account/" + folder + data_filetype;
    }

    private static String getAccountFilePath(String username, AccountType type) {
        if (username.isEmpty()) return "";
        String folder = switch (type) {
            case STUDENT -> "student";
            case TEACHER -> "teacher";
            case MANAGER -> "manager";
        };
        return "data/account/" + folder + "/" + username + data_filetype;
    }

    // perhaps login is done through the system.
    public static Account login(String username, String password, AccountType type) {
        Account account = switch (type) {
            case STUDENT -> getStudent(username);
            case TEACHER -> getTeacher(username);
            case MANAGER -> getManager(username);
        };
        if (account == null || !account.login(password)) return null;
        return account;
    }

    public static String studentToJsonString(Student student) {
        return new Gson().toJson(student);
    }

    public static Student jsonStringToStudent(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Student.class);
    }

    public static Teacher jsonStringToTeacher(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Teacher.class);
    }

    public static Manager jsonStringToManager(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Manager.class);
    }

    public static Course jsonStringToCourse(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Course.class);
    }

    public static Course getCourse(String courseID) {
        courseID = courseID.replace(" ", "");
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

    public static Student getStudent(String username) {
        String filepath = getAccountFilePath(username, AccountType.STUDENT);
        try {
            FileInputStream fis = new FileInputStream(filepath);
            byte[] content = fis.readAllBytes();
            if (content.length == 0) return null;
            String text = new String(content, StandardCharsets.UTF_8);
            Student student = jsonStringToStudent(text);
            fis.close();
            return student;
        } catch (IOException e) {
            return null;
        }
    }

    public static Teacher getTeacher(String username) {
        String filepath = getAccountFilePath(username, AccountType.TEACHER);
        try {
            FileInputStream fis = new FileInputStream(filepath);
            byte[] content = fis.readAllBytes();
            if (content.length == 0) return null;
            String text = new String(content, StandardCharsets.UTF_8);
            Teacher teacher = jsonStringToTeacher(text);
            fis.close();
            return teacher;
        } catch (IOException e) {
            return null;
        }
    }

    public static Manager getManager(String username) {
        String filepath = getAccountFilePath(username, AccountType.MANAGER);
        try {
            FileInputStream fis = new FileInputStream(filepath);
            byte[] content = fis.readAllBytes();
            if (content.length == 0) return null;
            String text = new String(content, StandardCharsets.UTF_8);
            Manager manager = jsonStringToManager(text);
            fis.close();
            return manager;
        } catch (IOException e) {
            return null;
        }
    }

    private static ArrayList<String> getCourseIDArray() {
        String filename = "data/courses" + data_filetype;
        try {
            FileInputStream fis = new FileInputStream(filename);
            byte[] bytes = fis.readAllBytes();
            if (bytes.length == 0) {
                return null;
            }
            String list_str = new String(bytes, StandardCharsets.UTF_8);
            fis.close();
            return new Gson().fromJson(list_str, ArrayList.class);
        } catch (IOException e) {
            return null;
        }
    }

    /*
    * Functions that filter students.
    * Leave empty for no filter
    * */
    public static ArrayList<Student> getStudentList(String usernameFilter, String nameFilter, String departmentFilter) {
        ArrayList<Student> studentList = new ArrayList<>();
        ArrayList<String> username_array = getUsernameArray(AccountType.STUDENT);
        Student student;
        if (username_array == null) return studentList;
        for (String username : username_array) {
            if ((student = getStudent(username)) != null) {
                studentList.add(student);
            }
        }
        return studentList.stream().filter(s ->
                s.getUsername().contains(usernameFilter) &&
                        s.getName().contains(nameFilter) &&
                        s.getDepartment().contains(departmentFilter)
        ).collect(Collectors.toCollection(ArrayList::new));

    }

    /*
     * Functions that filter teachers.
     * Leave empty for no filter
     * */
    public static ArrayList<Teacher> getTeacherList(String usernameFilter, String nameFilter, String departmentFilter) {
        ArrayList<Teacher> teacherList = new ArrayList<>();
        ArrayList<String> username_array = getUsernameArray(AccountType.TEACHER);
        Teacher teacher;
        if (username_array == null) return teacherList;
        for (String username : username_array) {
            if ((teacher = getTeacher(username)) != null) {
                teacherList.add(teacher);
            }
        }
        return teacherList.stream().filter(t ->
                t.getUsername().contains(usernameFilter) &&
                        t.getName().contains(nameFilter) &&
                        t.getDepartment().contains(departmentFilter)
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Course> getCourseList(String courseIDFilter, String courseNameFilter, String departmentFilter) {
        ArrayList<Course> courseList = new ArrayList<>();
        ArrayList<String> courseID_array = getCourseIDArray();
        Course course;
        if (courseID_array == null) return courseList;
        for (String courseID : courseID_array) {
            if ((course = getCourse(courseID)) != null) {
                courseList.add(course);
            }
        }
        return courseList.stream().filter(c ->
                c.getCourseID().contains(courseIDFilter) &&
                        c.getCourseName().contains(courseNameFilter) &&
                        c.getDepartment().contains(departmentFilter)
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    /*
    * Helper function for getting the username list of students, teachers and managers.
    * The username list of students, teachers, and managers are stored in different .tmp
    * each username is separated by ';'
    * */
    private static ArrayList<String> getUsernameArray(AccountType type) {
        try {
            String filename = getNameListFilePath(type);
            FileInputStream fis = new FileInputStream(filename);
            byte[] bytes = fis.readAllBytes();
            if (bytes.length == 0) {
                return new ArrayList<>();
            }
            String list_str = new String(bytes, StandardCharsets.UTF_8);
            fis.close();
            return new Gson().fromJson(list_str, ArrayList.class);
        } catch (IOException e) {

        }
        return new ArrayList<>();
    }

    private static void writeUsernameFile(ArrayList<String> username_array, AccountType type) {
        String filename = getNameListFilePath(type);
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            String text = new Gson().toJson(username_array);
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {

        }

    }

    private static void writeCourseIDFile(ArrayList<String> course_array) {
        String filename = "data/courses" + data_filetype;
        try {
            FileOutputStream fos = new FileOutputStream(filename);
            String text = new Gson().toJson(course_array);
            byte[] bytes = text.getBytes();
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {

        }
    }

    private static void writeJson(String filepath, String text) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            byte[] content = text.getBytes(StandardCharsets.UTF_8);
            fos.write(content);
            fos.close();
        } catch (IOException e) {

        }
    }

    private static void writeStudentFile(Student student) {
        String username = student.getUsername();
        String filepath = getAccountFilePath(username, AccountType.STUDENT);
        createFile(filepath);
        String text = new Gson().toJson(student);
        writeJson(filepath, text);
        ArrayList<String> username_array = getUsernameArray(AccountType.STUDENT);
        username_array = addToList(username, username_array);
        writeUsernameFile(username_array, AccountType.STUDENT);
    }

    private static void writeTeacherFile(Teacher teacher) {
        String username = teacher.getUsername();
        String filepath = getAccountFilePath(username, AccountType.TEACHER);
        createFile(filepath);
        String text = new Gson().toJson(teacher);
        writeJson(filepath, text);
        ArrayList<String> username_array = getUsernameArray(AccountType.TEACHER);
        username_array = addToList(username, username_array);
        writeUsernameFile(username_array, AccountType.TEACHER);
    }

    private static void writeManagerFile(Manager manager) {
        String username = manager.getUsername();
        String filepath = getAccountFilePath(username, AccountType.MANAGER);
        createFile(filepath);
        String text = new Gson().toJson(manager);
        writeJson(filepath, text);
        ArrayList<String> username_array = getUsernameArray(AccountType.MANAGER);
        username_array = addToList(username, username_array);
        writeUsernameFile(username_array, AccountType.MANAGER);
    }

    private static void writeCourseFile(Course course) {
        String courseID = course.getCourseID();
        String filepath = "data/course/" + courseID + data_filetype;
        String text = new Gson().toJson(course);
        writeJson(filepath, text);
        ArrayList<String> courseID_array = getCourseIDArray();
        courseID_array = addToList(courseID, courseID_array);
        writeCourseIDFile(courseID_array);
    }

    /*
    * Function for updating student information
    * */
    public static void updateStudent(Student newStudent, String old_username) {
        if (!Objects.equals(old_username, newStudent.getUsername())) removeStudent(old_username);
        writeStudentFile(newStudent);
    }

    public static void updateStudent(Student newStudent) {
        updateStudent(newStudent, newStudent.getUsername());
    }

    /*
     * Function for updating teacher information
     * */
    public static void updateTeacher(Teacher newTeacher, String old_username) {
        if (!Objects.equals(old_username, newTeacher.getUsername())) removeTeacher(old_username);
        writeTeacherFile(newTeacher);
    }

    public static void updateTeacher(Teacher newTeacher) {
        updateTeacher(newTeacher, newTeacher.getUsername());
    }

    public static void modifyCourse(Course newCourse, String old_courseID) {
        if (!Objects.equals(old_courseID, newCourse.getCourseID())) removeCourse(old_courseID);
        writeCourseFile(newCourse);
    }

    /*
    * Function for removing student from database
    * */

    private static ArrayList<String> removeFromList(String item, ArrayList<String> list) {
        if (list == null) return new ArrayList<>();
        list.remove(item);
        return list;
    }

    private static ArrayList<String> addToList(String item, ArrayList<String> list) {
        if (list == null) list = new ArrayList<>();
        if (list.contains(item)) return list;
        list.add(item);
        return list;
    }

    public static void removeStudent(String username) {
        if (getStudent(username) != null) {
            removeFile(getAccountFilePath(username, AccountType.STUDENT));
            ArrayList<String> username_array = getUsernameArray(AccountType.STUDENT);
            username_array = removeFromList(username, username_array);
            writeUsernameFile(username_array, AccountType.STUDENT);
        }
    }

    /*
     * Function for removing teacher from database
     * */
    public static void removeTeacher(String username) {
        if (getTeacher(username) != null) {
            removeFile(getAccountFilePath(username, AccountType.TEACHER));
            ArrayList<String> username_array = getUsernameArray(AccountType.TEACHER);
            username_array = removeFromList(username, username_array);
            writeUsernameFile(username_array, AccountType.TEACHER);
        }
    }

    public static void removeCourse(String courseID) {
        if (getCourse(courseID) != null) {
            removeFile("data/course/" + courseID + data_filetype);
            ArrayList<String> courseID_array = getCourseIDArray();
            courseID_array = removeFromList(courseID, courseID_array);
            writeCourseIDFile(courseID_array);
        }
    }

    /*
    * Called for register / add a student / teacher
    * */
    public static String registerStudent(Student student) {
        String username = student.getUsername();
        if (getStudent(username) != null) {
            // teacher with this username already exists
            return "Student username " + student.getUsername() + " already exist";
        }
        writeStudentFile(student);
        return "";
    }

    public static String registerTeacher(Teacher teacher) {
        String username = teacher.getUsername();
        if (getTeacher(username) != null) {
            // teacher with this username already exists
            return "Teacher username " + teacher.getUsername() + " already exist";
        }
        writeTeacherFile(teacher);
        return "";
    }

    public static String registerManager(Manager manager) {
        String username = manager.getUsername();
        if (getManager(username) != null) {
            // manager with this username already exists
            return "Manager username " + manager.getUsername() + " already exist";
        }
        writeManagerFile(manager);
        return "";
    }

    public static String createCourse(Course course) {
        String courseID = course.getCourseID();
        if (getCourse(courseID) != null) {
            return "Course ID " + courseID + " already exist";
        }
        writeCourseFile(course);
        return "";
    }
}