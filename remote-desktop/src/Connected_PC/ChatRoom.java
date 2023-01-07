package Connected_PC;

import java.awt.AWTEventMulticaster;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.io.InputStreamReader;

public class ChatRoom extends JFrame implements Runnable {

    public Socket socket;
    public ServerSocket serversocket;
    public DataInputStream inFromO;
    public DataOutputStream outToO;
    public BufferedReader br = null;
    public JFrame frame;
    public JTextArea Room;
    public JTextField msgField;
    public JTextArea Joiners;
    public String NickName;

    public ChatRoom() throws IOException {
        DrawGUI();
        serversocket = new ServerSocket(8080);
        this.socket = serversocket.accept();
        //br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.inFromO = new DataInputStream(socket.getInputStream());
        this.outToO = new DataOutputStream(socket.getOutputStream());
        this.run();
    }

    public void DrawGUI() {

        this.setDefaultCloseOperation(3);

        this.frame = new JFrame("Chat Room!");
        this.frame.setSize(480, 400);

        this.frame.setLayout(null);

        this.Room = new JTextArea("");
        this.Room.setBounds(20, 50, 300, 250);
        this.Room.setEditable(false);
        this.frame.add(Room);

        JLabel lsd = new JLabel("Send");
        lsd.setBounds(20, 325, 50, 25);
        this.frame.add(lsd);
        this.msgField = new JTextField("");
        this.msgField.setBounds(100, 325, 200, 25);
        this.frame.add(msgField);

        JButton btnSend = new JButton("Send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Kiểm tra xem người dùng nhập tin nhắn hay chưa
                if (msgField.getText().isEmpty()) {
                    return;
                }
                try {
                    outToO.writeUTF("Connected_PC:" + msgField.getText());
                    Room.setText( Room.getText()+"\n"+"Connected_PC:" + msgField.getText());
                    msgField.setText("");
                } catch (Exception ex) {
                    System.out.println("Error while sendding messeger");
                }
            }
        });
        btnSend.setBounds(320, 325, 80, 25);
        this.frame.add(btnSend);
//        JLabel lj = new JLabel("Joiners");
//        lj.setBounds(620, 10, 50, 50);
//        this.frame.add(lj);
//        this.Joiners = new JTextArea("");
//        this.Joiners.setBounds(330, 50, 120, 250);
//        this.Joiners.setEditable(false);
//        this.frame.add(Joiners);
        frame.setVisible(true);

    }

    @Override
    public void run() {
        while (true) {
            try {
                String ch = inFromO.readUTF();
                String cmd = ch.substring(0, ch.indexOf(":"));
                String msg = ch.substring(ch.indexOf(":") + 1);
                if (cmd.equals("Connect_PC")) {
                    Room.setText("Connect_PC: "+msg + "\n" + Room.getText());
                } else {
                    socket.close();
                }
                
            } catch (Exception e) {
            }
        }
    }

}