package Controllers;

import Models.User;
import Views.LoginView;
import Network.Client; // <-- We use your Network Client now!

public class LoginController {
    private LoginView view;
    private Runnable onLoginSuccess; 
    private User loggedInUser;
    
    private Client networkClient; // <-- Replaced the Database Services!

    public LoginController(LoginView view, Runnable onLoginSuccess, Client networkClient) {
        this.view = view;
        this.onLoginSuccess = onLoginSuccess;
        this.networkClient = networkClient;
        setupListeners();
    }

    private void setupListeners() {
        // --- 1. SENDING THE LOGIN REQUEST ---
        view.addLoginListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            // Format string and send over socket!
            String payload = "LOGIN|" + username + "|" + password;
            networkClient.sendMessage(payload);
            
            // NOTE: We do NOT close the window here. We wait for the server!
        });

        // --- 2. SENDING THE REGISTER REQUEST ---
        view.addCreateAccountListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            // Format string and send over socket!
            String payload = "REGISTER|" + username + "|" + password;
            networkClient.sendMessage(payload);
            
            // NOTE: We wait for the server to handle permissions and reply!
        });
    }

    // ======================================================================
    // --- NEW: METHODS FOR YOUR UPCOMING CLIENT-SIDE SOCKET ROUTER ---
    // ======================================================================
    // When your Client.java receives a message from the server, it will 
    // look at the string and trigger one of these specific methods.

    public void handleLoginSuccess(String username, String password) {
        loggedInUser = new User(username, password); 
        view.hide();
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