package Controllers;
import java.util.LinkedList;

import Models.User;
import Services.ChannelService;
import Services.UserService;
import Views.LoginView;

public class LoginController {
    private LoginView view;
    private Runnable onLoginSuccess; 
    private User loggedInUser;
    
    private UserService userService;
    private ChannelService channelService;

    public LoginController(LoginView view, Runnable onLoginSuccess, UserService userService, ChannelService channelService) {
        this.view = view;
        this.onLoginSuccess = onLoginSuccess;
        this.userService = userService;
        this.channelService = channelService;
        setupListeners();
    }

    private void setupListeners() {
        view.addLoginListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            if (userService.login(username, password)) {
                loggedInUser = new User(username, password); 
                view.hide();
                onLoginSuccess.run();
            } else {
                view.showError("Invalid username or password.");
            }
        });

        view.addCreateAccountListener(e -> {
            String username = view.getUsername();
            String password = view.getPassword();

            if (username.isEmpty() || password.isEmpty()) {
                view.showError("Please enter both username and password.");
                return;
            }

            if (userService.register(username, password)) {

            channelService.handleNewUserPermissions(username);
                
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