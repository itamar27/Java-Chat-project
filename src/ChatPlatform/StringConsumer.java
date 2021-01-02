package ChatPlatform;

import java.io.IOException;

public interface StringConsumer {

    /*
     * Implement the action which the class should execute
     */
    public void consume(String str) throws IOException;
}
