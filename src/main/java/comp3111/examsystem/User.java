package comp3111.examsystem;

import java.util.Objects;

public class User extends Account{
    private String name;
    private String gender;
    private int age;
    private String department;

    public User(String username, String password, String name, String gender, int age, String department ) {
        super(username, password);
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.department = department;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name.isEmpty()) {
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

    @Override
    public boolean equals(Object other) {
        return super.equals(other) &&
                Objects.equals(this.name, ((User) other).getName()) &&
                Objects.equals(this.gender, ((User) other).getGender()) &&
                (this.age == ((User) other).getAge()) &&
                Objects.equals(this.department, ((User) other).getDepartment());
    }
}
