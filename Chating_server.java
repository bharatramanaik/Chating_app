import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Chating_server {

    
    public static ArrayList<Chating_controller> clients=new ArrayList<>();
    public static void main(String[] args) {
        ServerSocket serverSocket;
        Socket socket;
        try {
            serverSocket = new ServerSocket(8889);
            while(true) {
                System.out.println("Waiting for clients...");
                socket = serverSocket.accept();
                System.out.println("Connected");
                Chating_controller clientThread = new Chating_controller(socket, clients);
                clients.add(clientThread);
                clientThread.start();
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
