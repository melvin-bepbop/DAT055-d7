package Database;
import java.util.LinkedList;

import Models.User;

public interface IUserRepo {
    boolean isUsernameTaken(String username);
    boolean createUser(String username, String password);
    boolean loginUser(String username, String password); 
    LinkedList<User> GetAllUsers();
}
