package ChatPlatform;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionProxy extends Thread implements StringConsumer, StringProducer
{
    private Socket socket = null;
    private List<StringConsumer> consumers = null;
    private DataInputStream dis = null;
    private DataOutputStream dos = null;

    public ConnectionProxy(Socket socket) throws IOException {
        consumers = new ArrayList<>();
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    public void consume(String str) throws IOException {
        dos.writeUTF(str);
    }

    @Override
    public void addConsumer(StringConsumer sc) {consumers.add(sc);}

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumers.remove(sc);
    }

    @Override
    public void run() {
        try {
            while (!socket.isClosed()) {
                String readMsg = dis.readUTF();
                for(StringConsumer consumer : consumers) {
                    consumer.consume(readMsg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}