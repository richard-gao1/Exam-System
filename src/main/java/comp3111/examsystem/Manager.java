package comp3111.examsystem;

/**
 * A class representing a student, which extends the User class.
 */
public class Manager extends Account {
    /**
     * Constructs a new Manager instance with default credentials.
     */
    public Manager() {
        super("admin", "comp3111");
    }

    /**
     * Constructs a new Manager instance with the specified username and password.
     *
     * @param username The unique identifier for the manager.
     * @param password The password for the manager.
     */
    public Manager(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean equals(Object other) {
        return super.equals(other);
    }
}
