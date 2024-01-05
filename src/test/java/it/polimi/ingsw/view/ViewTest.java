package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ViewTest {
    @Test
    public void setAPlayerWithDoActionMethodTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 2, true);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), clientConnection, lobby);


        assertEquals(remoteView.getLobby(), lobby);
        assertEquals(remoteView.getConnection(), clientConnection);
        ClientConnection clientConnection2 = new ClientConnection() {
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        assertEquals(remoteView.getPlayer(),players[0]);
    }
}
