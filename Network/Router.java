package Network;

import java.util.HashMap;
import java.util.Map;

import Network.NetworkCommands.INetworkCommand;

public class Router {
    private final Map<String, INetworkCommand> commands = new HashMap<>();

    public void registerCommand(String commandName, INetworkCommand command) {
        commands.put(commandName, command);
    }

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
