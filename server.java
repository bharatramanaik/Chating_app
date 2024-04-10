import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class server{


    @FXML
    private Stage stage;
    ServerSocket server;
    Socket socket;
    BufferedReader br;
    PrintWriter prnt;
    OutputStream output;
    ObjectOutputStream obwrite;
    Connection conn;
    ResultSet rslt;
    PreparedStatement prst;
    private String key="bharat12||091234";
    private byte[] k=key.getBytes();
    private SecretKeySpec secret=new SecretKeySpec(k, "AES");
    private Cipher ciper;

    public server(){
        try {
            server=new ServerSocket(8000);
            System.out.println("server is listening");
            while (true) {
                socket=server.accept();
                br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
                prnt=new PrintWriter(socket.getOutputStream());
                output=socket.getOutputStream();
                obwrite=new ObjectOutputStream(output);
                String ss=br.readLine();
                Multiclient mltclient=new Multiclient(ss);
                new Thread(mltclient).start();
            }   
        } catch (Exception e) {
            // TODO: handle exception
        }
       
    }

    public class Multiclient implements Runnable {
        String usnn;
        String passs;
        String name;
        String fullString;
        public Multiclient(String ss) throws IllegalBlockSizeException, BadPaddingException{
            

            try {
                ciper = Cipher.getInstance("AES");
                ciper.init(Cipher.DECRYPT_MODE, secret);
                byte[] decrypted=ciper.doFinal(Base64.getDecoder().decode(ss));
                fullString=new String(decrypted);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String[] det=fullString.split(" ");
            usnn=det[0];
            passs=det[1];
           
        }

        @Override
        public void run() {
                try{
                    conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/chatting_app", "root", "Example@2024#");
                    prst=conn.prepareStatement("SELECT usn,pass FROM logindet WHERE usn=? AND pass=?");
                    prst.setString(1, usnn);
                    prst.setString(2, passs);

                    rslt=prst.executeQuery();
                    String succString="success";
                    String duplString="duplicate";
                    String invaliString="invalid";
                    if (rslt.next()) {
                        try {
                            
                            InetSocketAddress connectedsocket=(InetSocketAddress) socket.getRemoteSocketAddress();
                            String clientip=connectedsocket.getAddress().getHostAddress();
                            int port=socket.getPort();
                            
                            prst.close();
                            rslt.close();
                            
                            ciper=Cipher.getInstance("AES");
                            ciper.init(Cipher.ENCRYPT_MODE, secret);
                            byte[] encryptedmsg1=ciper.doFinal(succString.getBytes());
                            byte[] encryptedmsg2=ciper.doFinal(duplString.getBytes());
                            String encodedString1=Base64.getEncoder().encodeToString(encryptedmsg1);
                            String encodedString2=Base64.getEncoder().encodeToString(encryptedmsg2);
                            
                            if (!isrecordexists(conn,usnn,clientip,port)) {
                                String sql = "INSERT INTO sessionhandler(usn, ipaddr, port) VALUES(?, ?, ?)";
                                try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                                    preparedStatement.setString(1, usnn);
                                    preparedStatement.setString(2, clientip);
                                    preparedStatement.setInt(3, port);
                                    preparedStatement.executeUpdate();
                                    // System.out.println(encodedString1);
                                    obwrite.writeObject(encodedString1);
                                    
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                // System.out.println(encodedString2);
                                obwrite.writeObject(encodedString2);
                            }
                            
                            
                            
                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                            
                        }  
                        
                    } else {
                        // System.out.println(invaliString);
                        obwrite.writeObject(invaliString);
                    }

                } catch (SQLException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                    
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

        }
    
        public boolean isrecordexists(Connection conn,String usn,String ip,int port) throws SQLException {
            
                String query = "SELECT 1 FROM sessionhandler WHERE usn = ?";

                try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    preparedStatement.setString(1, usn);
                    

                    try (ResultSet resultSet = preparedStatement.executeQuery()) {
                        return resultSet.next(); // Returns true if the record exists
                    }
                }
           
            
        }

        
    }

    /**
     * Innerserver4nm21is034
     */
    // public class Innerserver {

    //     public void nextScene(){
    //         Platform.runLater(()->{
    //             try {
    //                 FXMLLoader loader=new FXMLLoader(getClass().getResource("succes.fxml"));
    //                 Parent root=loader.load();
    //                 //stage=(Stage)((Node).event).getScene().getWindow();
    //                 Scene scene=new Scene(root);
    //                 stage.setScene(scene);
    //                 stage.show();
    //                 //System.out.println(clientip);
    //             } catch (Exception e) {
    //                 // TODO: handle exception
    //             }

    //         });
    //     }

    //     public static void main(String[] args) {
            
    //     }

    // }



    
    public static void main(String[] args) {
        @SuppressWarnings("unused")
        server so=new server();
        
    }


}
