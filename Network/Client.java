package Network;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private ClientRouter clientRouter;

    public Client (Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter, ClientRouter clientRouter, String username){
        this.socket = socket;
        this.bufferedReader = bufferedReader;
        this.bufferedWriter = bufferedWriter;
        this.clientRouter = clientRouter;
        this.username = username;
    }


  public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine(); // Very important! Tells the server the message is done
            bufferedWriter.flush();   // Pushes it out immediately
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }
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
