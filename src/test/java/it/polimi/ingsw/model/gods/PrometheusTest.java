package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import static org.junit.Assert.*;

public class PrometheusTest{

    @Test
    public void usePrometheusPowerPlayer1Test() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 8, remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(model.getPhase(), Phase.PROMETHEUS_WORKER);
        SetPrometheus setPrometheus = new SetPrometheus(players[0], remoteView, 0);
        setPrometheus.handler(controller);

        assertEquals(model.getPhase(), Phase.BUILD);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 1);
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 1);
    }

    @Test
    public void usePrometheusPowerPlayer2Test() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 8, remoteView);
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

        PlayerWorker playerWorker = new PlayerWorker(players[0], 2, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 3, 2, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 0, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 1, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 2, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 2, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[1], remoteView2, ch);
        useGodPower.handler(controller);

        assertEquals(model.getPhase(), Phase.PROMETHEUS_WORKER);
        SetPrometheus setPrometheus = new SetPrometheus(players[1], remoteView2, 0);
        setPrometheus.handler(controller);

        assertEquals(model.getPhase(), Phase.BUILD);
        PlayerBuild playerBuild = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMove3 = new PlayerMove(players[1], 0, 0, 1, remoteView2);
        controller.move(playerMove3);

        PlayerBuild playerBuild3 = new PlayerBuild(players[1], players[1].getUsedWorker(), 0, 2, remoteView2);
        controller.build(playerBuild3);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 1);
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 1);
    }

    @Test
    public void prometheusNotUsePowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 8, remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(model.getPhase(), Phase.MOVE);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 0);
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 1);
    }

    @Test
    public void losePrometheusTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 12, 8, remoteView);
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

        PlayerWorker playerWorker = new PlayerWorker(players[0], 1, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 0, 4, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        SetPrometheus setPrometheus = new SetPrometheus(players[0], remoteView, 0);
        setPrometheus.handler(controller);
        PlayerBuild playerBuild = new PlayerBuild(players[0], 1, 1, 2, remoteView);
        controller.build(playerBuild);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], 0, 0, 1, remoteView);
        controller.build(playerBuild5);
        PlayerMove playerMove2 = new PlayerMove(players[0], 1, 1, 2, remoteView);
        controller.move(playerMove2);
        PlayerMove playerMove12 = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove12);
        model.getPlayer(0).getGodCard().handlerMove(model,controller,playerMove12);
        PlayerMove playerMove3 = new PlayerMove(players[0], 0, 0, 0, remoteView);
        controller.move(playerMove3);
        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild2);

        PlayerMove playerMove4 = new PlayerMove(players[1], 0, 3, 0, remoteView2);
        controller.move(playerMove4);
        UseGodPower useGodPower2 = new UseGodPower(players[1], remoteView2, ch);
        useGodPower2.handler(controller);
        PlayerMove playerMove6 = new PlayerMove(players[1], 1, 0, 3, remoteView2);
        controller.move(playerMove6);
        PlayerMove playerMove7 = new PlayerMove(players[1], 0, 4, 0, remoteView2);
        controller.move(playerMove7);
        UseGodPower useGodPower3 = new UseGodPower(players[1], remoteView2, 'n');
        useGodPower3.handler(controller);
        assertEquals(Phase.BUILD,model.getPhase());
        PlayerBuild playerBuild4 = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild4);

        useGodPower.handler(controller);
        PlayerBuild playerBuild12 = new PlayerBuild(players[0], 0, 1, 0, remoteView);
        controller.build(playerBuild12);
        assertEquals(1, model.getBoard().getCell(0, 1).getLevel().getBlockId());
        assertEquals(2, model.getBoard().getCell(1, 0).getLevel().getBlockId());
        assertTrue(players[1].hasWon());
        assertTrue(players[0].getHasLost());
    }
}


