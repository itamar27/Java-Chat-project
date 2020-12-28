package ChatPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientDescriptor implements StringConsumer, StringProducer {
    StringConsumer consumer;
    String name = "";

    public ClientDescriptor() {
    }

    @Override
    public void consume(String str) throws IOException {
        if(str.equals("disconnect")){
            name = "";
            consumer.consume(name +" has left the chat");
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