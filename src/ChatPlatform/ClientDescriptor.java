package ChatPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientDescriptor implements StringConsumer, StringProducer
{
    List<StringConsumer> consumers = null;
    String name = "";

    public ClientDescriptor() {
        consumers = new ArrayList<>();
    }

    @Override
    public void consume(String str) throws IOException {
        if(name == "") {
            name = str;
            for(StringConsumer consumer : consumers) {
                consumer.consume(name + " has joined the chat!");
            }
        }
        else for(StringConsumer consumer : consumers) {
            consumer.consume(name + ": " + str);
        }
    }

    @Override
    public void addConsumer(StringConsumer sc) {
        consumers.add(sc);
    }

    @Override
    public void removeConsumer(StringConsumer sc) {
        consumers.remove(sc);
    }
}