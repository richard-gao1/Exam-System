package comp3111.examsystem;

public class User {
    private String username;
    private String password;
    private String name;
    private String gender;
    private int age;
    private String department;

    public User(String username, String password, String name, String gender, int age, String department ) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.department = department;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        // check if valid username (will need to verify with database eventually)
        if (username != "") {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        // can add password requirements here
        if (password != "") {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != "") {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        // UI should not allow invalid gender, will be drop down will Male, Female, Other
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        // UI should ensure department is valid
        this.department = department;
    }
}
