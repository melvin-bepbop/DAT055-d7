package Network;

import java.util.HashMap;
import java.util.Map;

import Network.ClientResponseCommands.IClientResponseCommands;
import Network.NetworkCommands.INetworkCommand;

/**
 * Client-side response router.
 *
 * Maps command identifiers in incoming protocol messages from the server
 * to IClientResponseCommands implementations and executes them.
 */
public class ClientRouter {
    private final Map<String, IClientResponseCommands> commands = new HashMap<>();

    /**
     * Registers a client response handler.
     *
     * @param commandName protocol identifier for the response
     * @param command handler implementation to invoke
     */
    public void registerCommand(String commandName, IClientResponseCommands command) {
        commands.put(commandName, command);
    }

    /**
     * Dispatches a raw protocol line from the server to the appropriate handler.
     *
     * @param rawData full incoming line from the server
     */
    public void handleRequest(String rawData) {
        if (rawData == null || rawData.isEmpty()) return;

        String[] parts = rawData.split(";");
        String commandName = parts[0]; 

        IClientResponseCommands command = commands.get(commandName);

        if (command != null) {
            command.execute(parts);
            //System.out.println("Client Recieved: "+ rawData);
        } else {
            System.err.println("Okänt kommando mottaget -> " + commandName);
        }
    }

}
