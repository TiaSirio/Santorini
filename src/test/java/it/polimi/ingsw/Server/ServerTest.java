package it.polimi.ingsw.Server;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import java.io.IOException;

public class ServerTest{

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void noLobbyExceptionTest() throws IOException {
        Server server = new Server();
        Player player = new Player("Toad");
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
        //exception.expect(InvalidLobbyException.class);
        //server.joinLobby(1,clientConnection,player.getPlayerName());
        server.addLobby("Pippo",clientConnection,player.getPlayerName(),2,false);

        //server.joinLobby(0,clientConnection4,player3.getPlayerName());
        assertEquals(server.getLobbiesCount(),1);
        assertEquals(server.getLobbiesNames(),"Select which lobby you want to join: \n" +
                        "0 - Go Back\n" +
                        "1 - Pippo\t[1/2]\tH\n"
                //"]");
        );
        Player player1 = new Player("Luigi");
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
        server.joinLobby(1,clientConnection1,player1.getPlayerName());
        assertEquals(server.getLobbiesNames(),"Select which lobby you want to join: \n" +
                "0 - Go Back\n" +
                "1 - Pippo [FULL] \n");
        Player player2 = new Player("Mario");
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
        server.deregisterConnection(clientConnection1);
        Player player3 = new Player("Pluto");
        ClientConnection clientConnection3 = new ClientConnection() {
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
        server.addLobby("Facebook",clientConnection3,player3.getPlayerName(),2,true);
        assertEquals(server.getLobbiesNames(),"Select which lobby you want to join: \n" +
                "0 - Go Back\n" +
                "1 - Facebook\t[1/2]\tS\n");
        exception.expect(InvalidLobbyException.class);
        server.joinLobby(10,clientConnection2,player2.getPlayerName());
        //server.joinLobby(0,clientConnection2,player2.getPlayerName());
    }
}
