import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Chat extends Thread implements Initializable{
    
    BufferedReader br;
    PrintWriter prnt;
    ObjectInputStream obread;
    InputStream input;
    Connection conn;
    ResultSet rslt;
    PreparedStatement prst;
    
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextArea chats;

    @FXML
    private TextField sendmsg;

    @FXML
    private Label clname;

    @FXML
    private Button send;
    BufferedReader reader;
    PrintWriter writer;
    Socket socket;

   
    public void connecttoserver(){
        clname.setText(succontrol.clusn);
        try {
            socket = new Socket("localhost", 8889);
            System.out.println("Socket is connected with server!");
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            this.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run(){
        try {
            while (true) {
                String msgfrom=reader.readLine();
                chats.appendText(msgfrom+"\n");
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
   
    @FXML
    void send(ActionEvent event) {
       try {
            String mstosend=sendmsg.getText();
            writer.println(succontrol.myusn+" : "+mstosend);
            //chats.appendText(mstosend+"\n");
            sendmsg.clear();
       } catch (Exception e) {
            // TODO: handle exception
       }
    }
    
   
    
    @FXML
    void goback(ActionEvent event) {
        try {
            FXMLLoader loader=new FXMLLoader(getClass().getResource("succes.fxml"));
            root = loader.load();
            succontrol ss=loader.getController();
            ss.sttext(succontrol.myusn);
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
     @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        connecttoserver();
    }


}
