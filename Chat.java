import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.ResourceBundle;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

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
    String myusn;
    String myip;
    int myport;
    String clusn;
    String clip;
    int clport;
    private String key="bharat12||091234";
    private byte[] k=key.getBytes();
    private SecretKeySpec secret=new SecretKeySpec(k, "AES");
    

   
    public void connecttoserver(){
        clname.setText(succontrol.clusn);
        myusn=succontrol.myusn;
        myip=succontrol.myip;
        myport=succontrol.myport;
        clip=succontrol.clip;
        clport=succontrol.clport;
        try {
            // System.out.println("myip:-"+myip+",myport:-"+myport+"clip:-"+clip+",clportport:-"+clport);
            socket = new Socket("localhost",8889);
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
                Cipher ciper=Cipher.getInstance("AES");
                ciper.init(Cipher.DECRYPT_MODE, secret);
                byte[] decrypted=ciper.doFinal(Base64.getDecoder().decode(msgfrom));
                String finalstring=new String(decrypted).trim();
                chats.appendText(finalstring+"\n");
                
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
   
    @FXML
    void send(ActionEvent event) {
       try {
            String mstosend=sendmsg.getText();
            // System.out.println(succontrol.myusn);
            // Encrypting messages
            chats.appendText("Me : "+mstosend+"\n");
            sendmsg.clear();
            
            String combinedString=myusn+" : "+mstosend;
            Cipher ciper=Cipher.getInstance("AES");
            ciper.init(Cipher.ENCRYPT_MODE, secret);
            byte[] encryptedmsg=ciper.doFinal(combinedString.getBytes());
            String encodedString=Base64.getEncoder().encodeToString(encryptedmsg);
            writer.println(encodedString);
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
