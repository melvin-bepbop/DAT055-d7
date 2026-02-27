import java.awt.event.ActionListener;

public class LoginController {
    private LoginView view;
    
    private Runnable onLoginSuccess; 
    private User loggedInUser;

    public LoginController(LoginView view, Runnable onLoginSuccess) {
        this.view = view;
        this.onLoginSuccess = onLoginSuccess;
        setupListeners();
    }

    private void setupListeners() {
        //login user
        view.getLoginButton().addActionListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            if (Database.loginUser(username, password)) {
                loggedInUser = new User(username, password, true); 
                view.hide();
                onLoginSuccess.run();
            } else {
                view.showError("Invalid username or password.");
            }
        });
        //create user
        view.getCreateAccountButton().addActionListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            if (Database.createUser(username, password)) {
                view.showMessage("Account created! You can now log in.");
            } else {
                view.showError("Username is already taken.");
            }
        });
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }
}