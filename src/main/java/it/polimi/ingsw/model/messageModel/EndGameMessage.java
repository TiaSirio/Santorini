package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;

/**
 * This class notifies clients about the ending of the game
 */
public class EndGameMessage extends GameMessage {
    private  final HashMap<Player, Integer> podium;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param podium is a HashMap containing the game players as key and their position as values
     */
    public EndGameMessage(Player player, String message, MessageType messageType, Phase phase, HashMap<Player, Integer> podium) {
        super(player, message, messageType, phase);
        this.podium=podium;
    }

    /**
     *
     * @return return the podium HashMap
     */
    public HashMap<Player, Integer> getPodium(){
        return this.podium;
    }
}
