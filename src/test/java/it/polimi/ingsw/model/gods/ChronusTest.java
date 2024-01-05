package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChronusTest {

    @Test
    public void useChronusPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 9, 0, remoteView);
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 3, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 0, 2, 1, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0], 0, 0, 0, remoteView);
        controller.move(playerMove3);
        PlayerBuild playerBuild3 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild3);

        PlayerMove playerMove4 = new PlayerMove(players[1], 1, 3, 2, remoteView2);
        controller.move(playerMove4);
        PlayerBuild playerBuild4 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild4);

        PlayerMove playerMove5 = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove5);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild5);

        PlayerMove playerMove6 = new PlayerMove(players[1], 1, 3, 3, remoteView2);
        controller.move(playerMove6);
        PlayerBuild playerBuild6 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild6);

        PlayerMove playerMove7 = new PlayerMove(players[0], 0, 0, 0, remoteView);
        controller.move(playerMove7);
        PlayerBuild playerBuild7 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild7);

        PlayerMove playerMove8 = new PlayerMove(players[1], 1, 2, 3, remoteView2);
        controller.move(playerMove8);
        PlayerBuild playerBuild8 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild8);
        assertEquals(model.getBoard().getCell(2, 2).getLevel().getBlockId(), 4);

        PlayerMove playerMove9 = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove9);
        PlayerBuild playerBuild9 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 2, remoteView);
        controller.build(playerBuild9);

        PlayerMove playerMove10 = new PlayerMove(players[1], 1, 3, 3, remoteView2);
        controller.move(playerMove10);
        PlayerBuild playerBuild10 = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 4, remoteView2);
        controller.build(playerBuild10);

        PlayerMove playerMove11 = new PlayerMove(players[0], 0, 0, 2, remoteView);
        controller.move(playerMove11);
        PlayerBuild playerBuild11 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 2, remoteView);
        controller.build(playerBuild11);

        PlayerMove playerMove12 = new PlayerMove(players[1], 1, 3, 4, remoteView2);
        controller.move(playerMove12);
        PlayerBuild playerBuild12 = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 4, remoteView2);
        controller.build(playerBuild12);

        controller.move(playerMove9);
        controller.build(playerBuild9);

        controller.move(playerMove10);
        controller.build(playerBuild10);

        controller.move(playerMove11);
        controller.build(playerBuild11);
        assertEquals(model.getBoard().getCell(1, 2).getLevel().getBlockId(), 4);

        controller.move(playerMove12);
        controller.build(playerBuild12);
        assertEquals(model.getBoard().getCell(4, 4).getLevel().getBlockId(), 4);

        PlayerMove playerMove13 = new PlayerMove(players[0], 1, 2, 0, remoteView);
        controller.move(playerMove13);
        PlayerBuild playerBuild13 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuild13);

        PlayerMove playerMove14 = new PlayerMove(players[1], 0, 3, 1, remoteView2);
        controller.move(playerMove14);
        PlayerBuild playerBuild14 = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild14);

        PlayerMove playerMove15 = new PlayerMove(players[0], 1, 2, 1, remoteView);
        controller.move(playerMove15);
        PlayerBuild playerBuild15 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuild15);

        PlayerMove playerMove16 = new PlayerMove(players[1], 0, 4, 1, remoteView2);
        controller.move(playerMove16);
        PlayerBuild playerBuild16 = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild16);
        assertTrue(players[1].hasWon());
    }
}
