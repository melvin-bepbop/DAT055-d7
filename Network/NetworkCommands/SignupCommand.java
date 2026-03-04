package Network.NetworkCommands;

import Database.IUserRepo;
import Network.ClientHandler;

public class SignupCommand implements INetworkCommand{
    public final static String identifier = "SIGNUP";

    private IUserRepo userRepo;

    public SignupCommand(IUserRepo userRepo){
        this.userRepo = userRepo;
    }
    @Override
    public void execute(String[] data, ClientHandler sender){
        String username = data[1];
        String passWord = data[2];
        boolean succed = userRepo.createUser(username, passWord);
        if (succed) {
            sender.broadcastMessage(identifier+";TRUE");
        }
        else{
            sender.broadcastMessage(identifier+";FAIL");
        }
    }
}
