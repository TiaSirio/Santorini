package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class PanTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1, 2);
        Cell cell2 = new Cell(3, 3);
        Worker worker = new Worker(cell);
        Worker worker1 = new Worker(cell2);
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        Player[] players = new Player[2];
        players[0] = player;
        players[1] = player2;
        player.setWorkers(worker);
        player2.setWorkers(worker1);
        Model model = new Model(players, true);
        List<Object> winList = new ArrayList<>();
        winList.add(model);
        winList.add(players[0]);
        GodCard godCard = new Pan();
        model.getActualPlayer().setGodCard(godCard);
        model.getActualPlayer().getGodCard().usePower(winList);
        assertTrue(model.getActualPlayer().hasWon());
    }

    @Test
    public void getPhaseTest(){
        GodCard godCard = new Pan();
        Phase phase = godCard.getPhase();
        assertEquals(phase,Phase.MOVE);
    }

    @Test
    public void usePanPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        DrawedCards drawedCards = new DrawedCards(players[0], 7, 0, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 1);
        controller.pickACard(pickedCard);
        assertEquals(model.getPlayer(0).getGodCard().getCardGod(), Gods.PAN);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 0, 2, 1, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMove3);
        PlayerBuild playerBuild3 = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuild3);

        PlayerMove playerMove4 = new PlayerMove(players[1], 1, 3, 3, remoteView2);
        controller.move(playerMove4);
        PlayerBuild playerBuild4 = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 3, remoteView2);
        controller.build(playerBuild4);

        PlayerMove playerMove5 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMove5);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 1, remoteView);
        controller.build(playerBuild5);

        PlayerMove playerMove6 = new PlayerMove(players[1], 1, 4, 3, remoteView2);
        controller.move(playerMove6);
        PlayerBuild playerBuild6 = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 4, remoteView2);
        controller.build(playerBuild6);

        PlayerMove playerMove7 = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMove7);

        assertTrue(model.getPlayer(0).hasWon());
    }
}
