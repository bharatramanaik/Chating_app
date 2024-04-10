import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;


public class succontrol {

    @FXML
    private Label alerts;

    @FXML
    private Label myname;
    
    static String myusn;
    static String myip;
    static int myport;
    static String clusn;
    static String clip;
    static int clport;
    ServerSocket server;
    Socket socket;
    InputStream input;
    OutputStream output;
    ObjectOutputStream obwrite;
    ObjectInputStream obread;

    Connection conn;
    ResultSet rslt;
    PreparedStatement prst;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    static String mynamee;
    static String mybranch;
    
    public void sttext(String usnn){
        //myname.setText(usnn);
        myusn=usnn;
        
    }

    @FXML
    void showprof(ActionEvent event) {
        PreparedStatement pr1;
        ResultSet rs1;
        try {
            System.out.println(myusn);
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/chatting_app", "root", "Example@2024#");
            pr1=conn.prepareStatement("SELECT username,branch FROM userdet WHERE usn=?");
            pr1.setString(1, myusn);
            rs1=pr1.executeQuery();
            
            if (rs1.next()) {
                mynamee=rs1.getString(1);
                mybranch=rs1.getString(2);
                myname.setText(mynamee+"\n"+mybranch+"\n"+myusn);
            } else {
                myname.setText("usnn");
                
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    
    @FXML
    void chats(ActionEvent event) {
        try {
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/chatting_app", "root", "Example@2024#");
            prst=conn.prepareStatement("SELECT usn,ipaddr,port FROM sessionhandler WHERE usn <> ?");
            prst.setString(1, myusn);
            rslt=prst.executeQuery();
            if (rslt.next()) {
                clusn=rslt.getString(1);
                clip=rslt.getString(2);
                clport=rslt.getInt(3);
                prst.close();
                rslt.close();
                prst=conn.prepareStatement("SELECT * FROM sessionhandler WHERE usn=?");
                prst.setString(1, myusn);
                rslt=prst.executeQuery();
                if (rslt.next()) {
                    myip=rslt.getString(2);
                    myport=rslt.getInt(3);
                    
                    try {
                        FXMLLoader loader=new FXMLLoader(getClass().getResource("chating_panel.fxml"));
                        root = loader.load();
                        stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                        scene=new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

            } else {
                alerts.setText("No one is there to chat :(");
            }
        } catch (SQLException e) {
            // TODO: handle exception
        }
    }

     @FXML
    void logout(ActionEvent event) {

        try {
            System.out.println(myusn);
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/chatting_app", "root", "Example@2024#");
            prst=conn.prepareStatement("DELETE FROM sessionhandler WHERE usn=?");
            prst.setString(1, myusn);
            prst.executeUpdate();
            FXMLLoader loader=new FXMLLoader(getClass().getResource("loginpage.fxml"));
            root = loader.load();
            stage=(Stage)((Node)event.getSource()).getScene().getWindow();
            scene=new Scene(root);
            stage.setScene(scene);
            stage.show();
                            
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

}
