import java.io.BufferedReader;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class logincontroller {
    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button login;

    @FXML
    private PasswordField pass;

    @FXML
    private TextField usn;

    @FXML
    private Label warn;

    Socket socket;
    BufferedReader br;
    PrintWriter prnt;
    ObjectInputStream obread;
    InputStream input;
    private String key="bharat12||091234";
    private byte[] k=key.getBytes();
    private SecretKeySpec secret=new SecretKeySpec(k, "AES");
    private Cipher ciper;

    public void logins(ActionEvent event) {

            if (usn.getText().equals("") || pass.getText().equals("")) {
                warn.setText("Please fill both feilds");
                return;
            } else {

                try {
                    //System.out.println(event);
                    warn.setText("");
                    socket=new Socket("127.0.0.1",8000); //connect here
                    System.out.println("connected to server");
                    //br=new BufferedReader(new InputStreamReader(socket.getInputStream())); //read form socket
                    prnt=new PrintWriter(socket.getOutputStream()); //write to socket
                    input=socket.getInputStream();
                    obread=new ObjectInputStream(input);
                    String usn1=usn.getText().toString();
                    String pass1=pass.getText().toString();
                    String fina=usn1+" "+pass1;
                    // usn.setText("");
                    usn.clear();
                    // pass.setText("");
                    pass.clear();
                    ciper=Cipher.getInstance("AES");
                    ciper.init(Cipher.ENCRYPT_MODE, secret);
                    byte[] encryptedmsg=ciper.doFinal(fina.getBytes());
                    String encodedString=Base64.getEncoder().encodeToString(encryptedmsg);
                    prnt.println(encodedString);
                    prnt.flush();

                    // Reciving response
                    String res=(String) obread.readObject();
                    ciper.init(Cipher.DECRYPT_MODE, secret);
                    byte[] decrypted=ciper.doFinal(Base64.getDecoder().decode(res));
                    String responsString=new String(decrypted);
                    // System.out.println(res);
                    // System.out.println(ip);
                    if (responsString.equals("success")) {
                        try {
                            System.out.println(Thread.currentThread().getName());
                            FXMLLoader loader=new FXMLLoader(getClass().getResource("succes.fxml"));
                            root=loader.load();
                            
                            succontrol on=loader.getController();
                            on.sttext(usn1);
                            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                            scene=new Scene(root);
                            stage.setScene(scene);
                            stage.show();
                            
                            } catch (Exception e) {
                                // TODO: handle exception
                            }
                    } else if (responsString.equals("duplicate")) {
                        warn.setText("This user is already logged in");
                    } else {
                        warn.setText("Invalid credentials");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // TODO: handle exception
                }
            }
        
    }

   
    public static void main(String[] args) {

        //logincontroller ll=new logincontroller();
    }
}