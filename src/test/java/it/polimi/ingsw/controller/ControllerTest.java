package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.NewGameMessage;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.*;

public class ControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void godCardTest(){
        GodCard godCard = new GodCard(Gods.APOLLO,Phase.MOVE);
        godCard.usePower(null);
        assertFalse(godCard.equals(new Object()));
    }

    @Test
    public void setPlayerWorkerTest(){
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
        assertFalse(controller.getModel().getBoard().getCell(1,2).isFree());
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,4,remoteView);
        controller.setPlayerWorker(playerWorker2);
        assertFalse(controller.getModel().getBoard().getCell(1,4).isFree());

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
        assertFalse(controller.getModel().getBoard().getCell(3,4).isFree());

        PlayerWorker playerWorker4 = new PlayerWorker(players[1],4,4,remoteView1);
        controller.setPlayerWorker(playerWorker4);
        assertFalse(controller.getModel().getBoard().getCell(4,4).isFree());
    }

    @Test
    public void SetWorkerException(){
        exception.expect(IllegalArgumentException.class);
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
        assertFalse(controller.getModel().getBoard().getCell(-1,2).isFree());
    }







    @Test
    public void moveTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        assertFalse(controller.getModel().getBoard().getCell(1,1).isFree());
        assertTrue(controller.getModel().getBoard().getCell(1,2).isFree());
    }

    @Test
    public void increaseLevelTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        },null);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],3,4,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],4,4,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],0,1,1,remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),1,2,remoteView);
        controller.build(playerBuild);
        assertEquals(1, controller.getModel().getBoard().getCell(1, 2).getLevel().getBlockId());
    }

    @Test
    public void playerCannotMoveTest(){

        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        },null);
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],0,3,remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],3,0,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],1,3,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_a = new PlayerMove(players[0],1,0,2,remoteView);
        controller.move(playerMoveWorker1_a);

        PlayerBuild playerBuildWorker1_a = new PlayerBuild(players[0],players[0].getUsedWorker(),0,1,remoteView);
        controller.build(playerBuildWorker1_a);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,0,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker1_i = new PlayerMove(players[0],1,0,3,remoteView);
        controller.move(playerMoveWorker1_i);

        PlayerBuild playerBuildWorker1_i = new PlayerBuild(players[0],players[0].getUsedWorker(),0,4,remoteView);
        controller.build(playerBuildWorker1_i);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],1,1,2,remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),1,1,remoteView1);
        controller.build(playerBuild2Worker1_a);

        controller.move(playerMoveWorker1_a);
        controller.build(playerBuildWorker1_a);

        PlayerMove playerMove2Worker0_i = new PlayerMove(players[1],0,3,0,remoteView1);
        controller.move(playerMove2Worker0_i);

        PlayerBuild playerBuild2Worker0_i = new PlayerBuild(players[1],players[1].getUsedWorker(),4,0,remoteView1);
        controller.build(playerBuild2Worker0_i);

        controller.move(playerMoveWorker1_i);
        controller.build(playerBuildWorker1_i);

        controller.move(playerMove2Worker0_a);
        controller.build(playerBuild2Worker0_a);

        controller.move(playerMoveWorker1_a);
        PlayerBuild playerBuildWorker1_l = new PlayerBuild(players[0],players[0].getUsedWorker(),1,1,remoteView);
        controller.build(playerBuildWorker1_l);

        controller.move(playerMove2Worker0_i);
        controller.build(playerBuild2Worker0_i);

        assertEquals(2, controller.getModel().getBoard().getCell(0, 1).getLevel().getBlockId());
        assertEquals(2, controller.getModel().getBoard().getCell(1, 0).getLevel().getBlockId());
        assertEquals(2, controller.getModel().getBoard().getCell(1, 1).getLevel().getBlockId());
        assertEquals(controller.getModel().getActualPlayer().getPlayerName(), players[0].getPlayerName());

        PlayerMove playerMove = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMove);
        assertFalse(controller.getModel().getActualPlayer().getWorker(0).getStatus());
    }

    @Test
    public void victoryTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0],players[0].getUsedWorker(),2,0,remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],1,3,1,remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0],0,2,0,remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0],players[0].getUsedWorker(),3,0,remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1],1,4,1,remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.build(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0],0,3,0,remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());
    }

    @Test
    public void buildADomeTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,0,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker1_d = new PlayerMove(players[0],1,1,1,remoteView);
        controller.move(playerMoveWorker1_d);

        PlayerBuild playerBuildWorker1_a = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_a);

        PlayerMove playerMove2Worker1_s = new PlayerMove(players[1],1,2,0,remoteView1);
        controller.move(playerMove2Worker1_s);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.build(playerBuild2Worker1_s);

        assertEquals(4, model.getBoard().getCell(1, 0).getLevel().getBlockId());
    }

    @Test
    public void IllegalBuildException(){
        exception.expect(IllegalArgumentException.class);
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,0,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker1_d = new PlayerMove(players[0],1,1,1,remoteView);
        controller.move(playerMoveWorker1_d);

        PlayerBuild playerBuildWorker1_a = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_a);

        PlayerMove playerMove2Worker1_s = new PlayerMove(players[1],1,2,0,remoteView1);
        controller.move(playerMove2Worker1_s);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.build(playerBuild2Worker1_s);

        controller.move(playerMoveWorker1_s);
        controller.build(playerBuildWorker1_d);
    }

    @Test
    public void newGameTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao",players[0].getPlayerName(),clientConnection,2,true);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(),clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0],players[0].getUsedWorker(),2,0,remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],1,3,1,remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0],0,2,0,remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0],players[0].getUsedWorker(),3,0,remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1],1,4,1,remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.build(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0],0,3,0,remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0],remoteView,ch,clientConnection,lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1],remoteView1,ch,clientConnection2,lobby);
        newGameMessage2.handler(controller);

        assertEquals(model.getPhase(),Phase.SETWORKER1);
    }

    @Test
    public void newGameNewPlayerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao",players[0].getPlayerName(),clientConnection,2,true);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(),clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0],players[0].getUsedWorker(),2,0,remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],1,3,1,remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0],0,2,0,remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0],players[0].getUsedWorker(),3,0,remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1],1,4,1,remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.build(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0],0,3,0,remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0],remoteView,ch,clientConnection,lobby);
        newGameMessage.handler(controller);

        char ch2 = 'n';
        NewGameMessage newGameMessage2 = new NewGameMessage(players[1],remoteView1,ch2,clientConnection2,lobby);
        newGameMessage2.handler(controller);

        Lobby lobby1 = new Lobby("pizza",players[0].getPlayerName(),clientConnection,2,true);

        Player player3 = new Player("Toad");

        Player[] players1 = new Player[2];
        players1[0] = players[0];
        players1[1] = player3;
        Model model1 = new Model(players1,true);
        Controller controller1 = new Controller(model1);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby1.addPlayer(players1[1].getPlayerName(),clientConnection3);
        RemoteView remoteView2 = new RemoteView(players1[1],players1[0].getPlayerName(),clientConnection3,lobby1);

        assertEquals(controller1.getModel().getPhase(),Phase.SETWORKER1);
    }

    @Test
    public void newGame3PlayersTest(){
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao",players[0].getPlayerName(),clientConnection,3,true);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(),clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), clientConnection2, lobby);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[2].getPlayerName(),clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2],players[0].getPlayerName(),players[1].getPlayerName(),clientConnection3,lobby);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2],2,4,remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2],4,0,remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMove = new PlayerMove(players[2],1,4,1,remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2],players[2].getUsedWorker(),3,0,remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMoveWorker0_d);
        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0],players[0].getUsedWorker(),2,0,remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],0,1,1,remoteView1);
        controller.move(playerMove2Worker1_a);
        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,1,remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMove2 = new PlayerMove(players[2],1,4,0,remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[2],players[2].getUsedWorker(),3,0,remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0],0,2,0,remoteView);
        controller.move(playerMoveWorker0_d2);
        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0],players[0].getUsedWorker(),3,0,remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1],0,1,0,remoteView1);
        controller.move(playerMove2Worker1_d);
        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),1,1,remoteView1);
        controller.build(playerBuild2Worker1_s);

        controller.move(playerMove);
        PlayerBuild playerBuild12 = new PlayerBuild(players[2],1,4,0,remoteView2);
        controller.build(playerBuild12);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0],0,3,0,remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getPlayer(0).hasWon());
        assertEquals(model.getActualPlayer(),model.getPlayer(1));

        PlayerMove playerMove2Worker0_win = new PlayerMove(players[1],0,2,0,remoteView1);
        controller.move(playerMove2Worker0_win);
        PlayerBuild playerBuild2Worker1_win = new PlayerBuild(players[1],players[1].getUsedWorker(),2,1,remoteView1);
        controller.build(playerBuild2Worker1_win);

        controller.move(playerMove2);
        PlayerBuild playerBuild1 = new PlayerBuild(players[2],1,4,1,remoteView2);
        controller.build(playerBuild1);

        PlayerMove playerMove2Worker0_winNow = new PlayerMove(players[1],0,3,0,remoteView1);
        controller.move(playerMove2Worker0_winNow);
        assertTrue(model.getPlayer(1).hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0],remoteView,ch,clientConnection,lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1],remoteView1,ch,clientConnection2,lobby);
        newGameMessage2.handler(controller);

        NewGameMessage newGameMessage3 = new NewGameMessage(players[2],remoteView2,ch,clientConnection3,lobby);
        newGameMessage3.handler(controller);

        assertEquals(model.getPhase(),Phase.SETWORKER1);
    }

    @Test
    public void newGame3PlayersNewPlayerTest(){
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao",players[0].getPlayerName(),clientConnection,3,true);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(),clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), clientConnection2, lobby);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[2].getPlayerName(),clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2],players[0].getPlayerName(),players[1].getPlayerName(),clientConnection3,lobby);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2],2,4,remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2],4,0,remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMove = new PlayerMove(players[2],1,4,1,remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2],players[2].getUsedWorker(),3,0,remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMoveWorker0_d);
        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0],players[0].getUsedWorker(),2,0,remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],0,1,1,remoteView1);
        controller.move(playerMove2Worker1_a);
        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,1,remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMove2 = new PlayerMove(players[2],1,4,0,remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[2],players[2].getUsedWorker(),3,0,remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0],0,2,0,remoteView);
        controller.move(playerMoveWorker0_d2);
        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0],players[0].getUsedWorker(),3,0,remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1],0,1,0,remoteView1);
        controller.move(playerMove2Worker1_d);
        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),1,1,remoteView1);
        controller.build(playerBuild2Worker1_s);

        controller.move(playerMove);
        PlayerBuild playerBuild12 = new PlayerBuild(players[2],1,4,0,remoteView2);
        controller.build(playerBuild12);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0],0,3,0,remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getPlayer(0).hasWon());
        assertEquals(model.getActualPlayer(),model.getPlayer(1));

        PlayerMove playerMove2Worker0_win = new PlayerMove(players[1],0,2,0,remoteView1);
        controller.move(playerMove2Worker0_win);
        PlayerBuild playerBuild2Worker1_win = new PlayerBuild(players[1],players[1].getUsedWorker(),2,1,remoteView1);
        controller.build(playerBuild2Worker1_win);

        controller.move(playerMove2);
        PlayerBuild playerBuild1 = new PlayerBuild(players[2],1,4,1,remoteView2);
        controller.build(playerBuild1);

        PlayerMove playerMove2Worker0_winNow = new PlayerMove(players[1],0,3,0,remoteView1);
        controller.move(playerMove2Worker0_winNow);
        assertTrue(model.getPlayer(1).hasWon());

        char ch = 'y';
        char ch2 = 'n';
        NewGameMessage newGameMessage = new NewGameMessage(players[0],remoteView,ch,clientConnection,lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1],remoteView1,ch2,clientConnection2,lobby);
        newGameMessage2.handler(controller);

        NewGameMessage newGameMessage3 = new NewGameMessage(players[2],remoteView2,ch,clientConnection3,lobby);
        newGameMessage3.handler(controller);

        Lobby lobby1 = new Lobby("pizza",players[0].getPlayerName(),clientConnection,3,true);
        lobby1.addPlayer(players[2].getPlayerName(),clientConnection3);
        Player player3 = new Player("Peach");

        Player[] players1 = new Player[3];
        players1[0] = players[0];
        players1[1] = players[2];
        players1[2] = player3;
        Model model1 = new Model(players1,true);
        Controller controller1 = new Controller(model1);

        ClientConnection clientConnection4 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby1.addPlayer(players1[2].getPlayerName(),clientConnection4);
        RemoteView remoteView4 = new RemoteView(players1[2],players1[0].getPlayerName(),players[1].getPlayerName(),clientConnection4,lobby1);

        assertEquals(model1.getPhase(),Phase.SETWORKER1);
    }
}
