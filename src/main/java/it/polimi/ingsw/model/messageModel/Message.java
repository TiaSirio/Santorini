package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.io.Serializable;

/**
 * This class in the generic abstract class for messages from the remote view to the controller
 */
public abstract class Message implements Serializable {
    protected final Player player;
    protected final View view;

    /**
     *
     * @param player is the turn player which is sending the message
     * @param view is the remote view instance representing the client on the server
     */
    public Message(Player player, View view){
        this.player = player;
        this.view = view;
    }

    /**
     *
     * @return the player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return the view instance
     */
    public View getView() {
        return view;
    }

    /**
     * Abstract method which has to be overrode by subclasses
     * It is used to handle actions based on the message's subclass type
     * @param controller is the game controller instance
     */
    public abstract void handler(Controller controller);
}
