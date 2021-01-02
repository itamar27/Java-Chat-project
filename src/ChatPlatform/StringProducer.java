package ChatPlatform;

public interface StringProducer {

    /*
     * Implement this method by adding StringConsumer to a List or a single variable
     */
    public void addConsumer(StringConsumer sc);

    /*
     * Implement this method by removing StringConsumer from a List or setting a variable to null
     */
    public void removeConsumer(StringConsumer sc);
}
