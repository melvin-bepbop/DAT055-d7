package Network.NetworkCommands;

import Network.ClientHandler;

public interface INetworkCommand {
    void execute(String[] data, ClientHandler sender);
}
