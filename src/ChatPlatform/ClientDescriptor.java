package ChatPlatform;

import java.io.IOException;

public class ClientDescriptor implements StringConsumer, StringProducer {
    /*
     * This class holds information about our user in the server side.
     * Act as a consumer for ConnectionProxy
     * Act as a producer for MessageBoard
     */

    StringConsumer consumer;
    String name = "";


    public ClientDescriptor() {
    }

    /*
     * If consume is performed, the ClientDescriptor should add the
     * relevant info to the message and pass it to his consumer
     */
    @Override
    public void consume(String str) throws IOException {
        if(str.equals("disconnect")){
            consumer.consume(name +" has left the chat");
            name = "";
            removeConsumer(consumer);
        }
       else if (name.equals("")) {
            name = str;
            consumer.consume(name + " has joined the chat!");
        } else
            consumer.consume(name + ": " + str);
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