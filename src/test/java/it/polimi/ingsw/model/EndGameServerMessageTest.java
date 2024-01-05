package it.polimi.ingsw.model;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EndGameServerMessageTest {
    @Test
    public void endGameServerMessageTest(){
        Player player2 = new Player("Mario");
        Player player = new Player("Maria");
        Player[] players = new Player[2];
        players[0] = player;
        players[1] = player2;
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        ClientConnection clientConnection1 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("Pizza",players[0].getPlayerName(),clientConnection,2,true);
        lobby.addPlayer(players[1].getPlayerName(),clientConnection1);
        ArrayList<ClientConnection> clientConnections = new ArrayList<>();
        clientConnections.add(clientConnection);
        clientConnections.add(clientConnection1);
        ArrayList<String> playerNames = new ArrayList<>();
        playerNames.add(players[0].getPlayerName());
        playerNames.add(players[1].getPlayerName());
        EndGameServerMessage endGameServerMessage = new EndGameServerMessage(lobby,clientConnections,playerNames,2,true);
        assertEquals(endGameServerMessage.getClientConnections(),clientConnections);
        assertEquals(endGameServerMessage.getLobby(),lobby);
        assertEquals(endGameServerMessage.getPlayerNames(),playerNames);
        assertEquals(endGameServerMessage.getLobbyName(),lobby.getLobbyName());
        assertEquals(endGameServerMessage.getNumPlayer(),playerNames.size());
        assertTrue(endGameServerMessage.isSimplePlay());
    }
}
