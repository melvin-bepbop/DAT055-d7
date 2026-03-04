package Network.ClientResponseCommands;
import Controllers.LoginController;

public class SignupResponse implements IClientResponseCommands{
    private LoginController loginController;
    public SignupResponse(LoginController loginController){
        this.loginController = loginController;
    }

    @Override
    public void execute(String[] string){
        if(!string[1].equals("FAIL") ){
            loginController.handleRegisterSuccess();
        }
        else{
            loginController.handleRegisterFailure();;
        }
    }
}
/*
package Network.ClientResponseCommands;

public class LoginResponse 

    
}*/