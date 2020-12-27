package ChatPlatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;

public class ClientGUI implements StringConsumer, StringProducer {

    //Registration components
    private JFrame frame;
    private JButton button;
    private JTextField clientDescriptor;
    private JLabel message;
    private JPanel panel1;
    private JPanel panel2;
    private String nickname;

    //Chat components
    private JFrame chatFrame;
    private JPanel displayText;
    private JPanel readText;
    private JButton sendMessage;
    private JTextArea messageBoard;
    private JTextField textMessage;


    //Connection Variables
    private StringConsumer consumer;


    /*
     * Class constructor
     */
    public ClientGUI() {

        initComponents();
        nickname = "";
    }

    public void initComponents() {
        //chat sign up components
        frame = new JFrame("ChatPlatform");
        button = new JButton("Submit");
        clientDescriptor = new JTextField(10);
        message = new JLabel("Please enter your nick name");
        panel1 = new JPanel();
        panel2 = new JPanel();

        //chat active components
        chatFrame = new JFrame("Friendly Chat");
        displayText = new JPanel();
        readText = new JPanel();
        sendMessage = new JButton("send");
        textMessage = new JTextField(20);
        messageBoard = new JTextArea(30, 30);
        messageBoard.setEditable(false);

    }

    public void closeSignUpComponents() {
        frame.setVisible(false);
        frame = null;
        button = null;
        clientDescriptor = null;
        message = null;
        panel1 = null;
        panel2 = null;
    }

    public void closeChatComponents() {
        chatFrame = null;
        sendMessage = null;
        textMessage = null;
        messageBoard = null;
        displayText = null;
        readText = null;
    }

    public void chatSignUp() {

        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);

        frame.setLayout(new FlowLayout());

        panel1.add(message);
        panel2.add(clientDescriptor);
        panel2.add(button);

        frame.add(panel1);
        frame.add(panel2);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                nickname = clientDescriptor.getText();
            }
        });

        frame.setSize(350, 400);
        frame.setVisible(true);

    }

    public void start() {

        displayText.setBackground(Color.LIGHT_GRAY);
        readText.setBackground(Color.LIGHT_GRAY);

        chatFrame.setLayout(new FlowLayout());

        displayText.add(messageBoard);
        readText.add(textMessage);
        readText.add(sendMessage);

        chatFrame.add(displayText);
        chatFrame.add(readText);

        chatFrame.setSize(350, 400);
        chatFrame.setVisible(true);


    }

    public boolean isNickName() {
        if (nickname.equals(""))
            return false;
        else
            return true;

    }


    /*
     * Class main to run the GUI program
     */
    public static void main(String[] args) {

        ClientGUI clientGui = new ClientGUI();

        clientGui.chatSignUp();

        while (true) {
            synchronized (clientGui.nickname) {
                if (clientGui.nickname != "")
                    break;
            }
        }

        try {
            clientGui.closeSignUpComponents();
            clientGui.start();
            Socket sock = new Socket("192.168.125.1", 1300);
            ConnectionProxy connectionProxy = new ConnectionProxy(sock);


            clientGui.addConsumer(connectionProxy);
            connectionProxy.addConsumer(clientGui);
            connectionProxy.consume(clientGui.nickname);
            connectionProxy.run();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            clientGui.closeChatComponents();
        }


    }


    /*
     * Class methods to implement interface of StringConsumer and StringProducer
     */

    @Override
    public void consume(String str) throws IOException {

        synchronized (messageBoard) {
            messageBoard.append(str);
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumer = sc;
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumer = null;
    }
}