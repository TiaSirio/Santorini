package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.exceptions.FullWorkerException;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.model.gods.Apollo;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class PlayerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void EqualsTest(){
        Player player = new Player("Toad");
        Player player1 = new Player(null);
        assertFalse(player.equals(null));
        assertFalse(player.equals(new Object()));
        assertFalse(player.equals(player1));
        assertFalse(player1.equals(player));
    }

    @Test
    public void getPlayerNameTest() {
        Player player = new Player("Mario");
        assertEquals("Mario", player.getPlayerName());
    }

    @Test
    public void getGodCardTest() {
        Player player = new Player("Luigi");
        player.setGodCard(Gods.getGod(2));
        assertEquals(Gods.getGod(2), player.getGodCard());
    }

    @Test
    public void getUnusedWorkerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
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
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,4,remoteView);
        controller.setPlayerWorker(playerWorker2);

        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],3,4,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],4,4,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],0,1,1,remoteView);
        controller.move(playerMove);
        assertEquals(players[0].getUnusedWorker(),1);
    }

    @Test
    public void resetUsePowerTest(){
        Player player = new Player("Luigi");
        GodCard godCard = new Apollo();
        player.setGodCard(godCard);
        player.setUsePower(true);
        player.resetUsePower();
        assertFalse(player.getUsePower());
    }

    @Test
    public void setGodCard(){
        Player player = new Player("Luigi");
        player.setGodCard(Gods.getGod(1));
        assertEquals(Gods.getGod(1), player.getGodCard());
    }

    @Test
    public void setWorkers() {
        Cell cell = new Cell(1,1);
        Player player = new Player("Mario");
        Worker worker = new Worker(cell);
        player.setWorkers(worker);
        assertEquals(worker, player.getWorker(0));
    }

    @Test
    public void getWorker() {
        Cell cell = new Cell(1,1);
        Cell cell2 = new Cell(2,2);
        Player player = new Player("Mario");
        Worker worker = new Worker(cell);
        Worker worker2 = new Worker(cell2);
        player.setWorkers(worker);
        player.setWorkers(worker2);

        assertNotEquals(worker2, player.getWorker(0));
        assertNotEquals(worker, player.getWorker(1));
        assertEquals(worker, player.getWorker(0));
        assertEquals(worker2, player.getWorker(1));
    }

    @Test
    public void testSetVictory() {
        Player player = new Player("Mario");
        player.setVictory(true);
        assertTrue(player.hasWon());
    }

    @Test
    public void testHasWon() {
        Player player = new Player("Mario");
        assertFalse(player.hasWon());
    }

    @Test
    public void setWorkersException(){
        exception.expect(FullWorkerException.class);
        Player player = new Player("Mario");
        Worker worker = new Worker(new Cell(1,1));
        player.setWorkers(worker);
        Worker worker1 = new Worker(new Cell(2,1));
        player.setWorkers(worker1);
        Worker worker2 = new Worker(new Cell(3,1));
        player.setWorkers(worker2);
    }

    @Test
    public void getWorkerException(){
        exception.expect(IndexOutOfBoundsException.class);
        Cell cell = new Cell(1,1);
        Cell cell2 = new Cell(2,2);
        Player player = new Player("Mario");
        Worker worker = new Worker(cell);
        Worker worker2 = new Worker(cell2);
        player.setWorkers(worker);
        player.setWorkers(worker2);
        player.getWorker(3);
    }

    @Test
    public void resetTest(){
        Player player = new Player("Marco");
        player.setHasLost(true);
        player.setVictory(true);
        GodCard godCard = new Apollo();
        player.setGodCard(godCard);
        Worker worker1 = new Worker(new Cell(1,2));
        Worker worker2 = new Worker(new Cell(2,2));
        player.setWorkers(worker1);
        player.setWorkers(worker2);
        player.reset();
        assertFalse(player.hasWon());
        assertFalse(player.getHasLost());
        assertNull(player.getGodCard());
    }
}