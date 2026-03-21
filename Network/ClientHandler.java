package Network;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;



/**
 * Handles a single client connection on the server side.
 *
 * Reads incoming messages from the client socket, forwards them to the Router,
 * and can respond to that client or broadcast messages to all connected clients.
 */
public class ClientHandler implements Runnable {
    private Socket socket;
    public static ArrayList<ClientHandler> ClientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Router router;

    /**
     * Creates a new client handler and registers it in the global handler list.
     *
     * @param socket client socket
     * @param router router used to dispatch incoming protocol messages
     */
    public ClientHandler(Socket socket, Router router) {
        try{
            this.socket = socket; 
            this.router = router;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ClientHandlers.add(this);
            broadcastMessage("Connected to the server");

        }catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    @Override 
    public void run()  {
        String messageFromClient;
        while (socket.isConnected()) {
            try{
                messageFromClient = bufferedReader.readLine();
                //System.out.println(messageFromClient);
                if(messageFromClient != null){
                    //System.out.println("From Client: "+ messageFromClient);
                    router.handleRequest(messageFromClient, this);
                }
            }
            catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
    /**
     * Broadcasts a message to all connected clients.
     *
     * @param message raw protocol message to send
     */
    public void broadcastMessage(String message){
        try{
            for(ClientHandler ch : ClientHandlers){
            ch.bufferedWriter.write(message);
            ch.bufferedWriter.newLine();
            ch.bufferedWriter.flush();
        }
        }
    
        catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    /**
     * Sends a message only to this client.
     *
     * @param message raw protocol message to send
     */
    public void respondToClient(String message){
        try{
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        
        }
    
        catch(IOException e){
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    /**
     * Removes this handler from the global list and informs other clients.
     */
    public void removeClientHandler(){
        ClientHandlers.remove(this);
        broadcastMessage("User has left");
    }
    /**
     * Closes this handler, underlying streams and socket.
     *
     * @param socket socket to close
     * @param bufferedReader reader to close
     * @param bufferedWriter writer to close
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}