package Network.NetworkCommands;

import Network.ClientHandler;

/**
 * Abstraction for server-side network commands.
 *
 * Implementations encapsulate the logic for handling a specific protocol command.
 */
public interface INetworkCommand {
    /**
     * Executes the command using the parsed data and sender context.
     *
     * @param data split protocol fields, including the command identifier at index 0
     * @param sender client handler that sent the request
     */
    void execute(String[] data, ClientHandler sender);
}
