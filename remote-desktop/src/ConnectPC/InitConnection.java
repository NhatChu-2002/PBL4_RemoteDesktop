package ConnectPC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;


public class InitConnection extends Thread{

    Socket socket = null;
    Socket Chatsocket = null;
    String ipAdress = "";
    int port = 0;

    public InitConnection(String ipAdress, int port) {
        this.ipAdress = ipAdress;
        this.port = port;
        start();
    }


    public void run(){
        DataInputStream verificationFromO = null;
        String msgConnect = "";
        String width = "";
        String height = "";

        try {
            socket = new Socket(ipAdress,port);
            
            verificationFromO = new DataInputStream(socket.getInputStream());
            msgConnect = verificationFromO.readUTF();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        if (msgConnect.equals("valid")) {
            try {
                width = verificationFromO.readUTF();
                height = verificationFromO.readUTF();

            } catch (IOException e) {
                e.printStackTrace();
            }
            CreateFrame createFrame = new CreateFrame(socket, width, height);
            try {
                Chatsocket = new Socket(ipAdress,8080);
                ChatRoom ChatRoom= new ChatRoom(Chatsocket);
//                 Thread t = new Thread(ChatRoom);
//		    t.start();
            } catch (IOException ex) {
                Logger.getLogger(InitConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    

}