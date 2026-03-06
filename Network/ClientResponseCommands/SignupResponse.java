package Network.ClientResponseCommands;
import Controllers.LoginController;

/**
 * Client-side handler for SIGNUP responses.
 *
 * Notifies the LoginController when account creation succeeds or fails.
 */
public class SignupResponse implements IClientResponseCommands{
    private LoginController loginController;
    /**
     * Creates a new SignupResponse handler.
     *
     * @param loginController controller to notify about signup results
     */
    public SignupResponse(LoginController loginController){
        this.loginController = loginController;
    }

    @Override
    /**
     * Executes the SIGNUP response.
     *
     * @param string protocol fields: SIGNUP;TRUE or SIGNUP;FAIL
     */
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