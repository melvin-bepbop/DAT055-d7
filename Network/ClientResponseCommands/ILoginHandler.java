package Network.ClientResponseCommands;

/**
 * Interface for handling the results of a login attempt.
 */
public interface ILoginHandler {
    void handleLoginSuccess(String username, String password);
    void handleLoginFailure();
}