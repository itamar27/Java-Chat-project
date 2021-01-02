package ChatPlatform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MessageBoard implements StringConsumer, StringProducer {
    /*
     * MessageBoard is a class to hold all our ConnectionProxies on the server side
     * Act as a consumer for ClientDescriptor
     * Act as a producer for ConnectionProxy
     */

    List<StringConsumer> consumers;

    public MessageBoard() {
        consumers = new ArrayList<>();
    }

    /*
     * When consume is performed we will send the desired message to all our connection proxies
     */
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