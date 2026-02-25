import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public class ClientHandler implements Runnable {
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket; 
    }

    @Override public void run()  {
        try (
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        ) {
          
            while (true) {
                Message message = (Message) in.readObject();
                System.out.println("Received: " + message.getText());

                // Send response
                out.writeObject(new Message("Server received: " + message.getText()));
                out.flush();
            }
        } catch (EOFException e) {
            System.out.println("Client disconnected.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}