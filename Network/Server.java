package Network;
import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
public class Server{
    private ServerSocket serverSocket;
    private Router router;
    public Server(ServerSocket serverSocket, Router router){
        this.serverSocket = serverSocket;
        this.router = router;
    }
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
