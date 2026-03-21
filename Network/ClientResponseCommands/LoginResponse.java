package Network.ClientResponseCommands;

/**
 * Client-side handler for LOGIN responses.
 *
 * Notifies the LoginController about successful or failed login attempts.
 */
public class LoginResponse implements IClientResponseCommands{
    private ILoginHandler loginHandler;
    /**
     * Creates a new LoginResponse handler.
     *
     * @param loginHandler handler to notify about login results
     */
    public LoginResponse(ILoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @Override
    /**
     * Executes the LOGIN response.
     *
     * @param string protocol fields: LOGIN;username;password or LOGIN;FAIL
     */
    public void execute(String[] string){
        if(!string[1].equals("FAIL") ){
            loginHandler.handleLoginSuccess(string[1], string[2]);
        }
        else{
            loginHandler.handleLoginFailure();
        }
    }
}
