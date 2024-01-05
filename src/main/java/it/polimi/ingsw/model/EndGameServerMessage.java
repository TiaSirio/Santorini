package it.polimi.ingsw.model;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;

import java.util.ArrayList;

/**
 * This class represents a message catch by the server to start a new play
 */
public class EndGameServerMessage {
    private final Lobby lobby;
    private final ArrayList<ClientConnection> clientConnections;
    private final ArrayList<String> playerNames;
    private final int numPlayer;
    private final boolean simplePlay;

    /**
     * Constructor of the class
     * @param lobby is the actual game's lobby.
     * @param clientConnections is an ArrayList containing the connections of the players in this game.
     * @param playerNames is an ArrayList containing the names of the players in the game.
     * @param numPlayer is an int representing the number of players.
     * @param simplePlay is true if the game mode is set to simpleMode.
     */
    public EndGameServerMessage(Lobby lobby, ArrayList<ClientConnection> clientConnections, ArrayList<String> playerNames, int numPlayer, boolean simplePlay){
     this.clientConnections = clientConnections;
     this.lobby = lobby;
     this.playerNames = playerNames;
     this.numPlayer = numPlayer;
     this.simplePlay = simplePlay;

    }

    /**
     * @return the actual game's lobby's name.
     */
    public String getLobbyName() {
        return lobby.getLobbyName();
    }

    /**
     * @return the actual game's lobby.
     */
    public Lobby getLobby(){
        return lobby;
    }

    /**
     * @return an ArrayList containing the connections of the players in this game.
     */
    public ArrayList<ClientConnection> getClientConnections() {
        return clientConnections;
    }

    /**
     * @return an ArrayList containing the names of the players in the game.
     */
    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    /**
     * @return an int representing the number of players.
     */
    public int getNumPlayer() {
        return numPlayer;
    }

    /**
     * @return true if the game mode is set to simpleMode.
     */
    public boolean isSimplePlay() {
        return simplePlay;
    }
}
