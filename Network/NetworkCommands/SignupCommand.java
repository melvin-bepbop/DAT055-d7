package Network.NetworkCommands;

import Database.IUserRepo;
import Network.ClientHandler;

/**
 * Handles SIGNUP requests on the server.
 *
 * Attempts to create a new user account via IUserRepo and responds with a
 * success flag or FAIL.
 */
public class SignupCommand implements INetworkCommand{
    public final static String identifier = "SIGNUP";

    private IUserRepo userRepo;

    /**
     * Creates a new SignupCommand.
     *
     * @param userRepo repository used to create users
     */
    public SignupCommand(IUserRepo userRepo){
        this.userRepo = userRepo;
    }
    @Override
    /**
     * Executes the SIGNUP command.
     *
     * @param data protocol fields: SIGNUP;username;password
     * @param sender client handler that sent the request
     */
    public void execute(String[] data, ClientHandler sender){
        String username = data[1];
        String passWord = data[2];
        boolean succed = userRepo.createUser(username, passWord);
        if (succed) {
            sender.respondToClient(identifier+";TRUE");
        }
        else{
            sender.respondToClient(identifier+";FAIL");
        }
    }
}
