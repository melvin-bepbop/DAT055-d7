package Network.ClientResponseCommands;

/**
 * Abstraction for client-side response handlers.
 *
 * Implementations interpret and act on protocol messages sent from the server.
 */
public interface IClientResponseCommands {
    /**
     * Handles an incoming response message from the server.
     *
     * @param data split protocol fields, including the command identifier at index 0
     */
    void execute(String[] data);
}
