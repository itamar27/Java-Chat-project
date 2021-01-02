package ChatPlatform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.Socket;

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
    private JScrollPane scroll;

    //Connection Variables
    private StringConsumer consumer;

    /*
     * Class constructor
     */
    public ClientGUI() {
        initComponents();
        this.nickname = "";
    }

    /*
     * Initialize all JSwing components for the User GUI
     */
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
        scroll = new JScrollPane(messageBoard);
    }


    /*
     * Connect all the components of the Chat Sign up frame and set it on
     */
    public void chatSignUpInit() {

        panel1.setBackground(Color.LIGHT_GRAY);
        panel2.setBackground(Color.LIGHT_GRAY);

        frame.setLayout(new FlowLayout());

        panel1.add(message);
        panel2.add(clientDescriptor);
        panel2.add(button);

        frame.add(panel1);
        frame.add(panel2);

        frame.getRootPane().setDefaultButton(button);
        frame.setSize(350, 400);
        frame.setVisible(true);
    }


    /*
     * Connect all the components of the Chat Conversation frame and set it on
     */
    public void chatConversationInit() {

        displayText.setBackground(Color.LIGHT_GRAY);
        readText.setBackground(Color.LIGHT_GRAY);

        chatFrame.setLayout(new FlowLayout());

        displayText.add(scroll);
        readText.add(textMessage);
        readText.add(sendMessage);

        chatFrame.add(displayText);
        chatFrame.add(readText);

        chatFrame.getRootPane().setDefaultButton(sendMessage);
        chatFrame.setSize(400, 600);
        chatFrame.setVisible(true);
    }

    /*
     * Starts the Client GUI application
     */
    public void start() {
        chatSignUpInit();

        button.addActionListener(e -> {
            nickname = clientDescriptor.getText();
            runChat();
        });
    }

    /*
     * Establish connection to the server and manages it
     */
    public void runChat() {

        closeSignUpComponents();
        chatConversationInit();

        try {
            Socket sock = new Socket("127.0.0.1", 1300);
            ConnectionProxy connectionProxy = new ConnectionProxy(sock);
            addConsumer(connectionProxy);
            connectionProxy.addConsumer(this);
            consumer.consume(nickname);
            connectionProxy.start();

            sendMessage.addActionListener(e -> {
                String message = textMessage.getText();
                try {
                    consumer.consume(message);
                    textMessage.setText("");

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            });

            chatFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    try {
                        consumer.consume("disconnect");
                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage());
                    }
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

    /*
     * Close methods to each stage of the application
     */


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
     * When consume is performed the message passed to it will be displayed in the application
     */
    @Override
    public void consume(String str) {
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