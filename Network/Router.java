package Network;

import java.util.HashMap;
import java.util.Map;

import Network.NetworkCommands.INetworkCommand;

/**
 * Server-side command router.
 *
 * Maps command identifiers in incoming protocol messages to INetworkCommand
 * implementations and executes them for a given client.
 */
public class Router {
    private final Map<String, INetworkCommand> commands = new HashMap<>();

    /**
     * Registers a server command handler.
     *
     * @param commandName protocol identifier for the command
     * @param command command implementation to invoke
     */
    public void registerCommand(String commandName, INetworkCommand command) {
        commands.put(commandName, command);
    }

    /**
     * Dispatches a raw protocol line to the appropriate command handler.
     *
     * @param rawData full incoming line from a client
     * @param sender client handler representing the sender
     */
    public void handleRequest(String rawData, ClientHandler sender) {
        if (rawData == null || rawData.isEmpty()) return;

        String[] parts = rawData.split(";");
        String commandName = parts[0]; 

        INetworkCommand command = commands.get(commandName);

        if (command != null) {
            command.execute(parts, sender);
        } else {
            System.err.println("Router: Okänt kommando mottaget -> " + commandName);
        }
    }

}
