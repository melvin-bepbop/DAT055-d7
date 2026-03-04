package Network.ClientResponseCommands;
import Controllers.LoginController;

public class LoginResponse implements IClientResponseCommands{
    private LoginController loginController;
    public LoginResponse(LoginController loginController){
        this.loginController = loginController;
    }

    @Override
    public void execute(String[] string){
        if(!string[1].equals("FAIL") ){
            loginController.handleLoginSuccess(string[1], string[2]);
        }
        else{
            loginController.handleLoginFailure();
        }
    }
}
