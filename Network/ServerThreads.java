package Network;/* 
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ServerThreads {
   private int port;

    public ServerThreads(int port) {
        this.port = port;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server listening on port " + port);

        while (true) {
            Socket clientSocket = serverSocket.accept(); // wait for a client
            System.out.println("Client connected: " + clientSocket);

            // Handle client in a new thread
            new Thread(new ClientHandler(clientSocket)).start();
            
        }

        Socket socket = new Socket("localhost", 6767);
        InputStream inStream = socket.getInputStream();
        ObjectInputStream objectInputStreamin = new ObjectInputStream(socket.getInputStream());         
        PrintWriter writer = new PrintWriter(out, true); // true = auto-flush
        
        String response = reader.readLine();
    }
}*/

