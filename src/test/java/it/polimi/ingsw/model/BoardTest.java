package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BoardTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getCell() {
        Player[] players=new Player[2];
        players[0]=new Player("pioppo");
        players[1]=new Player("Amedeus");
        Board board=new Board(players);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                assertEquals(i, board.getCell(i, j).getX());
                assertEquals(j, board.getCell(i, j).getY());
            }
        }
    }

    @Test
    public void getCellException(){
        exception.expect(IllegalArgumentException.class);
        Player[] players=new Player[2];
        players[0]=new Player("pioppo");
        players[1]=new Player("Amedeus");
        Board board=new Board(players);
        int i = -1,j = -1;
        board.getCell(i,j).getX();
        board.getCell(i,j).getY();
    }

    @Test
    public void printTest(){
        Player[] players = new Player[2];
        players[0] = new Player("pioppo");
        players[1] = new Player("Amedeus");
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

        String string = "         0         1         2         3         4\n" +
                "    ---------------------------------------------------\n" +
                "0   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "1   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "2   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "3   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "4   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n";
        assertEquals(string,model.getBoard().print());

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,3,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        String string2 = "         0         1         2         3         4\n" +
                "    ---------------------------------------------------\n" +
                "0   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:0 W:0 | P:n W:n | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "1   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:0 W:1 | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "2   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:1 W:0 | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "3   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:n W:n | P:1 W:1 | P:n W:n |\n" +
                "    ---------------------------------------------------\n" +
                "4   | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | L:0 F:0 | \n" +
                "    | P:n W:n | P:n W:n | P:n W:n | P:n W:n | P:n W:n |\n" +
                "    ---------------------------------------------------\n";

        assertEquals(string2,model.getBoard().print());
    }
}