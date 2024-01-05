package it.polimi.ingsw.observer;

/**
 * Observer interface
 * @param <T> generic type
 */
public interface Observer<T> {
    /**
     * This method receives updates from a Observable class
     * @param msg is the message sent
     */
    void update(T msg);
}
