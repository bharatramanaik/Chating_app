import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Chating_controller extends Thread {
    private ArrayList<Chating_controller> clients;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public Chating_controller(Socket socket, ArrayList<Chating_controller> clients){
        try {
            this.socket = socket;
            this.clients = clients;
            this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.writer = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String msg;
            while ((msg = reader.readLine()) != null) {
                for (Chating_controller cl : clients) {
                    if(cl.socket!=this.socket){
                        cl.writer.println(msg);
                    }
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        

    }
}
