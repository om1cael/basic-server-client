import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    Socket socket;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("New connection from " + socket.getLocalAddress() + " in thread " + Thread.currentThread().getName());

        try(BufferedReader receivedClientData = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String userInput;

            System.out.println("local: " + socket.getLocalAddress());

            while((userInput = receivedClientData.readLine()) != null) {
                Server.broadcastMessage(userInput, this);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(String message) {
        try {
            PrintWriter serverDataToSend = new PrintWriter(socket.getOutputStream(), true);
            serverDataToSend.println(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
