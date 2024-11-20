package comp3111.examsystem;

import com.google.gson.Gson;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Communicates and writes between systemDatabase and actors
 * @author whwmaust2125
 * @since 2024-11-21
 */
public class SystemDatabase {
    /**
     * The file format of the data stored
     */
    static final String data_filetype = ".json";
    /**
     * Store the current logged-in user (Teacher or Student)
     */
    public static User currentUser;

    /**
     * Creates a directory if it does not already exist.
     *
     * @param directory The path of the directory to create.
     * @return true if the directory was created or already exists, false otherwise.
     */
    private boolean createFolder(String directory) {
        File folder = new File(directory);
        if (!folder.exists()) return folder.mkdir();
        return true;
    }

    /**
     * Creates a file at the specified path if it does not already exist.
     *
     * @param filepath The path where the file should be created.
     * @return true if the file was created or already exists, false otherwise.
     */
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

    /**
     * Removes a file at the specified path.
     *
     * @param filepath The path of the file to remove.
     * @return true if the file was successfully removed, false otherwise.
     */
    private static boolean removeFile(String filepath) {
        File file = new File(filepath);
        return file.delete();
    }

    /**
     * Removes all files and directories of the database.
     */
    public static void removeAll() {
        removeFile("data");
    }

    /**
     * Constructs a new SystemDatabase instance, initializing necessary folders and files.
     * Also creates a default Manager account if none exists.
     */
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
        Manager manager = new Manager();
        registerManager(manager);
    }

    /**
     * Retrieves the file path for the list of names based on the account type.
     *
     * @param type The type of account (STUDENT, TEACHER, or MANAGER).
     * @return The file path as a string.
     */
    private static String getNameListFilePath(AccountType type) {
        String folder = switch (type) {
            case STUDENT -> "students";
            case TEACHER -> "teachers";
            case MANAGER -> "managers";
        };
        return "data/account/" + folder + data_filetype;
    }

    /**
     * Retrieves the file path for a specific user's account based on the username and account type.
     *
     * @param username The unique identifier for the user.
     * @param type     The type of account (STUDENT, TEACHER, or MANAGER).
     * @return The file path as a string.
     */
    private static String getAccountFilePath(String username, AccountType type) {
        if (username.isEmpty()) return "";
        String folder = switch (type) {
            case STUDENT -> "student";
            case TEACHER -> "teacher";
            case MANAGER -> "manager";
        };
        return "data/account/" + folder + "/" + username + data_filetype;
    }

    /**
     * Logs in an account with the provided username, password, and account type.
     *
     * @param username The unique identifier for the user.
     * @param password The password for the user.
     * @param type     The type of account (STUDENT, TEACHER, or MANAGER).
     * @return An Account object if login is successful, otherwise null.
     */
    public static Account login(String username, String password, AccountType type) {
        Account account = switch (type) {
            case STUDENT -> getStudent(username);
            case TEACHER -> getTeacher(username);
            case MANAGER -> getManager(username);
        };
        if (account == null || !account.login(password)) return null;
        return account;
    }

    /**
     * Converts a JSON string to a Student object.
     *
     * @param input The JSON string to be converted.
     * @return A Student object if conversion is successful, otherwise null.
     */
    public static Student jsonStringToStudent(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Student.class);
    }

    /**
     * Converts a JSON string to a Teacher object.
     *
     * @param input The JSON string to be converted.
     * @return A Teacher object if conversion is successful, otherwise null.
     */
    public static Teacher jsonStringToTeacher(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Teacher.class);
    }

    /**
     * Converts a JSON string to a Manager object.
     *
     * @param input The JSON string to be converted.
     * @return A Manager object if conversion is successful, otherwise null.
     */
    public static Manager jsonStringToManager(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Manager.class);
    }

    /**
     * Converts a JSON string to a Course object.
     *
     * @param input The JSON string to be converted.
     * @return A Course object if conversion is successful, otherwise null.
     */
    public static Course jsonStringToCourse(String input) {
        Gson gson = new Gson();
        return gson.fromJson(input, Course.class);
    }

    /**
     * Retrieves a Course object from the file system based on the provided course ID.
     *
     * @param courseID The unique identifier for the course.
     * @return A Course object if found, otherwise null.
     */
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

    /**
     * Retrieves a Student object from the file system based on the provided username.
     *
     * @param username The unique identifier for the student.
     * @return A Student object if found, otherwise null.
     */
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

    /**
     * Retrieves a Teacher object from the file system based on the provided username.
     *
     * @param username The unique identifier for the teacher.
     * @return A Teacher object if found, otherwise null.
     */
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

    /**
     * Retrieves a Manager object from the file system based on the provided username.
     *
     * @param username The unique identifier for the manager.
     * @return A Manager object if found, otherwise null.
     */
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

    /**
     * Reads and returns a list of course IDs from a file.
     *
     * @return An ArrayList containing the course IDs, or null if an error occurs or the file is
    empty.
     */
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

    /**
     * Retrieves a list of students filtered by containing specific substring in username, name, and department.
     *
     * @param usernameFilter The filter for student usernames.
     * @param nameFilter     The filter for student names.
     * @param departmentFilter The filter for student departments.
     * @return An ArrayList containing the filtered list of Student objects.
     */
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

    /**
     * Retrieves a list of teachers filtered by containing specific substring in username, name, and department.
     *
     * @param usernameFilter The filter for teacher usernames.
     * @param nameFilter     The filter for teacher names.
     * @param departmentFilter The filter for teacher departments.
     * @return An ArrayList containing the filtered list of Teacher objects.
     */
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

    /**
     * Retrieves a list of courses filtered by containing specific substring in course ID, course name, and department.
     *
     * @param courseIDFilter The filter for course IDs.
     * @param courseNameFilter The filter for course names.
     * @param departmentFilter The filter for course departments.
     * @return An ArrayList containing the filtered list of Course objects.
     */
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

    /**
     * Reads and returns a list of usernames from a file.
     *
     * @param type The AccountType specifying which list to read.
     * @return An ArrayList containing the usernames, or an empty ArrayList if an error occurs or
    the file is empty.
     */
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

    /**
     * Writes a list of usernames to a file.
     *
     * @param username_array The ArrayList containing the usernames to be written.
     * @param type           The AccountType specifying which list to write to.
     */
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

    /**
     * Writes a list of course IDs to a file.
     *
     * @param course_array The ArrayList containing the course IDs to be written.
     */
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

    /**
     * Writes JSON content to a specified file.
     *
     * @param filepath The path of the file where the JSON will be written.
     * @param text     The JSON string to be written to the file.
     */
    private static void writeJson(String filepath, String text) {
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            byte[] content = text.getBytes(StandardCharsets.UTF_8);
            fos.write(content);
            fos.close();
        } catch (IOException e) {

        }
    }

    /**
     * Writes a student's information to a file.
     *
     * @param student The Student object containing the information to be written.
     */
    private static void writeStudentFile(Student student) {
        if (student == null) return;
        String username = student.getUsername();
        String filepath = getAccountFilePath(username, AccountType.STUDENT);
        createFile(filepath);
        String text = new Gson().toJson(student);
        writeJson(filepath, text);
        ArrayList<String> username_array = getUsernameArray(AccountType.STUDENT);
        username_array = addToList(username, username_array);
        writeUsernameFile(username_array, AccountType.STUDENT);
    }

    /**
     * Writes a teacher's information to a file.
     *
     * @param teacher The Teacher object containing the information to be written.
     */
    private static void writeTeacherFile(Teacher teacher) {
        if (teacher == null) return;
        String username = teacher.getUsername();
        String filepath = getAccountFilePath(username, AccountType.TEACHER);
        createFile(filepath);
        String text = new Gson().toJson(teacher);
        writeJson(filepath, text);
        ArrayList<String> username_array = getUsernameArray(AccountType.TEACHER);
        username_array = addToList(username, username_array);
        writeUsernameFile(username_array, AccountType.TEACHER);
    }

    /**
     * Writes a manager's information to a file.
     *
     * @param manager The Manager object containing the information to be written.
     */
    private static void writeManagerFile(Manager manager) {
        if (manager == null) return;
        String username = manager.getUsername();
        String filepath = getAccountFilePath(username, AccountType.MANAGER);
        createFile(filepath);
        String text = new Gson().toJson(manager);
        writeJson(filepath, text);
        ArrayList<String> username_array = getUsernameArray(AccountType.MANAGER);
        username_array = addToList(username, username_array);
        writeUsernameFile(username_array, AccountType.MANAGER);
    }

    /**
     * Writes a course's information to a file.
     *
     * @param course The Course object containing the information to be written.
     */
    private static void writeCourseFile(Course course) {
        if (course == null) return;
        String courseID = course.getCourseID();
        String filepath = "data/course/" + courseID + data_filetype;
        String text = new Gson().toJson(course);
        writeJson(filepath, text);
        ArrayList<String> courseID_array = getCourseIDArray();
        courseID_array = addToList(courseID, courseID_array);
        writeCourseIDFile(courseID_array);
    }

    /**
     * Updates a student's information using a new Student object and the old username.
     *
     * @param newStudent The updated Student object with new information.
     * @param old_username The original username of the student to be updated.
     */
    public static void updateStudent(Student newStudent, String old_username) {
        changeStudentUsername(newStudent, old_username);
        writeStudentFile(newStudent);
    }

    /**
     * Updates a student's information using a new Student object.
     *
     * @param newStudent The updated Student object with new information.
     */
    public static void updateStudent(Student newStudent) {
        updateStudent(newStudent, newStudent.getUsername());
    }

    /**
     * Updates a teacher's information using a new Teacher object and the old username.
     *
     * @param newTeacher The updated Teacher object with new information.
     * @param old_username The original username of the teacher to be updated.
     */
    public static void updateTeacher(Teacher newTeacher, String old_username) {
        changeTeacherUsername(newTeacher, old_username);
        writeTeacherFile(newTeacher);
    }

    /**
     * Updates a teacher's information using a new Teacher object.
     *
     * @param newTeacher The updated Teacher object with new information.
     */
    public static void updateTeacher(Teacher newTeacher) {
        updateTeacher(newTeacher, newTeacher.getUsername());
    }

    /**
     * Modifies a course by updating its information using a new Course object and the old course
     ID.
     *
     * @param newCourse The updated Course object with new information.
     * @param old_courseID The original course ID of the course to be modified.
     */
    public static void modifyCourse(Course newCourse, String old_courseID) {
        changeCourseID(newCourse, old_courseID);
        writeCourseFile(newCourse);
    }

    /**
     * Changes the username of a given student.
     *
     * @param newStudent The student object with the new username information.
     * @param old_username The original username that will be replaced.
     */
    private static void changeStudentUsername(Student newStudent, String old_username) {
        String new_username = (newStudent == null) ? "" : newStudent.getUsername();
        if (Objects.equals(old_username, new_username)) return;
        Student oldStudent = getStudent(old_username);
        if (oldStudent == null) return;
        List<Course> courses = oldStudent.getCourses();
        for (Course course : courses) {
            course.dropStudent(oldStudent);
            if (newStudent != null) course.addStudent(newStudent);
        }
        removeStudent(old_username);
    }

    /**
     * Changes the username of a given teacher.
     *
     * @param newTeacher The teacher object with the new username information.
     * @param old_username The original username that will be replaced.
     */
    private static void changeTeacherUsername(Teacher newTeacher, String old_username) {
        String new_username = (newTeacher == null) ? "" : newTeacher.getUsername();
        if (Objects.equals(old_username, new_username)) return;
        Teacher oldTeacher = getTeacher(old_username);
        if (oldTeacher == null) return;
        List<Course> courses = oldTeacher.getCourses();
        for (Course course : courses) {
            course.setTeacher(newTeacher);
        }
        removeTeacher(old_username);
    }

    /**
     * Changes the course ID of a given course.
     *
     * @param newCourse The course object with the new username information.
     * @param old_courseID The original courseID that will be replaced.
     */
    private static void changeCourseID(Course newCourse, String old_courseID) {
        String new_courseID = (newCourse == null) ? "" : newCourse.getCourseID();
        if (Objects.equals(old_courseID, new_courseID)) return;
        Course oldCourse = getCourse(old_courseID);
        if (oldCourse == null) return;
        Teacher teacher = oldCourse.getTeacher();
        if (teacher != null) {
            teacher.dropCourse(oldCourse);
            if (newCourse != null) teacher.addCourse(newCourse);
        }
        List<Student> students = oldCourse.getStudents();
        for (Student student : students) {
            student.dropCourse(oldCourse);
            if (newCourse != null) student.addCourse(newCourse);
        }
        removeCourse(old_courseID);
    }

    /**
     * Removes a specified item from an ArrayList of Strings.
     *
     * @param item The string item to be removed from the list.
     * @param list The ArrayList of strings from which the item should be removed.
     * @return A new ArrayList of Strings with the specified item removed. If the input list is
    null, returns an empty ArrayList.
     */
    private static ArrayList<String> removeFromList(String item, ArrayList<String> list) {
        if (list == null) return new ArrayList<>();
        list.remove(item);
        return list;
    }

    /**
     * Adds an item to a list if it does not already contain that item.
     *
     * @param item The item to be added to the list.
     * @param list The list to which the item should be added. If the list is null, it will be
    initialized as a new ArrayList.
     * @return The updated list containing the item.
     */
    private static ArrayList<String> addToList(String item, ArrayList<String> list) {
        if (list == null) list = new ArrayList<>();
        if (list.contains(item)) return list;
        list.add(item);
        return list;
    }

    /**
     * Removes a teacher account by username.
     *
     * @param username The username of the teacher to be removed.
     */
    public static void removeStudent(String username) {
        if (getStudent(username) != null) {
            removeFile(getAccountFilePath(username, AccountType.STUDENT));
            ArrayList<String> username_array = getUsernameArray(AccountType.STUDENT);
            username_array = removeFromList(username, username_array);
            writeUsernameFile(username_array, AccountType.STUDENT);
        }
    }

    /**
     * Removes a teacher account by username.
     *
     * @param username The username of the teacher to be removed.
     */
    public static void removeTeacher(String username) {
        if (getTeacher(username) != null) {
            removeFile(getAccountFilePath(username, AccountType.TEACHER));
            ArrayList<String> username_array = getUsernameArray(AccountType.TEACHER);
            username_array = removeFromList(username, username_array);
            writeUsernameFile(username_array, AccountType.TEACHER);
        }
    }

    /**
     * Removes a course by course ID.
     *
     * @param courseID The ID of the course to be removed.
     */
    public static void removeCourse(String courseID) {
        if (getCourse(courseID) != null) {
            removeFile("data/course/" + courseID + data_filetype);
            ArrayList<String> courseID_array = getCourseIDArray();
            courseID_array = removeFromList(courseID, courseID_array);
            writeCourseIDFile(courseID_array);
        }
    }

    /**
     * Registers a new student by writing their information to a file.
     *
     * @param student The Student object containing the information to be registered.
     * @return An empty string if registration is successful, or an error message if the username
    already exists.
     */
    public static String registerStudent(Student student) {
        String username = student.getUsername();
        if (getStudent(username) != null) {
            // teacher with this username already exists
            return "Student username " + student.getUsername() + " already exist";
        }
        writeStudentFile(student);
        return "";
    }

    /**
     * Registers a new teacher by writing their information to a file.
     *
     * @param teacher The Teacher object containing the information to be registered.
     * @return An empty string if registration is successful, or an error message if the username
    already exists.
     */
    public static String registerTeacher(Teacher teacher) {
        String username = teacher.getUsername();
        if (getTeacher(username) != null) {
            // teacher with this username already exists
            return "Teacher username " + teacher.getUsername() + " already exist";
        }
        writeTeacherFile(teacher);
        return "";
    }

    /**
     * Registers a new manager by writing their information to a file.
     *
     * @param manager The Manager object containing the information to be registered.
     * @return An empty string if registration is successful, or an error message if the username
    already exists.
     */
    public static String registerManager(Manager manager) {
        String username = manager.getUsername();
        if (getManager(username) != null) {
            // manager with this username already exists
            return "Manager username " + manager.getUsername() + " already exist";
        }
        writeManagerFile(manager);
        return "";
    }

    /**
     * Creates a new course by writing its information to a file.
     *
     * @param course The Course object containing the information to be created.
     * @return An empty string if creation is successful, or an error message if the course ID
    already exists.
     */
    public static String createCourse(Course course) {
        String courseID = course.getCourseID();
        if (getCourse(courseID) != null) {
            return "Course ID " + courseID + " already exist";
        }
        writeCourseFile(course);
        return "";
    }


}