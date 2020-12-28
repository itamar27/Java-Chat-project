package ChatPlatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Flow;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Handler;

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
        this.nickname = "";
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


    public void chatSignUpInit() {

        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);

        frame.setLayout(new FlowLayout());

        panel1.add(message);
        panel2.add(clientDescriptor);
        panel2.add(button);

        frame.add(panel1);
        frame.add(panel2);

        frame.setSize(350, 400);
        frame.setVisible(true);
    }

    public void chatConversationInit() {

        displayText.setBackground(Color.LIGHT_GRAY);
        readText.setBackground(Color.LIGHT_GRAY);

        chatFrame.setLayout(new FlowLayout());

        displayText.add(messageBoard);
        readText.add(textMessage);
        readText.add(sendMessage);

        chatFrame.add(displayText);
        chatFrame.add(readText);

        chatFrame.setSize(400, 600);
        chatFrame.setVisible(true);
    }

    public void start() {
        chatSignUpInit();

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nickname = clientDescriptor.getText();
                runChat();
            }
        });
    }

    public void runChat() {

        closeSignUpComponents();
        chatConversationInit();

        try {
            Socket sock = new Socket("10.0.0.100", 1300);
            ConnectionProxy connectionProxy = new ConnectionProxy(sock);
            addConsumer(connectionProxy);
            connectionProxy.addConsumer(this);
            connectionProxy.consume(nickname);
            connectionProxy.start();

            sendMessage.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String message = textMessage.getText();
                    try {
                        connectionProxy.consume(message);
                        textMessage.setText("");

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            });

            chatFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    try {
                        connectionProxy.consume("disconnect");
                    } catch (IOException ioException) {}

                    super.windowClosing(e);
                    closeChatComponents();
                    closeConnection(sock);
                    System.exit(0);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public void closeConnection(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    /*
     * Class methods to implement interface of StringConsumer and StringProducer
     */
    @Override
    public void consume(String str) throws IOException {
        messageBoard.append(str + "\n");
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