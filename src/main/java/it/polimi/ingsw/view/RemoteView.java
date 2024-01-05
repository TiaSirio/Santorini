package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.ClientConfigurator;
import it.polimi.ingsw.model.messageModel.EndGameMessage;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.utils.CommandParser;

import java.util.HashMap;

/**
 * View implementation server side
 */
public class RemoteView extends View {
    private final ClientConnection clientConnection;
    private Phase phase = Phase.SETWORKER1;

    /**
     *
     * @return the lobby connected to this view
     */
    public Lobby getLobby() {
        return lobby;
    }

    private final Lobby lobby;

    /**
     * This class recive messages from the socket
     */
    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String msg) {//questa riceve dal client
            //arg è la stringa ricevuta dall'input del client
            //inserire quindi qui le chiamate ai metodi di view per fare le mosse

            CommandParser commandParser = new CommandParser(phase, msg, getPlayer(), RemoteView.this);
            try{
                doAction(commandParser.parse());
            } catch(NumberFormatException e){
                reportError("Please insert numbers only");
            } catch(IndexOutOfBoundsException | IllegalArgumentException e){
                reportError(e.getMessage());
            }

        }
    }

    /**
     * Contructor for two players play
     * @param player The player owner of the view
     * @param opponent a string containing the opponent's name
     * @param c the connection of the player
     * @param lobby the lobby linked to this view
     */
    public RemoteView(Player player, String opponent, ClientConnection c, Lobby lobby) {
        super(player);
        this.clientConnection = c;
        MessageReceiver messageReceiver = new MessageReceiver();
        c.addObserver(messageReceiver);
        c.removeExcept(messageReceiver);
        c.asyncSend("Your opponent is: " + opponent);
        this.lobby = lobby;
        HashMap<String,String> opponents = new HashMap<>();
        opponents.put(opponent,"red");
        clientConnection.asyncSend(new ClientConfigurator(2, opponents, player));
    }

    /**
     * Contructor for two players play
     * @param player The player owner of the view
     * @param opponent1 a string containing the first opponent's name
     * @param opponent2 a stirng containing the second opponent's name
     * @param c the connection of the player
     * @param lobby the lobby linked to this view
     */
    public RemoteView(Player player, String opponent1, String opponent2, ClientConnection c, Lobby lobby) {
        super(player);
        this.clientConnection = c;
        MessageReceiver messageReceiver = new MessageReceiver();
        c.addObserver(messageReceiver);
        c.removeExcept(messageReceiver);
        c.asyncSend("Your opponents are: " + opponent1 + " and " + opponent2);
        this.lobby = lobby;
        HashMap<String,String> opponents = new HashMap<>();
        opponents.put(opponent1,"red");
        opponents.put(opponent2,"green");
        clientConnection.asyncSend(new ClientConfigurator(3, opponents, player));
    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.send(message);
    }

    @Override
    public void update(ViewMessage arg) {//questa riceve dal model
        if(arg instanceof EndGameMessage){
            this.lobby.setEndGame();
        }
        phase = arg.getPhase();
        showMessage(arg);

    }


    /**
     *
     * @return the player's connection
     */
    public ClientConnection getConnection(){
        return this.clientConnection;
    }

}
