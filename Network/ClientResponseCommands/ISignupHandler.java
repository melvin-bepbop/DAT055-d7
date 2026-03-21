package Network.ClientResponseCommands;

/**
 * Interface for handling the results of an account creation attempt.
 */
public interface ISignupHandler {
    void handleRegisterSuccess();
    void handleRegisterFailure();
}