package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TritonTest {

    @Test
    public void useTritonPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 12, remoteView);
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
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 2, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerMove playerMove2 = new PlayerMove(players[0], 0, 0, 2, remoteView);
        controller.move(playerMove2);
        useGodPower.handler(controller);
        PlayerMove playerMove3 = new PlayerMove(players[0], 0, 0, 3, remoteView);
        controller.move(playerMove3);
        useGodPower.handler(controller);
        PlayerMove playerMove4 = new PlayerMove(players[0], 0, 0, 4, remoteView);
        controller.move(playerMove4);
        useGodPower.handler(controller);

        PlayerMove playerMove5 = new PlayerMove(players[0], 0, 1, 4, remoteView);
        controller.move(playerMove5);
        useGodPower.handler(controller);
        PlayerMove playerMove6 = new PlayerMove(players[0], 0, 2, 4, remoteView);
        controller.move(playerMove6);
        useGodPower.handler(controller);
        PlayerMove playerMove7 = new PlayerMove(players[0], 0, 3, 4, remoteView);
        controller.move(playerMove7);
        useGodPower.handler(controller);
        PlayerMove playerMove8 = new PlayerMove(players[0], 0, 4, 4, remoteView);
        controller.move(playerMove8);
        useGodPower.handler(controller);

        PlayerMove playerMove9 = new PlayerMove(players[0], 0, 4, 3, remoteView);
        controller.move(playerMove9);
        useGodPower.handler(controller);
        PlayerMove playerMove10 = new PlayerMove(players[0], 0, 4, 2, remoteView);
        controller.move(playerMove10);
        useGodPower.handler(controller);
        PlayerMove playerMove11 = new PlayerMove(players[0], 0, 4, 1, remoteView);
        controller.move(playerMove11);
        useGodPower.handler(controller);
        PlayerMove playerMove12 = new PlayerMove(players[0], 0, 4, 0, remoteView);
        controller.move(playerMove12);
        useGodPower.handler(controller);

        PlayerMove playerMove13 = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMove13);
        useGodPower.handler(controller);
        PlayerMove playerMove14 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMove14);
        useGodPower.handler(controller);
        PlayerMove playerMove15 = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMove15);
        useGodPower.handler(controller);
        PlayerMove playerMove16 = new PlayerMove(players[0], 0, 0, 0, remoteView);
        controller.move(playerMove16);
        assertEquals(players[0].getWorker(0).getCell().getX(), 0);
        assertEquals(players[0].getWorker(0).getCell().getY(), 0);
        useGodPower.handler(controller);
        PlayerMove finishPlayerMove = new PlayerMove(players[0],0,1,1,remoteView);
        controller.move(finishPlayerMove);
    }

    @Test
    public void dontUseTritonPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 12, remoteView);
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
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        assertEquals(model.getPhase(),Phase.BUILD);
    }

    @Test
    public void tritonLoseTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 3, 12, remoteView);
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

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 3, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 3, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 4, 4, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 2, 1, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 2, remoteView);
        controller.move(playerMove);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 1, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 1, 2, 0, remoteView2);
        controller.move(playerMove2);
        UseGodPower useGodPower3 = new UseGodPower(players[1], remoteView2, 'n');
        useGodPower3.handler(controller);
        PlayerBuild playerBuild4 = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView2);
        controller.build(playerBuild4);

        PlayerMove playerMove3 = new PlayerMove(players[0], 1, 2, 2, remoteView);
        controller.move(playerMove3);
        useGodPower.handler(controller);
        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 1, remoteView);
        controller.build(playerBuild2);

        PlayerMove playerMove4 = new PlayerMove(players[1], 1, 3, 1, remoteView2);
        controller.move(playerMove4);
        PlayerBuild playerBuild3 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView2);
        controller.build(playerBuild3);


        PlayerMove playerMove5 = new PlayerMove(players[0], 1, 2, 1, remoteView);
        controller.move(playerMove5);
        UseGodPower useGodPower2 = new UseGodPower(players[0], remoteView, 'n');
        useGodPower2.handler(controller);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild5);

        PlayerMove playerMove6 = new PlayerMove(players[1], 1, 2, 0, remoteView2);
        controller.move(playerMove6);
        UseGodPower useGodPower4 = new UseGodPower(players[1], remoteView2, ch);
        useGodPower4.handler(controller);
        PlayerMove playerMove7 = new PlayerMove(players[1], 1, 1, 0, remoteView2);
        controller.move(playerMove7);
        useGodPower4.handler(controller);
        PlayerMove playerMove8 = new PlayerMove(players[1], 1, 0, 0, remoteView2);
        controller.move(playerMove8);
        useGodPower4.handler(controller);
        controller.move(playerMove7);
        assertTrue(players[0].hasWon());
        assertTrue(players[1].getHasLost());
    }
}
