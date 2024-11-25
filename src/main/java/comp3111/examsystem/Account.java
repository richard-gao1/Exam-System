package comp3111.examsystem;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * An abstract class representing an account
 */
public abstract class Account {
    private String username;
    private String password;

    /**
     * Constructs a new Account instance with the specified username and password.
     *
     * @param username The unique identifier for the account.
     * @param password The password for the account.
     */
    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the username of the account.
     *
     * @return The username as a string.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * Sets the username of the account.
     *
     * @param username The new username to set.
     */
    public void setUsername(String username) {
        // check if valid username (will need to verify with database eventually)
        if (!username.isEmpty()) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }

    /**
     * Updates both the username and password of the account.
     *
     * @param username The new username to set.
     * @param password The new password to set.
     */
    public void update(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Authenticates the account with a provided password.
     *
     * @param password The password to authenticate against.
     * @return true if the password matches, false otherwise.
     */
    public boolean login(String password) {
        return Objects.equals(this.password, password);
    }

    /**
     * Retrieves the password of the account.
     *
     * @return The password as a string.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password of the account.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        // can add password requirements here
        if (!password.isEmpty()) {
            this.password = password;
        } else {
            throw new IllegalArgumentException("Password cannot be empty");
        }
    }

    @Override
    public boolean equals(Object other) {
        Account account = (Account) other;
        if (other == null) return false;
        return (Objects.equals(this.username, account.username)) && (Objects.equals(this.password, account.password));
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}
