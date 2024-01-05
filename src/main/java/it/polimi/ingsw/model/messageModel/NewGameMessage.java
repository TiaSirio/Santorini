package it.polimi.ingsw.model.messageModel;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.*;
import it.polimi.ingsw.view.View;

/**
 * This class represents the message containing the choice for starting a new play
 */
public class NewGameMessage extends Message {
    private final char choice;
    private final ClientConnection clientConnection;
    private final Lobby lobby;

    /**
     * {@inheritDoc}
     * @param choice is the selection
     * @param clientConnection is the socket connection of the player which made this choice
     * @param lobby is the old lobby instance of the previews game
     */
    public NewGameMessage(Player player, View view, char choice, ClientConnection clientConnection, Lobby lobby) {
        super(player, view);
        this.choice = choice;
        this.clientConnection = clientConnection;
        this.lobby = lobby;
    }

    /**
     *
     * @return the lobby's instance
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     *
     * @return a char representing the player's choice
     */
    public char getChoice(){
        return this.choice;
    }

    /**
     *
     * @return player client's socket connection
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * Perform the controller end game actions by calling the controller's function
     * @param controller the game controller's instance
     */
    @Override
    public void handler(Controller controller) {
        controller.endGame(this);
    }
}
