package Network;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * TCP client wrapper for the chat application.
 *
 * Holds the socket and I/O streams, sends protocol messages to the server,
 * and dispatches incoming messages to the ClientRouter.
 */
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private ClientRouter clientRouter;

    /**
     * Creates a new client instance.
     *
     * @param socket underlying socket connection
     * @param bufferedReader reader used to consume server messages
     * @param bufferedWriter writer used to send messages to the server
     * @param clientRouter router that dispatches incoming messages
     * @param username local username label for this connection
     */
    public Client (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, ClientRouter clientRouter, String username){
        this.socket = socket;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.clientRouter = clientRouter;
        this.username = username;
    }


  /**
   * Sends a raw protocol message to the server.
   *
   * @param message message string to send (must include command identifier)
   */
  public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine(); // Very important! Tells the server the message is done
            bufferedWriter.flush();   // Pushes it out immediately
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
    /**
     * Starts a background thread that listens for messages from the server.
     *
     * Incoming messages are passed to the ClientRouter.
     */
    public void listenForMessage(){
        new Thread(new Runnable() {
            @Override
            public void run(){
                String message;
                while(socket.isConnected()){
                    try{
                        message = bufferedReader.readLine();

                        //System.out.println(message);
                        clientRouter.handleRequest(message);
                        //----------------

                    } catch(IOException e){
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();;
    }
    /**
     * Closes all client resources and the underlying socket.
     *
     * @param socket socket to close
     * @param bufferedReader reader to close
     * @param bufferedWriter writer to close
     */
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
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
