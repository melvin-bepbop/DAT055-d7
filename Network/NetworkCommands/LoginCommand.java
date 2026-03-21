package Network.NetworkCommands;

import Database.IUserRepo;
import Network.ClientHandler;

/**
 * Handles LOGIN requests on the server.
 *
 * Verifies user credentials via IUserRepo and responds with either a success
 * payload or a FAIL indicator.
 */
public class LoginCommand implements INetworkCommand{
    public final static String identifier = "LOGIN";

    private IUserRepo userRepo;

    /**
     * Creates a new LoginCommand.
     *
     * @param userRepo repository used to authenticate users
     */
    public LoginCommand(IUserRepo userRepo){
        this.userRepo = userRepo;
    }
    @Override
    /**
     * Executes the LOGIN command.
     *
     * @param data protocol fields: LOGIN;username;password
     * @param sender client handler that sent the request
     */
    public void execute(String[] data, ClientHandler sender){
        String username = data[1];
        String passWord = data[2];
        boolean succeed = userRepo.loginUser(username, passWord);
        if (succeed) {
            sender.respondToClient(identifier+";"+ username+ ";"+ passWord);
        }
        else{
            sender.respondToClient(identifier+";FAIL");
        }
    }
}
