package ConnectPC;

import java.awt.BorderLayout;
import java.awt.TextArea;
import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import java.util.zip.*;

import java.io.IOException;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

class CreateFrame extends Thread {

    String width = "", height = "";

    
    //JDesktopPane đại diện cho vùng chứa chính sẽ chứa tất cả các màn hình client được kết nối
    private JDesktopPane desktop = new JDesktopPane();
    private Socket cSocket = null;
    private JInternalFrame interFrame = new JInternalFrame("Server Screen", true, true, true);
    private JPanel cPanel = new JPanel();
    private JFrame frame = new JFrame();
    //
    private JTextArea chatArea = new JTextArea();
    public CreateFrame(Socket cSocket, String width, String height) {

        this.width = width;
        this.height = height;
        this.cSocket = cSocket;
        start();
    }

    
    //vẽ GUI của từng client đã kết nối 
    public void drawGUI() {
        frame.setTitle("client screen");
        frame.add(desktop, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Hiển thị khung hình ở trạng thái tối đa

        frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);		//CHECK THIS LINE
        frame.setVisible(true);
        interFrame.setLayout(new BorderLayout());
        interFrame.getContentPane().add(cPanel, BorderLayout.CENTER);
        interFrame.setSize(100, 100);
        desktop.add(interFrame);
        
        try {
            //hiển thị tối đa khung hình bên trong
            interFrame.setMaximum(true);
        } catch (PropertyVetoException ex) {
            ex.printStackTrace();
        }

        // cho phép xử lý các sự kiện KeyListener
        cPanel.setFocusable(true);
        interFrame.setVisible(true);

    }

    public void run() {
        //dùng để lấy bản sao màn hình
        InputStream inFromC = null;
        //Vẽ giao diện
        drawGUI();

        try {
            inFromC = cSocket.getInputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //nhận các thông số để vẽ màn hình
        new ReceiveScreen(inFromC, cPanel);
        //Gửi sự kiện đến client
        new SendEvents(cSocket, cPanel, width, height);
    }

}
