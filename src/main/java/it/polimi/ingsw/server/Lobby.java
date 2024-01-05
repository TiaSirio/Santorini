package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

/**
 * This class represents a Lobby, which is a "room" for a game
 */
public class Lobby {
    private final int numPlayers;
    private final String lobbyName;
    private boolean isFull = false;
    private final List<ClientConnection> connections = new ArrayList<>();
    private final Map<String, ClientConnection> waitingConnection = new LinkedHashMap<>();
    private final boolean simplePlay;
    private boolean isGameEnded = false;

    /**
     * Class' constructor
     * @param lobbyName is the selected lobby's name
     * @param playerName is the name of the player who created the lobby
     * @param clientConnection is the player's clientConnection instance
     * @param numPlayers is the selected amount of player for the game
     * @param simplePlay is the selected game mode
     */
    public Lobby(String lobbyName, String playerName, ClientConnection clientConnection, int numPlayers, boolean simplePlay){
        this.numPlayers = numPlayers;
        this.lobbyName = lobbyName;
        this.simplePlay = simplePlay;
        connections.add(clientConnection);
        waitingConnection.put(playerName, clientConnection);
        clientConnection.asyncSend(new ViewMessage(MessageType.WAIT_FOR_START, PlayerMessage.WAIT_PLAYERS, Phase.WAIT_PLAYERS));
    }

    /**
     *
     * @return the lobby's name.
     */
    public String getLobbyName(){
        return this.lobbyName;
    }

    /**
     *
     * @return an integer representing the number players in the lobby.
     */
    public int getNumPlayers(){
        return this.numPlayers;
    }

    /**
     *
     * @return false if the lobby has room for other players
     */
    public boolean isFull(){
        return this.isFull;
    }

    /**
     *
     * @return true if the lobby is made for a game in simple mode
     */
    public boolean isSimplePlay(){
        return this.simplePlay;
    }

    /**
     * This method removes every player from the lobby and closes their connections
     */
    public void closeLobby() {
        if(!isGameEnded) {
            for (int i = connections.size() - 1; i >= 0; i--) {
                ClientConnection clientConnection = connections.get(i);
                clientConnection.closeConnection();
                connections.remove(i);
            }
            waitingConnection.clear();
        }
    }

    /**
     * This function sets the value of a boolean representing that the game is over.
     */
    public void setEndGame(){
        this.isGameEnded = true;
    }

    /**
     * This function is the opposite of the "setEndGame" function above
     */
    public void resetEndGame(){
        this.isGameEnded = false;
    }

    /**
     * This method is used to add a player to the lobby
     *
     * A specific algorithm is used when the lobby is full to configure the beginning of the game
     *
     *  If the lobby is not full yet, a wait message is sent to the clients connected
     *
     * @param playerName is the new player's name
     * @param clientConnection is the ClientConnection instance of the new player
     */
    public void addPlayer(String playerName, ClientConnection clientConnection) {
        connections.add(clientConnection);
        waitingConnection.put(playerName, clientConnection);

        if(waitingConnection.size() == this.numPlayers){    //appena si riempie la stanza
            this.isFull = true;
            List<String> players = new ArrayList<>(waitingConnection.keySet());
            waitingConnection.clear();
            if(this.numPlayers == 2){
                twoPlayer(players);
            } else {
                threePlayer(players);
            }

        } else {
            clientConnection.asyncSend(new ViewMessage(MessageType.WAIT_FOR_START, PlayerMessage.WAIT_PLAYERS, Phase.WAIT_PLAYERS));
        }
    }

    /**
     * This method sends the same message to every player in the lobby.
     * It is only used before the model and controller's instances are created
     * @param o is an object to send Objects through the network
     */
    private void sendAllPlayer(Object o){
        for (ClientConnection connection : connections) {
            connection.send(o);
        }
    }

    /**
     * This method checks if the name of the new player already exists within the lobby
     * @param name is the name chosen by the player
     * @return true if the name doesn't exist
     */
    public boolean isPlayerNameAvailable (String name) {
        return waitingConnection.get(name) == null;
    }

    /**
     * This method configures a game for two players
     * For each player, it creates a remove view instance linked to his connection, the lobby's instance,
     *                his own player object and the name of the opponents.
     *
     * A new model and a new controller objects are then created: SimpleController if it's supposed to be a simple game, a GodCardController otherwise
     *
     * @param players is an arrayList containing the names of the player that joined the lobby
     */
    private void twoPlayer(List<String> players){
        ClientConnection c1,c2;
        Player player1,player2;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView remoteView1,remoteView2;

        c1 = connections.get(0);
        c2 = connections.get(1);
        player1 = new Player(players.get(0));
        player2 = new Player(players.get(1));
        playerArray[0] = player1;
        playerArray[1] = player2;

        remoteView1 = new RemoteView(player1, players.get(1), c1, this);
        remoteView2 = new RemoteView(player2, players.get(0), c2, this);

        Model model = new Model(playerArray, simplePlay);
        Controller controller;
        if(simplePlay){
            controller = new Controller(model);
        }
        else{
            controller=new GodCardController(model);
        }

        model.addObserver(remoteView1);
        model.addObserver(remoteView2);
        remoteView1.addObserver(controller);
        remoteView2.addObserver(controller);

        sendAllPlayer(PlayerMessage.START_PLAY);

        model.initialize();

    }

    /**
     * This method configures a game for three players
     * For each player, it creates a remove view instance linked to his connection, the lobby's instance,
     *                his own player object and the name of the opponents.
     *
     * A new model and a new controller objects are then created: SimpleController if it's supposed to be a simple game, a GodCardController otherwise
     *
     * @param players is an arrayList containing the names of the player that joined the lobby
    **/
    private void threePlayer(List<String> players) {
        ClientConnection c1, c2, c3;
        Player player1, player2, player3;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView remoteView1, remoteView2, remoteView3;

        c1 = connections.get(0);
        c2 = connections.get(1);
        c3 = connections.get(2);
        player1 = new Player(players.get(0));
        player2 = new Player(players.get(1));
        player3 = new Player(players.get(2));


        playerArray[0] = player1;
        playerArray[1] = player2;
        playerArray[2] = player3;

        remoteView1 = new RemoteView(player1, players.get(1), players.get(2), c1, this);
        remoteView2 = new RemoteView(player2, players.get(0), players.get(2), c2, this);
        remoteView3 = new RemoteView(player3, players.get(1), players.get(0), c3, this);


        Model model = new Model(playerArray, simplePlay);
        Controller controller;
        if(simplePlay){
            controller = new Controller(model);
        }
        else{
            controller=new GodCardController(model);
        }
        model.addObserver(remoteView1);
        model.addObserver(remoteView2);
        model.addObserver(remoteView3);

        remoteView1.addObserver(controller);
        remoteView2.addObserver(controller);
        remoteView3.addObserver(controller);

        sendAllPlayer(PlayerMessage.START_PLAY);

        model.initialize();

    }

}
