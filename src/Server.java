import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    static List<ServerThread> clients = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(7777)) {
            while(true) {
                ServerThread serverThread = new ServerThread(serverSocket.accept());
                clients.add(serverThread);

                System.out.println(clients);
                new Thread(serverThread).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static synchronized void removeFromClients(ServerThread thread) {
        System.out.println("Removing client " + thread);
        clients.remove(thread);
    }

    public static synchronized void broadcastMessage(String message) {
        for(ServerThread client : clients) {
            if(!client.socket.isClosed()) {
                System.out.println("sending to " + client);
                client.sendMessage(message);
            }
        }
    }
}
