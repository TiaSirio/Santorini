package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArtemisTest {

    @Test
    public void useArtemisPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, remoteView);
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
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerMove playerMoveError = new PlayerMove(players[0], 1, 1, 2, remoteView);
        model.getPlayer(0).getGodCard().handlerMove(model,controller,playerMoveError);
        PlayerMove playerMoveError2 = new PlayerMove(players[0], 0, 0, 0, remoteView);
        model.getPlayer(0).getGodCard().handlerMove(model,controller,playerMoveError2);
        PlayerMove playerMove2 = new PlayerMove(players[0], 0, 0, 2, remoteView);
        controller.move(playerMove2);
        assertEquals(model.getBoard().getCell(0, 2), players[0].getWorker(0).getCell());
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 3, remoteView);
        controller.build(playerBuild);
        assertEquals(((Artemis) playerMove.getPlayer().getGodCard()).getFirstMove(), model.getBoard().getCell(0, 0));
    }

    @Test
    public void cantUseArtemisPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 3, 1, remoteView);
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

        PlayerMove playerMove4 = new PlayerMove(players[1], 1, 1, 0, remoteView2);
        controller.move(playerMove4);
        useGodPower3.handler(controller);
        PlayerBuild playerBuild3 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView2);
        controller.build(playerBuild3);

        PlayerMove playerMove5 = new PlayerMove(players[0], 1, 1, 2, remoteView);
        controller.move(playerMove5);
        UseGodPower useGodPower2 = new UseGodPower(players[0], remoteView, 'y');
        useGodPower2.handler(controller);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 2, remoteView);
        controller.build(playerBuild5);

        PlayerMove playerMove6 = new PlayerMove(players[1], 1, 0, 0, remoteView2);
        controller.move(playerMove6);
        assertEquals(Phase.BUILD,model.getPhase());
    }
}
