package Controllers;

import Models.User;
import Views.LoginView;
import Network.Client; // <-- We use your Network Client now!
import Network.NetworkCommands.LoginCommand;
import Network.NetworkCommands.SignupCommand;

public class LoginController {
    private LoginView view;
    private Runnable onLoginSuccess; 
    private User loggedInUser;
    
    private Client networkClient; 

    public LoginController(LoginView view, Runnable onLoginSuccess, Client networkClient) {
        this.view = view;
        this.onLoginSuccess = onLoginSuccess;
        this.networkClient = networkClient;
        setupListeners();
    }

    private void setupListeners() {
        // SENDING THE LOGIN REQUEST
        view.addLoginListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            String payload = LoginCommand.identifier + ";" + username + ";" + password;
            networkClient.sendMessage(payload);
            
        });

        // SENDING THE REGISTER REQUEST 
        view.addCreateAccountListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

   
            String payload = SignupCommand.identifier +";" + username + ";" + password;
            networkClient.sendMessage(payload);
            
        });
    }



    public void handleLoginSuccess(String username, String password) {
        loggedInUser = new User(username, password); 
        view.hide();
        //MELLANSTEGHÄR
        onLoginSuccess.run();
    }

    public void handleLoginFailure() {
        view.showError("Invalid username or password.");
    }

    public void handleRegisterSuccess() {
        view.showMessage("Account created! You can now log in.");
    }

    public void handleRegisterFailure() {
        view.showError("Username is already taken or failed to create.");
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}