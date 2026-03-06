package Network.ClientResponseCommands;
import Controllers.LoginController;

/**
 * Client-side handler for LOGIN responses.
 *
 * Notifies the LoginController about successful or failed login attempts.
 */
public class LoginResponse implements IClientResponseCommands{
    private LoginController loginController;
    /**
     * Creates a new LoginResponse handler.
     *
     * @param loginController controller to notify about login results
     */
    public LoginResponse(LoginController loginController){
        this.loginController = loginController;
    }

    @Override
    /**
     * Executes the LOGIN response.
     *
     * @param string protocol fields: LOGIN;username;password or LOGIN;FAIL
     */
    public void execute(String[] string){
        if(!string[1].equals("FAIL") ){
            loginController.handleLoginSuccess(string[1], string[2]);
        }
        else{
            loginController.handleLoginFailure();
        }
    }
}
