package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.view.RemoteView;

/**
 * This class parses the command string received from the client.
 * The main information the parser receives is the game's phase, which is used to decide which type of string is expected
 */
public class CommandParser {
    private final Phase phase;
    private String string;
    private final Player player;
    private final RemoteView view;

    /**
     * Class' constructor
     * @param phase is the actual phase
     * @param string is the client's sent string
     * @param player is the instance of player that is sending the command
     * @param view is the instance of the player view that is asking to parse the command
     */
    public CommandParser(Phase phase, String string, Player player, RemoteView view){
        this.phase = phase;
        this.string = string;
        this.player = player;
        this.view = view;
    }

    /**
     *  The input string is parsed based on each game's phase: if the string's format doesn't pass a few tests (based on the phase) an IllegalArgumentException is thrown.
     *  DRAWCARD: expect x,y or x,y,z format.
     *  PICK_CARD: expect a single integer value.
     *  SETWORKER1 and SETWORKER2: expect x,y format representing the cell coordinates.
     *  MOVE: expect w,x,y format where w is the worker id and x,y the cel coordinates.
     *  BUILT: expect x,y format as cell coordinates.
     *  WAIT_GOD_ANSWER: expect a single char for the answer y or n.
     *  PROMETHEUS_WORKER: expect a single integer representing the selected worker id.
     *  END_GAME: expect a single char representing the player answer y or n.
     * @return the specific Message subclass to notify the controller.
     * @throws IllegalArgumentException if the input string does not pass the game phase's tests.
     * @throws IndexOutOfBoundsException reported at the function that called the parser the errors sent by the used class.
     */
    public Message parse() throws IllegalArgumentException, IndexOutOfBoundsException{
        String[] s;
        switch(phase){
            case DRAWCARD:
                string = string.replaceAll(" ", "");
                s = string.split(",");//x,y,z
                if(s.length == 2){
                    if(Integer.parseInt(s[0]) >= 0 && Integer.parseInt(s[1]) >= 0 && Integer.parseInt(s[0]) <= 13 && Integer.parseInt(s[1]) <= 13){
                        return new DrawedCards(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
                    }

                }
                else if(s.length == 3){
                    if(Integer.parseInt(s[0]) >= 0 && Integer.parseInt(s[1]) >= 0 && Integer.parseInt(s[0]) <= 13 && Integer.parseInt(s[1]) <= 13 && Integer.parseInt(s[2]) >= 0 && Integer.parseInt(s[2]) <= 13) {
                        return new DrawedCards(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), view);
                    }
                }
                throw new IllegalArgumentException("Wrong input");
            case PICK_CARD:
                int pickCard = Integer.parseInt(String.valueOf(string.charAt(0)));
                if(pickCard <= 2 && pickCard >= 0){
                    return new PickedCard(this.player, this.view, pickCard);
                }
                throw new IllegalArgumentException("Wrong index input");

            case SETWORKER1: case SETWORKER2:
                string = string.replaceAll(" ", "");
                s = string.split(",");//x,y
                if(s.length > 2){
                    throw new IllegalArgumentException("Wrong input, please insert x,y");
                }
                return new PlayerWorker(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
            case MOVE:
                string = string.replaceAll(" ", "");
                s = string.split(",");//workerID, x, y
                if(s.length>3){
                    throw new IllegalArgumentException("Wrong input, please insert w,x,y");
                }
                return new PlayerMove(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), view);
            case BUILD:
                string = string.replaceAll(" ", "");
                s = string.split(",");//x,y
                if(s.length>2){
                    throw new IllegalArgumentException("Wrong input, please insert x,y");
                }
                return new PlayerBuild(player, player.getUsedWorker() , Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
            case WAIT_GOD_ANSWER:
                char waitGodAnswer = string.toLowerCase().charAt(0);
                if(waitGodAnswer != 'y' && waitGodAnswer != 'n'){
                    throw new IllegalArgumentException("Please insert only y or n");
                }
                return new UseGodPower(player, view, waitGodAnswer);
            case PROMETHEUS_WORKER:
                int prometheusWorker = Integer.parseInt(string);
                if(prometheusWorker >= 2 || prometheusWorker < 0){
                    throw new IllegalArgumentException("Please insert only 0 or 1");
                }
                return new SetPrometheus(player, view, prometheusWorker);
            case END_GAME:
                char endGame = string.toLowerCase().charAt(0);
                if(endGame != 'y' && endGame != 'n'){
                    throw new IllegalArgumentException("Please insert only y or n");
                }
                return new NewGameMessage(player, view, endGame, view.getConnection(), view.getLobby());

            default: throw new IllegalArgumentException("Can't do it now");
        }
    }
}
