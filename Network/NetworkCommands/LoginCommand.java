package Network.NetworkCommands;

import Database.IUserRepo;
import Network.ClientHandler;

public class LoginCommand implements INetworkCommand{
    public final static String identifier = "LOGIN";

    private IUserRepo userRepo;

    public LoginCommand(IUserRepo userRepo){
        this.userRepo = userRepo;
    }
    @Override
    public void execute(String[] data, ClientHandler sender){
        String username = data[1];
        String passWord = data[2];
        boolean succed = userRepo.loginUser(username, passWord);
        if (succed) {
            sender.respondToClient(identifier+";"+ username+ ";"+ passWord);
        }
        else{
            sender.respondToClient(identifier+";FAIL");
        }
    }
}
