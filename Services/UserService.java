package Services;
import java.util.LinkedList;

import Database.IUserRepo;
import Models.User;
public class UserService {
    private IUserRepo userRepo;

    public UserService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    // Logic for logging in
    public boolean login(String username, String password) {
        return userRepo.loginUser(username, password);
    }

    // Logic for creating an account
    public boolean register(String username, String password) {

        return userRepo.createUser(username, password);
    }
    
    // Logic for fetching all users (if needed for things like admin lists)
    public LinkedList<User> getAllUsers() {
        return userRepo.GetAllUsers();
    }
}