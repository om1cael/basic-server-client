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

            while((userInput = receivedClientData.readLine()) != null) {
                Server.broadcastMessage(userInput);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            Server.removeFromClients(this);
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
