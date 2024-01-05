package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

/**
 * This class represents messages which are sent within a player's turn
 */
public class GameMessage extends ViewMessage{
    private final Player player;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param player is the turn player
     */
    public GameMessage(Player player, String message, MessageType messageType, Phase phase){
        super(messageType, message, phase);
        this.player = player;
    }

    /**
     *
     * @return the player instance
     */
    public Player getPlayer(){
        return player;
    }
}
