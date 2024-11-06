package comp3111.examsystem;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Account implements Serializable {
    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        // check if valid username (will need to verify with database eventually)
        if (username.isEmpty()) {
            this.username = username;
        } else {
            throw new IllegalArgumentException("Username cannot be empty");
        }
    }

    public String getPassword() {
        return this.password;
    }

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
        return (Objects.equals(this.username, account.username)) && (Objects.equals(this.password, account.password));
    }
}
