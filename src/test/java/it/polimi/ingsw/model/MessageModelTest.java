package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MessageModelTest {

    @Test
    public void drawedCardsTest(){
        Player player = new Player("Mario");
        Player player1 = new Player("Luigi");
        Player player2 = new Player("Toad");
        Player[] players = new Player[3];
        players[0] = player;
        players[1] = player1;
        players[2] = player2;
        Model model = new Model(players,true);
        RemoteView remoteView = new RemoteView(player, player1.getPlayerName(), player2.getPlayerName(), new ClientConnection() {
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
        DrawedCards drawedCards = new DrawedCards(player,1,12,3,remoteView);
        assertEquals(drawedCards.getFirst(),1);
        assertEquals(drawedCards.getSecond(),12);
        assertEquals(drawedCards.getThird(),3);
    }

    @Test
    public void clientConfiguratorTest(){
        Player player = new Player("Toad");
        Player player1 = new Player("Mario");
        HashMap<String,String> opponentsNames = new HashMap<>();
        opponentsNames.put("red",player1.getPlayerName());
        ClientConfigurator clientConfigurator = new ClientConfigurator(2,opponentsNames,player);
        assertEquals(clientConfigurator.getNumberOfPlayer(),2);
        assertEquals(clientConfigurator.getOpponentsNames().get("red"),player1.getPlayerName());
        assertEquals(clientConfigurator.getMyself(),player);
    }

    @Test
    public void gameBoardMessage(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Board board = new Board(players);
        GameBoardMessage gameBoardMessage = new GameBoardMessage(board,players[0],"Error", MessageType.MOVE,Phase.MOVE);
        assertEquals(gameBoardMessage.getBoard(),board);
    }

    @Test
    public void gameMessage(){
        Player player = new Player("Toad");
        GameMessage gameMessage = new GameMessage(player,"Error",MessageType.MOVE,Phase.MOVE);
        assertEquals(gameMessage.getPlayer(),player);
    }

    @Test
    public void endGameMessage(){
        Player player = new Player("Toad");
        Player player2 = new Player("Mario");
        HashMap<Player, Integer> values = new HashMap<>();
        values.put(player,1);
        values.put(player2,2);
        EndGameMessage endGameMessage = new EndGameMessage(player,"Error",MessageType.MOVE,Phase.MOVE,values);
        values = endGameMessage.getPodium();
        values.get(player);
    }

    @Test
    public void viewMessageTest(){
        ViewMessage viewMessage = new ViewMessage(MessageType.MOVE,"Moving",Phase.MOVE);
        assertEquals(viewMessage.getMessage(),"Moving");
        assertEquals(viewMessage.getMessageType(),MessageType.MOVE);
        assertEquals(viewMessage.getPhase(),Phase.MOVE);
    }

    @Test
    public void cantSelectPrometheusWorkerTest() {
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 0, 1, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 0, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(model.getPhase(), Phase.PROMETHEUS_WORKER);
        SetPrometheus setPrometheus = new SetPrometheus(players[0], remoteView, 0);
        setPrometheus.handler(controller);

        assertEquals(model.getPhase(), Phase.PROMETHEUS_WORKER);
        SetPrometheus setPrometheus2 = new SetPrometheus(players[0], remoteView, 1);
        setPrometheus2.handler(controller);
    }

    @Test
    public void cantBuildWithPlayerBuildTest() {
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

        PlayerMove playerMove = new PlayerMove(players[0],1,2,0,remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        playerBuild.handler(controller);
    }
}
