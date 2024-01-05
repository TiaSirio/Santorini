package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;

/**
 * Abstract class that manages the socket connections coming from clients
 */
public abstract class ClientConnection extends Observable<String> {

    /**
     * This method closes the connection represented by the instance of the class
     */
    public abstract void closeConnection();

    /**
     * This method sends a message to a client
     * @param message is an Object representing the message that needs to be sent to the client
     */
    public abstract void send(Object message);

    /**
     * This method sends a message to a client through another thread
     * @param message is anObject representing the message that needs to be sent to client
     */
    public abstract void asyncSend(Object message);
}
