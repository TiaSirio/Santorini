package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.model.gods.Athena;
import it.polimi.ingsw.model.gods.Atlas;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;


import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void firstPlayerLoseTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, true);
        Controller controller = new Controller(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, true);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), clientConnection, lobby);

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
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), clientConnection2, lobby);

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
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 0, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 0, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 1, 1, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 1, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        assertTrue(model.getPlayer(0).getHasLost());
    }

    @Test
    public void testIsPlayerTurn() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        assertTrue(model.isPlayerTurn(players[0]));
        assertFalse(model.isPlayerTurn(players[1]));
    }

    @Test
    public void isSimplePlayTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        assertTrue(model.isSimplePlay());
    }

    @Test
    public void updateTurnTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");

        Worker worker = new Worker(new Cell(0,0));
        players[0].setWorkers(worker);
        worker = new Worker(new Cell(0,1));
        players[0].setWorkers(worker);

        worker = new Worker(new Cell(1,0));
        players[1].setWorkers(worker);
        worker = new Worker(new Cell(1,1));
        players[1].setWorkers(worker);

        Model model = new Model(players,true);
        model.updateTurn();
        assertEquals(model.getActualPlayer(),players[1]);
        model.updateTurn();
        assertEquals(model.getActualPlayer(),players[0]);
    }

    @Test
    public void getPlayerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        assertEquals(model.getPlayer(0),players[0]);
        assertEquals(model.getPlayer(1),players[1]);
    }

    @Test
    public void getLeftCardTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        GodCard godCard2 = new Atlas();
        model.addGod(godCard);
        assertEquals(model.getLeftCards(),1);
        model.addGod(godCard1);
        assertEquals(model.getLeftCards(),2);
        model.addGod(godCard2);
        assertEquals(model.getLeftCards(),3);
    }

    @Test
    public void isGodAvailableTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        model.addGod(godCard);
        model.addGod(godCard1);
        assertTrue(model.isGodAvailable(0));
        assertTrue(model.isGodAvailable(1));
    }

    @Test
    public void getGCPlayerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        model.addGod(godCard);
        model.addGod(godCard1);
        model.assignCard(players[1],1);
        model.assignCard(players[0],0);
        assertEquals(players[0],model.getGCPlayer(godCard.getCardGod()));
        assertEquals(players[1],model.getGCPlayer(godCard1.getCardGod()));
    }

    @Test
    public void testUpdatePhase(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Phase phase = Phase.SETWORKER2;
        Worker worker= new Worker(new Cell(0,0));
        players[0].setWorkers(worker);
        worker= new Worker(new Cell(0,1));
        players[0].setWorkers(worker);

        worker= new Worker(new Cell(1,0));
        players[1].setWorkers(worker);
        worker= new Worker(new Cell(1,1));
        players[1].setWorkers(worker);

        model.updatePhase();
        assertSame(phase, model.getPhase());
        model.updatePhase();
        assertNotSame(phase, model.getPhase());
    }

    @Test
    public void testSetPlayerWorker(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Cell cell = new Cell(1,2);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        }, null);
        PlayerWorker playerWorker = new PlayerWorker(players[0],1,2,remoteView);
        model.setPlayerWorker(playerWorker);
        assertTrue(cell.getX()==playerWorker.getX() && cell.getY()==playerWorker.getY());
    }

    @Test
    public void assignCardTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        model.addGod(godCard);
        model.addGod(godCard1);
        model.assignCard(players[0],0);
        assertEquals(godCard,model.getActualPlayer().getGodCard());
    }

    @Test
    public void moveTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        players[0].setWorkers(new Worker(new Cell(1,2)));
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        }, null);
        PlayerMove playerMove = new PlayerMove(players[0],0,2,2,remoteView);
        model.move(playerMove);
        assertTrue(players[0].getWorker(0).getCell().getX()==2 && players[0].getWorker(0).getCell().getX()==2);
    }

    @Test
    public void increaseLevelTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        Cell cell = model.getBoard().getCell(1,2);
        model.increaseLevel(cell,Blocks.LEVEL1);
        assertEquals(1, model.getBoard().getCell(1, 2).getLevel().getBlockId());
    }

    @Test
    public void victoryTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        model.victory(players[0]);
        assertTrue(model.getActualPlayer().hasWon());
    }

    @Test
    public void looseTest(){
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, true);
        model.loose(players[0]);
        assertEquals(2, model.getLeftPlayers());
    }
}

