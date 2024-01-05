package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.DrawedCards;
import it.polimi.ingsw.model.messageModel.PickedCard;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinotaurTest {

    @Test
    public void useMinotaurPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 0, 1, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 0, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        assertEquals(model.getPlayer(0).getWorker(0).getCell(), model.getBoard().getCell(0, 1));
        assertEquals(model.getPlayer(1).getWorker(0).getCell(), model.getBoard().getCell(0, 2));
    }
}
