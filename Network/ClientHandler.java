package Network;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import Models.Message;


public class ClientHandler implements Runnable {
    private Socket socket;
    public static ArrayList<ClientHandler> ClientHandlers = new ArrayList<>();
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        try{
            this.socket = socket; 
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
                
                broadcastMessage(messageFromClient);
            }
            catch(IOException e){
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }
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
    public void removeClientHandler(){
        ClientHandlers.remove(this);
        broadcastMessage("User has left");
    }
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