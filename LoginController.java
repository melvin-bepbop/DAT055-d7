import java.awt.event.ActionListener;

public class LoginController {
    private LoginView view;
    
    // Vi skickar in ett "Callback" (Runnable) så att vi vet vad vi ska göra när inloggningen lyckas
    private Runnable onLoginSuccess; 
    private User loggedInUser; // Sparar den inloggade användaren så vi kan ge den till App.java

    public LoginController(LoginView view, Runnable onLoginSuccess) {
        this.view = view;
        this.onLoginSuccess = onLoginSuccess;
        setupListeners();
    }

    private void setupListeners() {
        // Lyssnare för "Login"-knappen
        view.getLoginButton().addActionListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            // Anropa databasen direkt här
            if (Database.loginUser(username, password)) {
                // Skapa User-objektet, men UTAN att anropa Database.createUser i konstruktorn
                // OBS! Du kommer behöva justera din User-konstruktor något (se nedan)
                loggedInUser = new User(username, password, true); 
                view.hide();
                onLoginSuccess.run(); // Startar huvudprogrammet!
            } else {
                view.showError("Invalid username or password.");
            }
        });

        // Lyssnare för "Create Account"-knappen
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