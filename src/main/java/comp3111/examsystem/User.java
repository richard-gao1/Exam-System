package comp3111.examsystem;

import java.util.Objects;

/**
 * An abstract class representing a user, which extends the Account class.
 */
public abstract class User extends Account {
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

    /**
     * Updates the details of a User.
     *
     * @param username   The new username to set.
     * @param password   The new password to set.
     * @param name       The new name to set.
     * @param gender     The new gender to set.
     * @param age        The new age to set.
     * @param department The new department to set.
     * @return This User instance after updating the details.
     */
    public User update(String username, String password, String name, String gender, int age, String department) {
        super.update(username, password);
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.department = department;
        return this;
    }

    /**
     * Retrieves the name of the user.
     *
     * @return The name of the user.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     * Throws an IllegalArgumentException if the name is empty.
     *
     * @param name The new name to set for the user.
     */
    public void setName(String name) {
        if (name.isEmpty()) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }

    /**
     * Retrieves the gender of the user.
     *
     * @return The gender of the user.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the gender of the user.
     *
     * @param gender The new gender ("Male" or "Female") to set for the user.
     */
    public void setGender(String gender) {
        // UI should not allow invalid gender, will be drop down will Male, Female, Other
        this.gender = gender;
    }

    /**
     * Retrieves the age of the user.
     *
     * @return The age of the user.
     */
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the user.
     * Throws an IllegalArgumentException if the age is negative.
     *
     * @param age The new age to set for the user.
     */
    public void setAge(int age) {
        if (age > 0) {
            this.age = age;
        } else {
            throw new IllegalArgumentException("Age cannot be negative");
        }
    }

    /**
     * Retrieves the department associated with the user.
     *
     * @return The department of the user.
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Sets the department associated with the user.
     * Assumes that UI ensures a valid department is selected.
     *
     * @param department The new department to set for the user.
     */
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
