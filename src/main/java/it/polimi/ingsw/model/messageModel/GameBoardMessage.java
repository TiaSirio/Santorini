package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

/**
 * This class represents messages sent in a player's turn with a board updated
 */
public class GameBoardMessage extends GameMessage{
    private final Board board;

    /**
     * {@inheritDoc}
     * @param board is a cloned instance of the board from the model
     */
    public GameBoardMessage(Board board, Player player, String message, MessageType msg, Phase phase) {
        super(player, message, msg, phase);//questo invia un messaggio a tutti
        this.board = board;

    }

    /**
     *
     * @return the board instance
     */
    public Board getBoard() {
        return board;
    }
}
