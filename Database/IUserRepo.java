package Database;
import java.util.LinkedList;

import Models.User;

/**
 * Repository interface for user-related database operations.
 *
 * Implementations are responsible for persisting and querying user data.
 */
public interface IUserRepo {
    /**
     * Checks whether a username already exists.
     *
     * @param username username to check
     * @return true if the username exists, otherwise false
     */
    boolean isUsernameTaken(String username);

    /**
     * Creates a new user account.
     *
     * @param username new username
     * @param password new password
     * @return true if the user was created, otherwise false
     */
    boolean createUser(String username, String password);

    /**
     * Verifies a username and password combination.
     *
     * @param username username to authenticate
     * @param password password to authenticate
     * @return true if credentials are valid, otherwise false
     */
    boolean loginUser(String username, String password); 

    /**
     * Returns all users from the data store.
     *
     * @return list of users
     */
    LinkedList<User> GetAllUsers();
}
