package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.gods.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.*;
public class GodCardControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void drawedCards2PlayersTest() {
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
        Phase phase = Phase.PICK_CARD;
        assertEquals(controller.getModel().getPhase(), phase);
        GodCard godCard = new Apollo();
        assertEquals(controller.getModel().getGods().get(0), godCard);
        GodCard godCard2 = new Artemis();
        assertEquals(controller.getModel().getGods().get(1), godCard2);
    }

    @Test
    public void drawedCards3PlayersTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), new ClientConnection() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, 2, remoteView);
        controller.drawedCards(drawedCards);
        Phase phase = Phase.PICK_CARD;
        assertEquals(controller.getModel().getPhase(), phase);
        GodCard godCard = new Apollo();
        assertEquals(controller.getModel().getGods().get(0), godCard);
        GodCard godCard2 = new Artemis();
        assertEquals(controller.getModel().getGods().get(1), godCard2);
        GodCard godCard3 = new Athena();
        assertEquals(controller.getModel().getGods().get(2), godCard3);
    }

    @Test
    public void pickACard2PlayersTest() {
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
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Artemis();
        assertEquals(model.getPlayer(1).getGodCard(), godCard);

        PickedCard pickedCard1 = new PickedCard(players[0], remoteView, 1);
        controller.pickACard(pickedCard1);
        assertEquals(model.getPlayer(0).getGodCard(), godCard1);
    }

    @Test
    public void pickACard3PlayersTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), new ClientConnection() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, 2, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), new ClientConnection() {
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
        RemoteView remoteView3 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 2);
        PickedCard pickedCard1 = new PickedCard(players[2], remoteView3, 1);
        controller.pickACard(pickedCard);
        controller.pickACard(pickedCard1);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Artemis();
        GodCard godCard2 = new Athena();
        assertEquals(model.getPlayer(1).getGodCard(), godCard2);
        assertEquals(model.getPlayer(2).getGodCard(), godCard1);
        PickedCard pickedCard2 = new PickedCard(players[0], remoteView, 0);
        controller.pickACard(pickedCard2);
        assertEquals(model.getPlayer(0).getGodCard(), godCard);
    }

    @Test
    public void buildADomeTest() {
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

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 0, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker1_d = new PlayerMove(players[0], 1, 1, 1, remoteView);
        controller.move(playerMoveWorker1_d);

        PlayerBuild playerBuildWorker1_a = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_a);

        PlayerMove playerMove2Worker1_s = new PlayerMove(players[1], 1, 2, 0, remoteView1);
        controller.move(playerMove2Worker1_s);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView1);
        controller.build(playerBuild2Worker1_s);

        assertEquals(4, model.getBoard().getCell(1, 0).getLevel().getBlockId());
    }

    @Test
    public void newGameTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 2, false);
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerMove playerMoveError = new PlayerMove(players[0], 0, -1, -1, remoteView);
        controller.move(playerMoveError);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        controller.move(playerMove2Worker0_a);

        controller.checkCantBuild(playerBuild2Worker0_a);

        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 1, 3, 1, remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 1, 4, 1, remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        assertEquals(model.getPhase(), Phase.DRAWCARD);
    }

    @Test
    public void newGameNewPlayerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 2, false);
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        DrawedCards drawedCardsError = new DrawedCards(players[0], 0, 0, remoteView);
        controller.drawedCards(drawedCardsError);

        DrawedCards drawedCardsError2 = new DrawedCards(players[0], 0, 6,8, remoteView);
        controller.drawedCards(drawedCardsError2);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCardError = new PickedCard(players[1], remoteView1, 100);
        controller.pickACard(pickedCardError);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker1 = new PlayerWorker(players[1], 0, 0, remoteView1);
        controller.setPlayerWorker(playerWorker1);
        PlayerWorker playerWorker5 = new PlayerWorker(players[1], 6, 6, remoteView1);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 1, 3, 1, remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 1, 4, 1, remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        char ch2 = 'n';
        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch2, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        Lobby lobby1 = new Lobby("pizza", players[0].getPlayerName(), clientConnection, 2, false);

        Player player3 = new Player("Toad");

        Player[] players1 = new Player[2];
        players1[0] = players[0];
        players1[1] = player3;
        Model model1 = new Model(players1, false);
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
        lobby1.addPlayer(players1[1].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players1[1], players1[0].getPlayerName(), clientConnection3, lobby1);

        assertEquals(controller1.getModel().getPhase(), Phase.DRAWCARD);
    }

    @Test
    public void newGame3PlayersTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
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
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCardsError = new DrawedCards(players[0], 0, 0, 0, remoteView);
        controller.drawedCards(drawedCardsError);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, 7, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        controller.pickACard(pickedCard2);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 2);
        controller.pickACard(pickedCard);

        controller.pickACard(pickedCard2);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);
        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 0, 1, 1, remoteView1);
        controller.move(playerMove2Worker1_a);
        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMove2 = new PlayerMove(players[2], 1, 4, 0, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);
        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 0, 1, 0, remoteView1);
        controller.move(playerMove2Worker1_d);
        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 1, remoteView1);
        controller.build(playerBuild2Worker1_s);

        controller.move(playerMove);
        PlayerBuild playerBuild12 = new PlayerBuild(players[2], 1, 4, 0, remoteView2);
        controller.build(playerBuild12);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getPlayer(0).hasWon());
        assertEquals(model.getActualPlayer(), model.getPlayer(1));

        PlayerMove playerMove2Worker0_win = new PlayerMove(players[1], 0, 2, 0, remoteView1);
        controller.move(playerMove2Worker0_win);
        PlayerBuild playerBuild2Worker1_win = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_win);

        controller.move(playerMove2);
        PlayerBuild playerBuild1 = new PlayerBuild(players[2], 1, 4, 1, remoteView2);
        controller.build(playerBuild1);

        PlayerMove playerMove2Worker0_winNow = new PlayerMove(players[1], 0, 3, 0, remoteView1);
        controller.move(playerMove2Worker0_winNow);
        assertTrue(model.getPlayer(1).hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        NewGameMessage newGameMessage3 = new NewGameMessage(players[2], remoteView2, ch, clientConnection3, lobby);
        newGameMessage3.handler(controller);
        lobby.setEndGame();

        assertEquals(model.getPhase(), Phase.DRAWCARD);
    }

    @Test
    public void newGame3PlayersNewPlayerTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
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
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);

        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, 7, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 2);
        controller.pickACard(pickedCard);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        controller.pickACard(pickedCard2);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);
        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 0, 1, 1, remoteView1);
        controller.move(playerMove2Worker1_a);
        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMove2 = new PlayerMove(players[2], 1, 4, 0, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);
        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 0, 1, 0, remoteView1);
        controller.move(playerMove2Worker1_d);
        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 1, remoteView1);
        controller.build(playerBuild2Worker1_s);

        controller.move(playerMove);
        PlayerBuild playerBuild12 = new PlayerBuild(players[2], 1, 4, 0, remoteView2);
        controller.build(playerBuild12);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getPlayer(0).hasWon());
        assertEquals(model.getActualPlayer(), model.getPlayer(1));

        PlayerMove playerMove2Worker0_win = new PlayerMove(players[1], 0, 2, 0, remoteView1);
        controller.move(playerMove2Worker0_win);
        PlayerBuild playerBuild2Worker1_win = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_win);

        controller.move(playerMove2);
        PlayerBuild playerBuild1 = new PlayerBuild(players[2], 1, 4, 1, remoteView2);
        controller.build(playerBuild1);

        PlayerMove playerMove2Worker0_winNow = new PlayerMove(players[1], 0, 3, 0, remoteView1);
        controller.move(playerMove2Worker0_winNow);
        assertTrue(model.getPlayer(1).hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        char ch2 = 'n';
        NewGameMessage newGameMessage3 = new NewGameMessage(players[2], remoteView2, ch2, clientConnection3, lobby);
        newGameMessage3.handler(controller);

        Lobby lobby1 = new Lobby("pizza", players[0].getPlayerName(), clientConnection, 3, false);
        lobby1.addPlayer(players[1].getPlayerName(), clientConnection2);
        Player player3 = new Player("Peach");

        Player[] players1 = new Player[3];
        players1[0] = players[0];
        players1[1] = players[1];
        players1[2] = player3;
        Model model1 = new Model(players1, false);
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
        lobby1.addPlayer(players1[2].getPlayerName(), clientConnection4);
        RemoteView remoteView4 = new RemoteView(players1[2], players1[0].getPlayerName(), players[1].getPlayerName(), clientConnection4, lobby1);

        assertEquals(model1.getPhase(), Phase.DRAWCARD);
        lobby.closeLobby();
    }

    @Test
    public void checkLooseCantBuildTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 2, false);
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 11, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 4, 4, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 0, 1, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 2, 2, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 1, remoteView);
        controller.build(playerBuildWorker1_d);

        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        PlayerBuild playerBuild = new PlayerBuild(players[0], 0, 1, 0, remoteView);
        controller.build(playerBuild);

        useGodPower.handler(controller);
        controller.build(playerBuild);
        useGodPower.handler(controller);
        controller.build(playerBuild);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 3, 3, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 4, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 1, 1, 1, remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 2, remoteView);
        controller.build(playerBuildWorker0_d);

        useGodPower.handler(controller);
        controller.build(playerBuild);

        assertEquals(model.getPhase(), Phase.MOVE);
    }

    @Test
    public void notUsePowerTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
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
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 1, 4, 8, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        controller.pickACard(pickedCard2);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);


        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);
        UseGodPower useGodPower2 = new UseGodPower(players[1], remoteView1, ch);
        useGodPower2.handler(controller);

        UseGodPower useGodPower3 = new UseGodPower(players[2], remoteView2, ch);
        useGodPower3.handler(controller);
        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);
    }

    @Test
    public void notUsePowerPart2Test() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
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
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
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
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
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
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 1, 4, 0, remoteView);
        drawedCards.handler(controller);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        pickedCard2.handler(controller);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        playerWorker.handler(controller);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        playerMoveWorker1_s.handler(controller);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        playerBuildWorker1_d.handler(controller);


        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);
        UseGodPower useGodPower2 = new UseGodPower(players[1], remoteView1, ch);
        useGodPower2.handler(controller);


        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);
    }

    @Test
    public void dontUsePowersTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 4, 3, remoteView);
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
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 0, 2, 3, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild2);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch2 = 'y';
        UseGodPower useGodPower2 = new UseGodPower(players[1], remoteView, ch2);
        useGodPower2.handler(controller);
        PlayerBuild playerBuild3 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild3);
    }

    @Test
    public void differentGodPowersTest() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 6, 10, remoteView);
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

        PlayerWorker playerWorker = new PlayerWorker(players[0], 2, 2, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 2, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 3, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 1, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 2, 1, remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 2, remoteView);
        controller.build(playerBuild);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 1, 1, 2, remoteView2);
        controller.move(playerMove2);
    }
}
