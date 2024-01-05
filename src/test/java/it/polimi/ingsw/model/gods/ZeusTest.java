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

public class ZeusTest {

    @Test
    public void useZeusPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 13, remoteView);
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

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 1, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getBoard().getCell(0, 1).getLevel().getBlockId(), 1);
    }

    @Test
    public void zeusCantBuildADomeOnHisHeadTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 13, remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 4, 4, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 3, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 1, 1, 2, remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 2, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 1, 2, 2, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 1, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0], 1, 1, 1, remoteView);
        controller.move(playerMove3);
        PlayerBuild playerBuild3 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 1, remoteView);
        controller.build(playerBuild3);

        PlayerMove playerMove4 = new PlayerMove(players[1], 1, 2, 3, remoteView2);
        controller.move(playerMove4);
        PlayerBuild playerBuild4 = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 2, remoteView2);
        controller.build(playerBuild4);

        PlayerMove playerMove5 = new PlayerMove(players[0], 1, 1, 2, remoteView);
        controller.move(playerMove5);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 2, remoteView);
        controller.build(playerBuild5);

        controller.move(playerMove2);
        controller.build(playerBuild2);

        controller.move(playerMove3);
        model.getPlayer(0).getGodCard().checkBuilt(controller,model.getBoard().getCell(1,1),playerBuild3);

        assertEquals(Phase.BUILD,model.getPhase());
        assertEquals(players[0].getWorker(1).getCell(),model.getBoard().getCell(1,1));
        assertEquals(3,model.getBoard().getCell(1,1).getLevel().getBlockId());
    }
}
