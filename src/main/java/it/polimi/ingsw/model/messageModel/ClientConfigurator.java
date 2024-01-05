package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.HashMap;

/**
 * This class represents a particular message used to configure clients at the beginning of the game
 */
public class ClientConfigurator implements Serializable {
    private final int numberOfPlayer;
    private final HashMap<String,String> opponentsNames;
    private final Player me;

    /**
     * Class constructor
     * @param numberOfPlayer is the amount of player in a play
     * @param opponentsNames is a HashMap containing the opponents' names linked to a color
     * @param me is my own instance of player created by the server
     */
    public ClientConfigurator(int numberOfPlayer, HashMap<String,String> opponentsNames, Player me) {
        this.numberOfPlayer = numberOfPlayer;
        this.opponentsNames = opponentsNames;
        this.me = me;
    }

    /**
     *
     * @return the number of players
     */
    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    /**
     *
     * @return a HashMap containing the opponents' names and their gui color
     */
    public HashMap<String,String> getOpponentsNames() {
        return opponentsNames;
    }

    /**
     *
     * @return my own instance of player
     */
    public Player getMyself() {
        return me;
    }
}
