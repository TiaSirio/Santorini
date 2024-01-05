package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.FullLobbyException;
import it.polimi.ingsw.exceptions.InvalidLobbyException;
import it.polimi.ingsw.exceptions.UnavailablePlayerNameException;
import it.polimi.ingsw.model.EndGameServerMessage;
import it.polimi.ingsw.utils.LobbyExceptionMessage;
import it.polimi.ingsw.exceptions.NoLobbyException;
import it.polimi.ingsw.utils.ConnectionMessage;
import it.polimi.ingsw.utils.PlayerMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is the main server's class
 */
public class Server {
    private static final int PORT = 15986;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<ClientConnection, Lobby> lobbyConnections = new HashMap<>();
    private final List<Lobby> lobbies = new ArrayList<>();
    private final Map<Lobby, ArrayList<ClientConnection>> playerInLobby = new HashMap<>();

    /**
     * Class constructor
     * It creates the server's socket
     * @throws IOException if the socket can't be created
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    /**
     * This method removes and closes a client connection
     * @param connection is the instance of the client's socket connection
     */
    public synchronized void deregisterConnection(ClientConnection connection){
        Lobby lobby = this.lobbyConnections.get(connection);
        this.lobbyConnections.get(connection).closeLobby();
        ArrayList<ClientConnection> toRemove = playerInLobby.get(lobby);
        for (ClientConnection clientConnection : toRemove) {
            lobbyConnections.remove(clientConnection);
        }
        this.playerInLobby.remove(lobby);
        this.lobbies.remove(lobby);
    }

    /**
     * @return the number of active lobbies in the server
     */
    public synchronized int getLobbiesCount(){
        return this.lobbies.size();
    }

    /**
     *
     * @return a string containing the lobby's id, the name, the number of players in it and the game mode
     * @throws NoLobbyException if there is no lobby available
     */
    public synchronized String getLobbiesNames() throws NoLobbyException {
        if(this.lobbies.size() != 0) {
            StringBuilder names = new StringBuilder(PlayerMessage.JOIN_LOBBY + "\n0 - Go Back\n");
            for (int i = 0; i < this.lobbies.size(); i++) {
                Lobby lobby = this.lobbies.get(i);
                String lobbyPlayers;
                int playerInLobby = this.playerInLobby.get(lobby).size();
                if
                    (lobby.isFull()) lobbyPlayers = " [FULL] ";
                else {
                    lobbyPlayers = "\t[" + playerInLobby + "/" + lobby.getNumPlayers() + "]";
                    if(lobby.isSimplePlay()){
                        lobbyPlayers+="\tS";
                    }
                    else
                        lobbyPlayers+="\tH";
                }


                names.append(i + 1).append(" - ").append(lobby.getLobbyName()).append(lobbyPlayers).append("\n");
            }
            return names.toString();
        } else {
            throw new NoLobbyException(LobbyExceptionMessage.NO_LOBBY);
        }
    }

    /**
     * This function creates a new lobby in the server
     * It also adds the player to the playerInLobby map and lobbyConnections map
     * @param lobbyName is the chosen lobby's name
     * @param clientConnection is he client's socket instance
     * @param playerName is the name of the player that created the lobby
     * @param numPlayer is the chosen number of player for the lobby
     * @param simplePlay is a flag representing that the game is without any god card
     */
    public synchronized void addLobby(String lobbyName, ClientConnection clientConnection, String playerName, int numPlayer, boolean simplePlay){
        Lobby lobby = new Lobby(lobbyName, playerName, clientConnection, numPlayer, simplePlay);
        this.lobbies.add(lobby);
        this.lobbyConnections.put(clientConnection, lobby);
        ArrayList<ClientConnection> arr = new ArrayList<>();
        arr.add(clientConnection);
        this.playerInLobby.put(lobby, arr);
    }

    /**
     * This function adds a new player to an existing lobby
     * It also adds the player to the playerInLobby map and lobbyConnections map
     * @param lobbyId is the lobby's id
     * @param clientConnection is the client's socket connection instance
     * @param playerName is the player's name
     * @throws FullLobbyException if the selected lobby is already full
     * @throws InvalidLobbyException if the selected lobby doesn't exist
     */
    public synchronized void joinLobby(int lobbyId, ClientConnection clientConnection, String playerName) throws FullLobbyException, InvalidLobbyException {
        Lobby lobby;
        try {
            lobby = this.lobbies.get(lobbyId - 1);
        } catch (Exception e){
            throw new InvalidLobbyException(LobbyExceptionMessage.INVALID_LOBBY);
        }

        if (!lobby.isFull()) {
            if(!lobby.isPlayerNameAvailable(playerName)){
                throw new UnavailablePlayerNameException(LobbyExceptionMessage.UNAVAILABLE_NAME);
            }
            lobby.addPlayer(playerName, clientConnection);
            this.lobbyConnections.put(clientConnection, lobby);
            this.playerInLobby.get(lobby).add(clientConnection);
        } else {
            throw new FullLobbyException(LobbyExceptionMessage.FULL_LOBBY);
        }
    }

    /**
     * This function adds the players who decided to play another game to a new lobby
     * @param endGameServerMessage is the message that holds every information needed for the new game
     * @see EndGameServerMessage
     */
    public synchronized void addLobbyEndGame(EndGameServerMessage endGameServerMessage){
        deleteOldLobby(endGameServerMessage);
        Lobby lobbyNewGame = new Lobby(endGameServerMessage.getLobbyName(), endGameServerMessage.getPlayerNames().get(0), endGameServerMessage.getClientConnections().get(0), endGameServerMessage.getNumPlayer(), endGameServerMessage.isSimplePlay());
        this.lobbies.add(lobbyNewGame);
        this.lobbyConnections.put(endGameServerMessage.getClientConnections().get(0), lobbyNewGame);
        ArrayList<ClientConnection> arr = new ArrayList<>();
        arr.add(endGameServerMessage.getClientConnections().get(0));
        this.playerInLobby.put(lobbyNewGame, arr);
        if (endGameServerMessage.getPlayerNames().size() > 1){
            joinLobby(lobbies.size(),endGameServerMessage.getClientConnections().get(1),endGameServerMessage.getPlayerNames().get(1));
        }
    }

    /**
     * When a new game is created after the old one is over, this method deletes the old lobby from the server
     * @param endGameServerMessage endGameServerMessage the particular message that contains all info for the new play
     * @see EndGameServerMessage
     */
    private synchronized void deleteOldLobby(EndGameServerMessage endGameServerMessage) {
        try{
            for (Map.Entry<ClientConnection,Lobby> clientConnections: lobbyConnections.entrySet()) {
                if (clientConnections.getValue() == endGameServerMessage.getLobby())
                    lobbyConnections.remove(clientConnections.getKey());
            }
       }catch (Exception e){
            //e.printStackTrace();
        }


        lobbies.remove(endGameServerMessage.getLobby());
        playerInLobby.remove(endGameServerMessage.getLobby());
    }

    /**
     * {@inheritDoc}
     * This function opens the socket and submits new accepted connection to the runner
     */
    public void run(){
        System.out.println("Server listening on port: " + PORT);
        do{
            try {
                Socket socket = serverSocket.accept();
                SocketClientConnection connection = new SocketClientConnection(socket, this);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println(ConnectionMessage.ERROR);
            }
        }while(true);
    }

}
