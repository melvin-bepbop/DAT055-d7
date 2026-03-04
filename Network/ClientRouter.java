package Network;

import java.util.HashMap;
import java.util.Map;

import Network.ClientResponseCommands.IClientResponseCommands;
import Network.NetworkCommands.INetworkCommand;

public class ClientRouter {
    private final Map<String, IClientResponseCommands> commands = new HashMap<>();

    public void registerCommand(String commandName, IClientResponseCommands command) {
        commands.put(commandName, command);
    }

    public void handleRequest(String rawData) {
        if (rawData == null || rawData.isEmpty()) return;

        String[] parts = rawData.split(";");
        String commandName = parts[0]; 

        IClientResponseCommands command = commands.get(commandName);

        if (command != null) {
            command.execute(parts);
        } else {
            System.err.println("Okänt kommando mottaget -> " + commandName);
        }
    }

}
