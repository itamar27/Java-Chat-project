package ChatPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBoard implements StringConsumer, StringProducer {
    List<StringConsumer> consumers;

    public MessageBoard() {
        consumers = new ArrayList<>();
    }

    @Override
    public void consume(String str) throws IOException {

        int index = 0;
        for( ; index < consumers.size(); index++) {
            try {
                consumers.get(index).consume(str);
            }catch(IOException e){
                removeConsumer(consumers.get(index));
                index--;
            }
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