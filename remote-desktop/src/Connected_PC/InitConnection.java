package Connected_PC;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;

public class InitConnection extends Thread{
    ServerSocket socket = null;
    DataOutputStream verifyToC = null;
    int port;
    String width = "";
    String height = "";
    
    
    public InitConnection(int port) {
        this.port = port;
        start();
    }

    
public void run(){
        Robot robot = null;
        Rectangle rectangle = null;
        try {
            
            socket = new ServerSocket(port); 
               drawGUI();
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice gDev = gEnv.getDefaultScreenDevice();

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            String width = "" + dim.getWidth();
            String height = "" + dim.getHeight();
            rectangle = new Rectangle(dim);
            robot = new Robot(gDev);
            
            while (true) {
                Socket sc = socket.accept();
                
                verifyToC = new DataOutputStream(sc.getOutputStream());

                verifyToC.writeUTF("valid");
                verifyToC.writeUTF(width);
                verifyToC.writeUTF(height);
                new SendScreen(sc, robot, rectangle);
                new ReceiveEvents(sc, robot);
                new ChatRoom();
          
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
}

    
    
        private void drawGUI() {
        JFrame frame = new JFrame();
        JButton button = new JButton("Close");
        button.setBounds(140, 100, 100, 25);
        frame.add(button);


        frame.setLayout(null);

        frame.setSize(400, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        button.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        }
        );

        frame.setVisible(true);
    }

}
