package ChatPlatform;

import java.io.*;
import java.net.Socket;


public class ConnectionProxy extends Thread implements StringConsumer, StringProducer {
    /*
     * ConnectProxy class holds all the connection info about the other side and
     * manages all the messages received and that need to be sent
     * Act as a consumer for MessageBoard and ClientGUI
     * Act as a producer for ClientDescriptor and ClientGUI
     */

    private Socket socket = null;
    private StringConsumer consumer;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public ConnectionProxy(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    /*
     * When consume is performed the string received should be delivered to the other side with UTF
     */
    @Override
    public void consume(String str) throws IOException {
        dos.writeUTF(str);
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumer = sc;
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumer = null;
    }

    /*
     * For the threaded write and read we will use the run method to perform a loop for reading with UTF
     */
    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                String readMsg = dis.readUTF();
                consumer.consume(readMsg);
            }
            this.removeConsumer(consumer);
        } catch (Exception e) {
        }
    }
}