package Models;

/**
 * Represents a user in the system.
 *
 * Stores username and password used by the client and server.
 */
public class User {
    private String username;
    private String password;

    /**
     * Creates a new user.
     *
     * @param username username
     * @param password password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password.
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }
}
