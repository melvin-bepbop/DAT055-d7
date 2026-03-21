package Services;
import java.util.LinkedList;

import Database.IUserRepo;
import Models.User;

/**
 * Provides user-related application logic.
 *
 * This service wraps a user repository and exposes operations such as login,
 * registration, and user listing.
 */
public class UserService {
    private IUserRepo userRepo;

    /**
     * Creates a user service.
     *
     * @param userRepo repository used for user operations
     */
    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Logic for logging in
    /**
     * Attempts to authenticate a user.
     *
     * @param username username to authenticate
     * @param password password to authenticate
     * @return true if credentials are valid, otherwise false
     */
    public boolean login(String username, String password) {
        return userRepo.loginUser(username, password);
    }

    // Logic for creating an account
    /**
     * Attempts to register a new user account.
     *
     * @param username username to register
     * @param password password to register
     * @return true if the account was created, otherwise false
     */
    public boolean register(String username, String password) {

        return userRepo.createUser(username, password);
    }
    
    // Logic for fetching all users 
    /**
     * Fetches all users.
     *
     * @return list of users
     */
    public LinkedList<User> getAllUsers() {
        return userRepo.GetAllUsers();
    }
}