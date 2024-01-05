package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;


/**
 * Abstraction of the view server side
 */
public abstract class View extends Observable<Message> implements Observer<ViewMessage> {
    private final Player player;

    /**
     * Class constructor
     * @param player the player owner of the view
     */
    protected View(Player player){
        this.player = player;
    }

    /**
     *
     * @return the player owner of the view
     */
    protected Player getPlayer(){
        return player;
    }

    /**
     * Used to give a message to the client
     * @param message the object sent as a message
     */
    protected abstract void showMessage(Object message);

    /**
     *
     * @param message a message instance to notify to the observers
     */
    void doAction(Message message){
        notifyObservers(message);
    }

    /**
     * This method send an error message to the client
     * @param message String containing error details
     */
    public void reportError(String message){
        showMessage("ERROR: " + message);
    }


}