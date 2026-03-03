package Network;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
public class Server{
    private ServerSocket serverSocket;
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    public void startServer(){
        try{
            while (!this.serverSocket.isClosed()) {
                Socket socket = this.serverSocket.accept();
                System.out.println("New Client connected");
                ClientHandler clientHandler = new ClientHandler(socket);
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
