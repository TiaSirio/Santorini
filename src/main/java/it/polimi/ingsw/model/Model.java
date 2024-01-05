package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.gods.Prometheus;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This class merges all the game's data
 */

public class Model extends Observable<ViewMessage> {
    private Board board;
    private final Player[] turn;
    private int id = 0;
    private int leftPlayers, playersWhoWon = 0, playersWhoLost = 0;
    private final boolean simplePlay;
    private Phase phase = Phase.DRAWCARD;
    private final Map<Gods, Player> playerCards = new EnumMap<>(Gods.class);
    private final ArrayList<GodCard> gods = new ArrayList<>();
    private final ArrayList<GodCard> godsInPlay = new ArrayList<>();
    private boolean movedUp = false;
    private MessageType messageType = MessageType.DRAW_CARD;
    private String playerMessage = PlayerMessage.DRAW_CARD;
    private final HashMap<Player, Integer> podium = new HashMap<>();

    /**
     *Class' constructor
     * @param players represents the actual game's players.
     * @param simplePlay is a boolean: if it's set to true it means that the game is played without godCard (SimpleMode mode).
     */
    public Model(Player[] players, boolean simplePlay){
        this.turn = players;
        this.simplePlay = simplePlay;
        this.board= new Board(this.turn);
        leftPlayers = players.length;
        if(simplePlay){
            this.phase = Phase.SETWORKER1;
            this.messageType = MessageType.SET_WORKER_1;
            this.playerMessage = PlayerMessage.PLACE_FIRST_WORKER;
        }
    }

    /**
     * This method configures the model and the phases of the actual game. This method also notifies every players that the game ios starting.
     */
    public void initialize(){
        notifyObservers(new GameMessage(turn[id], PlayerMessage.YOUR_TURN, MessageType.BEGINNING, Phase.BEGINNING));
        notifyObservers(new GameBoardMessage(getBoardClone(), turn[id], this.playerMessage, this.messageType, this.phase));

    }

    /**
     * @return true if Athena's player has moved up during his/her turn.
     */
    public boolean isMovedUp() {
        return movedUp;
    }

    /**
     * @param movedUp is automatically set to true by Athena when she uses her power (she goes up one level). This value remains true until the beginning of Athena's turn.
     */
    public void setMovedUp(boolean movedUp) {
        this.movedUp = movedUp;
    }

    /**
     * This function resets the board, creating a new different Board. It's used when we want to start another game.
     */
    public void resetBoard(){
        this.board = new Board(turn);
    }

    /**
     * @return true if the mode of the actual game is set to SimpleMode.
     */
    public boolean isSimplePlay() {
        return simplePlay;
    }

    /**
     * @param p is a player of the game.
     * @return true if this turn is the turn of the player p.
     */
    public boolean isPlayerTurn(Player p){
        return this.turn[id] == p;
    }

    /**
     * This method sets the game's next phase.
     * @param phase represent a phase of the game.
     */
    public void setNextPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * @return the turn player's id (the id is used in the array turn[]).
     */
    public int getActualPlayerId() {
        return id;
    }

    /**
     * @return an array of players containing every actual game's players.
     */
    public Player[] getPlayers(){
        return turn;
    }

    /**
     *
     * @return the active gods card in the game.
     */
    public ArrayList<GodCard> getGodsInPlay() {
        return godsInPlay;
    }

    /**
     *
     * @return the number of players who didn't win/loose yet.
     */
    public int getLeftPlayers(){
        return leftPlayers;
    }

    /**
     * @return the number of players in the actual game.
     */
    public int getNumOfPlayers(){
        return this.turn.length;
    }

    /**
     * This function sets the next phase message type that needs to be notified to the clients
     * @param messageType is the MessageType enum instance to set
     */
    public void setNextMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     *
     * @param playerMessage is a string representing the next message that needs to be notified to the clients
     */
    public void setNextPlayerMessage(String playerMessage) {
        this.playerMessage = playerMessage;
    }

    /**
     * @return the actual game's board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method returns a clone of the board.
     * @return a clone of the board.
     */
    public Board getBoardClone() {
        try{
            return board.clone();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This function is used to update the turn with the order in which the players join the game.
     * If a player has won or lost he is skipped
     */
    public synchronized void updateTurn(){
        id = (id + 1) % (turn.length);

        if(turn[id].hasWon() && turn.length == 3){
            updateTurn();
        }
        if(turn[id].getHasLost() && turn.length == 3){
            updateTurn();
        }
        try{
            if(!turn[id].getWorker(0).getStatus() && !turn[id].getWorker(1).getStatus()){
                loose(turn[id]);
            }
        }catch(IndexOutOfBoundsException e){
            //e.printStackTrace();
        }

        notifyObservers(new GameMessage(turn[id], PlayerMessage.YOUR_TURN, MessageType.BEGINNING, Phase.BEGINNING));
    }

    /**
     *
     * @return the actual phase of the game.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * This method updates the phase of the actual game.
     * @see Phase {@link Phase} In the phase is contained the next() method.
     */
    public void updatePhase(){
        phase = Phase.next(phase);
    }

    /**
     * This method is used to add a new worker for a player.
     * It gets the cell inside the board with the coordinates given to it,
     * it then sets the cell as used.
     * I adds a new worker for the player who ask for it (contained in the message) and
     * at the end it notifies the clients about the changes
     * @param playerWorker is the message received from the remote view
     */
    public void setPlayerWorker (PlayerWorker playerWorker){
        Cell chosenCell = this.getBoard().getCell(playerWorker.getX(), playerWorker.getY());
        chosenCell.useCell();
        playerWorker.getPlayer().setWorkers(new Worker(chosenCell));
        notifyChanges();
    }

    /**
     * @return the turn player's instance.
     */
    public Player getActualPlayer() {
        return turn[id];
    }

    /**
     * This method adds a chosen god card to the game Array
     * This method also adds the godCard to the godsInPlay array
     * @param godCard is the chosen god card instance
     */
    public void addGod(GodCard godCard){
        gods.add(godCard);
        godsInPlay.add(godCard);
    }

    /**
     *
     * @return game's gods as an array
     */
    public ArrayList<GodCard> getGods() {
        return gods;
    }

    /**
     *
     * @param i is an int representing the value of a player in turn[].
     * @return the corresponding player instance in turn[i].
     */
    public Player getPlayer(int i){
        return turn[i];
    }

    /**
     *
     * @return the number of god cards left to choose.
     */
    public int getLeftCards(){
        return gods.size();
    }

    /**
     *
     * @param card an integer representing the card's index in the array
     * @return true if the selected index is available in the array
     */
    public boolean isGodAvailable(int card){
        return gods.size() >= card + 1;
    }

    /**
     * This method returns the Player owning a specific GodCard (gods). This method returns null if there are no player associated to that specific card.
     * @param gods is the GodCard involved.
     * @return the player associated.
     */
    public Player getGCPlayer(Gods gods){
        return playerCards.get(gods);
    }

    /**
     * This method is used to get the next turn player's GodCard
     * @return the GodCard instance of the next turn player
     */
    public GodCard getNextPlayerGC(){
        int next=(id+1)%(turn.length);
        if(turn[next].hasWon() || turn[next].getHasLost()){
            next=(next+1)%(turn.length);
        }
        return turn[next].getGodCard();
    }

    /**
     * This method is used to assign a GodCard to a player
     * @param player is the player who picked the card within the drawed ones
     * @param card is the drawed card's array index chosen by the player
     * @return the GodCard's instance of the chosen card
     */
    public GodCard assignCard(Player player, int card){
        GodCard godCard = gods.get(card);
        gods.remove(card);
        gods.trimToSize();
        player.setGodCard(godCard);
        playerCards.put(godCard.getCardGod(), player);
        return godCard;
    }


    /**
     * This method is called by the controller to update data for a move event
     * @param move is the message received from the controller (which received it from the client)
     * @throws ArrayIndexOutOfBoundsException when you try to move outside of the board
     */
    public void move(PlayerMove move) throws ArrayIndexOutOfBoundsException {
        Worker movingWorker = move.getPlayer().getWorker(move.getWorkerId());
        movingWorker.getCell().freeCell();
        movingWorker.setCell(this.getBoard().getCell(move.getRow(), move.getColumn()));
        this.getBoard().getCell(move.getRow(), move.getColumn()).useCell();
        getActualPlayer().setUsedWorker(move.getWorkerId());
        //notifyChanges();
    }

    /**
     * This function is called by the controller to update data for a build event
     * @see Cell
     * @param cell is the cell chosen for the built
     * @param level is the level that needs to be assigned to the cell
     */
    public void increaseLevel(Cell cell, Blocks level) {//build
        cell.setLevel(level);
        notifyChanges();
    }

    /**
     * This method adds a player who won to the podium,
     * then, if only one player is left, it calls the loose for the player who lost.
     * If more than one player is left, remove the player workers from the board and set the player victory flag to true
     * At the end, ut updates the model phases and messages and notifies the changes to the clients
     * @param player the player who won
     */
    public void victory(Player player) {
        player.setVictory(true);
        playersWhoWon++;
        ViewMessage win = new GameMessage(turn[id], "Player: " + player.getPlayerName() + " has won!!!!", MessageType.VICTORY, this.phase);
        notifyObservers(win);
        podium.put(player, playersWhoWon);
        if(!isSimplePlay()){
            if(player.getGodCard().getCardGod()== Gods.ATHENA){
                setMovedUp(false);
            }
        }
        if(leftPlayers == 2){
            for (Player value : turn) {
                if (!value.getHasLost() && !value.hasWon() && value != player) {
                    loose(value);
                    return;
                }
            }
            endGame();
        }
        else{
            leftPlayers--;
            player.remove();

                if(turn[id]==player){
                    updateTurn();
                    if(getActualPlayer()==getGCPlayer(Gods.PROMETHEUS) && !((Prometheus)getGCPlayer(Gods.PROMETHEUS).getGodCard()).hasBuilt()){
                        setNextPhase(Phase.WAIT_GOD_ANSWER);
                        setNextPlayerMessage(PlayerMessage.USE_POWER);
                        setNextMessageType(MessageType.USE_POWER);
                    }
                    else{
                        setNextPhase(Phase.MOVE);
                        setNextPlayerMessage(PlayerMessage.MOVE);
                        setNextMessageType(MessageType.MOVE);
                    }
                }

            notifyChanges();
        }

    }

    /**
     * This method adds a player who lost to the podium at the last position,
     * then, if only one player is left, it calls victory for that player.
     * If more than one player is left, it removes the player's workers from the board and sets the player lost flag to true
     * At the end it updates the model's phases and messages and notifies the changes to the clients
     * @param player the player who lost
     */
    public void loose(Player player){
        player.setHasLost(true);
        if(!isSimplePlay()){
            if(player.getGodCard().getCardGod()== Gods.ATHENA){
                setMovedUp(false);
            }
        }
        ViewMessage loose = new GameMessage(turn[id], "Player: " + player.getPlayerName() + " has lost. Retry, you'll have more luck", MessageType.LOSE, this.phase);
        notifyObservers(loose);
        podium.put(player, getNumOfPlayers()-playersWhoLost);
        playersWhoLost++;
        if(leftPlayers == 3){
            leftPlayers--;
            player.remove();

                if(turn[id]==player){
                    updateTurn();
                    if(getActualPlayer()==getGCPlayer(Gods.PROMETHEUS) && !((Prometheus)getGCPlayer(Gods.PROMETHEUS).getGodCard()).hasBuilt()){
                        setNextPhase(Phase.WAIT_GOD_ANSWER);
                        setNextPlayerMessage(PlayerMessage.USE_POWER);
                        setNextMessageType(MessageType.USE_POWER);
                    }
                    else{
                        setNextPhase(Phase.MOVE);
                        setNextPlayerMessage(PlayerMessage.MOVE);
                        setNextMessageType(MessageType.MOVE);
                    }
                }
            notifyChanges();
        }
        else{
            for (Player value : turn) {
                if (!value.hasWon() && !value.getHasLost() && value != player) {
                    victory(value);
                    return;
                }
            }
            endGame();
        }

    }

    /**
     * This function sets the game's phase to END_GAME
     * This function is called when only one player is left
     * It notifies the clients that the game is ended
     */
    public void endGame(){
        phase = Phase.END_GAME;
        ViewMessage end = new EndGameMessage(turn[id], "The game has ended.\nDo you want to play again?(y/n)\n", MessageType.END_GAME, this.phase,podium);
        notifyObservers(end);
    }

    /**
     * This method resets every model's parameters to start a new game and calls the model's "initialize" function
     * startOver is called only if every player has chosen to play again
     *
     */
    public void startOver(){
        resetBoard();
        podium.clear();
        leftPlayers = turn.length;
        this.movedUp=false;
        if(simplePlay){
            this.phase = Phase.SETWORKER1;
            this.messageType = MessageType.SET_WORKER_1;
            this.playerMessage = PlayerMessage.PLACE_FIRST_WORKER;
        }
        else{
            playerCards.clear();
            godsInPlay.clear();
            gods.clear();
            this.phase = Phase.DRAWCARD;
            this.messageType = MessageType.DRAW_CARD;
            this.playerMessage = PlayerMessage.DRAW_CARD;
        }
        for (Player player : turn) {
            player.reset();
        }
        this.playersWhoWon=0;
        this.playersWhoLost=0;
        this.id = 0;
        initialize();
    }


    /**
     * This function notifies every client a new state of the board, phase, turn and message
     */
    public void notifyChanges(){
        notifyObservers(new GameBoardMessage(getBoardClone(), turn[id], this.playerMessage, this.messageType, this.phase));
    }

    /**
     * Similar to notifyChanges, this method is used to notify clients that there is a new phase on the game
     * without changes to the board.
     * @param message is string representing the message that needs to be notified to clients
     */
    public void notifyMessage(String message){
        notifyObservers(new GameMessage(turn[id],message, MessageType.VOID_MESSAGE, this.phase));
    }
}