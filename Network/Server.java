package Network;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;

/**
 * TCP server wrapper for the chat application.
 *
 * Accepts incoming socket connections and spawns a ClientHandler in a new thread
 * for each connected client, using a shared Router for command dispatch.
 */
public class Server{
    private ServerSocket serverSocket;
    private Router router;
    /**
     * Creates a new server instance.
     *
     * @param serverSocket bound server socket
     * @param router router used to dispatch incoming messages
     */
    public Server(ServerSocket serverSocket, Router router){
        this.serverSocket = serverSocket;
        this.router = router;
    }
    /**
     * Starts accepting clients and handling them in dedicated threads.
     */
    public void startServer(){
        try{
            while (!this.serverSocket.isClosed()) {
                Socket socket = this.serverSocket.accept();
                System.out.println("New Client connected");
                ClientHandler clientHandler = new ClientHandler(socket, router);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }
        catch(IOException e){
            try{
                if(serverSocket != null){
                    serverSocket.close();
                }
            }
            catch(IOException i){
                i.printStackTrace();
            }
        }
    }

}
