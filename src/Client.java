import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try(Socket socket = new Socket("127.0.0.1", 7777);
            PrintWriter clientDataToSend = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader clientInput = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader serverData = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            String userInput;

            new Thread(() -> {
                String serverMessage;

                while(true) {
                    try {
                        while ((serverMessage = serverData.readLine()) != null) {
                            System.out.println("Sent: " + serverMessage);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                }
            }).start();

            while((userInput = clientInput.readLine()) != null) {
                clientDataToSend.println(userInput);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
