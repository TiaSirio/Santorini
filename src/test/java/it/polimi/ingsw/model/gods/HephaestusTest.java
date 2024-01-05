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

public class HephaestusTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        Cell cellBuilt = new Cell(2,2);
        Cell cell2 = new Cell(3,3);
        Worker worker = new Worker(cell);
        Worker worker1 = new Worker(cell2);
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        Player[] players = new Player[2];
        players[0] = player;
        players[1] = player2;
        player.setWorkers(worker);
        player2.setWorkers(worker1);
        Model model = new Model(players,true);
        List<Object> buildAgainList = new ArrayList<>();
        buildAgainList.add(model);
        Hephaestus godCard = new Hephaestus();
        godCard.setFirstBuilt(cellBuilt);
        model.getActualPlayer().setGodCard(godCard);
        GodCard godCard2 = new Prometheus();
        model.getPlayer(1).setGodCard(godCard2);
        ((Hephaestus)model.getActualPlayer().getGodCard()).setFirstBuilt(cellBuilt);
        Cell cellFirstBuild = ((Hephaestus)model.getActualPlayer().getGodCard()).getFirstBuilt();
        model.getActualPlayer().getGodCard().usePower(buildAgainList);
        assertEquals(cellFirstBuild.getLevel().getBlockId(), Blocks.LEVEL1.getBlockId());
    }

    @Test
    public void useHephaestusPowerTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 5, remoteView);
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

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(((Hephaestus) playerBuild.getPlayer().getGodCard()).getFirstBuilt(), model.getBoard().getCell(0, 2));
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 2);
    }

    @Test
    public void useHephaestusPowerCannotBuildOverDomeTest() {
        //exception.expect(IllegalArgumentException.class);
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 5, remoteView);
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
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 4, 4, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 1, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 2);

        PlayerMove playerMove3 = new PlayerMove(players[1], 0, 2, 0, remoteView2);
        controller.move(playerMove3);

        PlayerBuild playerBuild3 = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView2);
        controller.build(playerBuild3);

        PlayerMove playerMove4 = new PlayerMove(players[0], 0, 1, 1, remoteView);
        controller.move(playerMove4);

        PlayerBuild playerBuild4 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild4);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 4);
    }
}
