package ChatPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBoard implements StringConsumer, StringProducer
{
    List<StringConsumer> consumers = null;

    public MessageBoard() {
        consumers = new ArrayList<>();
    }

    @Override
    public void consume(String str) throws IOException {
        for(StringConsumer consumer : consumers) {
            consumer.consume(str);
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