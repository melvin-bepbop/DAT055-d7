import java.util.LinkedList;

public class LoginController {
    private LoginView view;
    
    private Runnable onLoginSuccess; 
    private User loggedInUser;
    private IDatabase db;

    public LoginController(LoginView view, Runnable onLoginSuccess, IDatabase db) {
        this.view = view;
        this.onLoginSuccess = onLoginSuccess;
        this.db= db;
        setupListeners();
    }

    private void setupListeners() {
        //login user
        view.addLoginListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            if (db.loginUser(username, password)) {
                loggedInUser = new User(username, password); 
                view.hide();
                onLoginSuccess.run();
            } else {
                view.showError("Invalid username or password.");
            }
        });
        //create user
       view.addCreateAccountListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            if (db.createUser(username, password)) {
                //CHANGE IF YOU WANT PRIVATE CHANNELS
                LinkedList<Channel> allExistingChannels = db.GetAllChannels();
                for (Channel channel : allExistingChannels) {
                    db.UserJoinChannel(username, channel.getChannelName());
                }
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