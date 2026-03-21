package Controllers;

import Models.User;
import Views.ILoginView;
import Network.Client; 
import Network.NetworkCommands.LoginCommand;
import Network.ClientResponseCommands.ILoginHandler;
import Network.ClientResponseCommands.ISignupHandler;
import Network.NetworkCommands.SignupCommand;

/**
 * Handles login and account creation actions.
 *
 * This controller binds UI listeners in the login view and sends requests to the server
 * using the text-based socket protocol. On successful login it initializes the local user
 * and executes the provided success callback.
 */
public class LoginController implements ILoginView.ViewListener, ILoginHandler, ISignupHandler {
    private ILoginView view;
    private Runnable onLoginSuccess; 
    private User loggedInUser;
    
    private Client networkClient; 

/**
     * Creates a controller for the given login view.
     *
     * @param view the abstract login UI
     * @param onLoginSuccess callback executed after a successful login
     * @param networkClient network client used to send requests to the server
     */
    public LoginController(ILoginView view, Runnable onLoginSuccess, Client networkClient) {
        this.view = view;
        this.onLoginSuccess = onLoginSuccess;
        this.networkClient = networkClient;
        
        this.view.setViewListener(this);
    }

    @Override
    public void onLoginRequested(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("Please enter both username and password.");
            return;
        }

        String payload = LoginCommand.identifier + ";" + username + ";" + password;
        networkClient.sendMessage(payload);
    }

    @Override
    public void onSignupRequested(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            view.showError("Please enter both username and password.");
            return;
        }

        String payload = SignupCommand.identifier + ";" + username + ";" + password;
        networkClient.sendMessage(payload);
    }



    /**
     * Handles a successful login response from the server.
     *
     * @param username the authenticated username
     * @param password the password provided during login
     */
    public void handleLoginSuccess(String username, String password) {
        loggedInUser = new User(username, password); 
        view.hide();
        //MELLANSTEGHÄR
        onLoginSuccess.run();
    }

    /**
     * Displays an invalid-credentials message in the view.
     */
    public void handleLoginFailure() {
        view.showError("Invalid username or password.");
    }

    /**
     * Displays an account-created message in the view.
     */
    public void handleRegisterSuccess() {
        view.showMessage("Account created! You can now log in.");
    }

    /**
     * Displays an account-creation failure message in the view.
     */
    public void handleRegisterFailure() {
        view.showError("Username is already taken or failed to create.");
    }

    /**
     * Returns the currently logged-in user, if any.
     *
     * @return the logged-in user, or null if login has not completed
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
}