package Network.ClientResponseCommands;


/**
 * Client-side handler for SIGNUP responses.
 *
 * Notifies the LoginController when account creation succeeds or fails.
 */
public class SignupResponse implements IClientResponseCommands{
    private ISignupHandler signupHandler;
    /**
     * Creates a new SignupResponse handler.
     *
     * @param signupHandler handler to notify about signup results
     */
    public SignupResponse(ISignupHandler signupHandler) {
        this.signupHandler = signupHandler;
    }

    @Override
    /**
     * Executes the SIGNUP response.
     *
     * @param string protocol fields: SIGNUP;TRUE or SIGNUP;FAIL
     */
    public void execute(String[] string){
        if(!string[1].equals("FAIL") ){
            signupHandler.handleRegisterSuccess();
        }
        else{
            signupHandler.handleRegisterFailure();;
        }
    }
}
