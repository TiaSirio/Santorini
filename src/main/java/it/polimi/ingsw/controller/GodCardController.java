package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class integrates additional controls so that the players can play in the "hard" mode
 */
public class GodCardController extends Controller {

    /**
     * {@inheritDoc}
     */
    public GodCardController(Model model) {
        super(model);
    }

    /**
     * It overrides the Controller's setPlayerWorker function, if Prometheus' god card is used in the game, this function modifies the game flow, instead of starting in the moving phase it makes the game start asking
     * the Prometheus' owner to use the power, only if the starting player has that card.
     * At first it checks if the player who has sent the message is the actual turn's player
     *       If he is, the worker is placed on the board using the model's setWorker function
     *         Then:
     *            - If the phase is SETWORKER1 then it changes the model phase to SETWORKER2 but it doesn't change the turn
     *            - If the phase is SETWORKER2 we can have 2 different alternatives:
     *                           - If the player wasn't the only one who didn't place the worker, then this method updates the turn and sets the phase to SETWORKER1 again
     *                           - Otherwise it updates the turn and sets model's phase to MOVE
     *
     *       If an error has been caught, it is sent only to che client which has generated it
     *
     *
     *      - If the phase is SETWORKER2 we can have 2 different cases:
     *                     - The player is not the last, then update turn and set the phase to SETWORKER1 again
     *                     - Otherwise it updates the turn and sets model's phase to MOVE only if the first player's god
     *                       card isn't Prometheus, in that case it sets the model's phase to WAIT_GOD_ANSWER
     * @param playerWorker is the Message subclass containing the information about player, and the cell chosen for the worker
     */
    @Override
    public synchronized void setPlayerWorker(PlayerWorker playerWorker){
        //Check for right turn
        if(!turnCheck(playerWorker)){
            return;
        }
        try{
            if(model.getBoard().getCell(playerWorker.getX(), playerWorker.getY()).isFree()){
                if(model.getPhase() == Phase.SETWORKER2){
                    if(model.getActualPlayerId() != model.getNumOfPlayers() - 1){
                        model.updateTurn();
                        model.setNextPhase(Phase.SETWORKER1);
                        model.setNextMessageType(MessageType.SET_WORKER_1);
                        model.setNextPlayerMessage(PlayerMessage.PLACE_FIRST_WORKER);
                    }
                    else{
                        model.updateTurn();
                        model.updatePhase();
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                        model.getActualPlayer().getGodCard().turnStartHandler(this, 0, null);
                    }

                }
                else{
                    model.updatePhase();
                    model.setNextMessageType(MessageType.SET_WORKER_2);
                    model.setNextPlayerMessage(PlayerMessage.PLACE_SECOND_WORKER);
                }
                model.setPlayerWorker(playerWorker);
                checkVictory();
            }
            else{
                playerWorker.getView().reportError("The cell is busy.");
            }
        }catch (IllegalArgumentException e){
            playerWorker.getView().reportError("Cell index must be between 0 and 4 (included)");
        }

    }

    /**
     * This method lets the first player choose 2 or 3 god cards (depending on the number of players in the game)
     * For each selected card the model's addGod function is called.
     * If the selected card number does not correspond to the number of players an error is reported and the action gets asked again
     * After the cards are saved, the phase is changed to PICK_CARD, in order to let the other player(s) choose a god card
     * @param drawedCards is the message sent by the view containing the drawed cards
     */
    public synchronized void drawedCards(DrawedCards drawedCards){
        if(drawedCards.getFirst() == drawedCards.getSecond() || drawedCards.getFirst()==drawedCards.getThird() || drawedCards.getSecond()==drawedCards.getThird()){
            if(drawedCards.getThird()!=-1) {
                drawedCards.getView().reportError("Please select 2 different cards");
            }else{
                drawedCards.getView().reportError("Please select 3 different cards");
            }
            return;
        }

        if(drawedCards.getThird() != -1 && model.getNumOfPlayers() == 3){
            model.addGod(Gods.getGod(drawedCards.getFirst()));
            model.addGod(Gods.getGod(drawedCards.getSecond()));
            model.addGod(Gods.getGod(drawedCards.getThird()));
        }
        else if(model.getNumOfPlayers() == 2 && drawedCards.getThird() != -1){
            drawedCards.getView().reportError("Insert 2 god cards only");
            return;
        }
        else{
            model.addGod(Gods.getGod(drawedCards.getFirst()));
            model.addGod(Gods.getGod(drawedCards.getSecond()));
        }
        model.notifyMessage(Gods.getGod(drawedCards.getFirst()).getName());
        model.notifyMessage(Gods.getGod(drawedCards.getSecond()).getName());
        if(model.getNumOfPlayers() == 3){
            model.notifyMessage(Gods.getGod(drawedCards.getThird()).getName());
        }
        model.updatePhase();
        model.updateTurn();
        model.setNextMessageType(MessageType.PICK_CARD);

        if(model.getNumOfPlayers()==3){
            model.setNextPlayerMessage("Pick a God between: \n0 - " + Gods.getGod(drawedCards.getFirst()).getName() + "\n1 - " + Gods.getGod(drawedCards.getSecond()).getName() + "\n2 - " + Gods.getGod(drawedCards.getThird()).getName());
        }
        else{
            model.setNextPlayerMessage("Pick a God between: \n0 - " + Gods.getGod(drawedCards.getFirst()).getName() + "\n1 - " + Gods.getGod(drawedCards.getSecond()).getName());
        }
        model.notifyChanges();
    }

    /**
     * This method controls and saves the card chosen by a player, if the card exists in the array containing the ones drawed,
     * the card is set and the phase updated to repeat the same action for the third player, otherwise it assigns the left card to the first player
     * and set the game phase to the SETWORKER1
     * @param pickedCard is the message sent by the view containing the selected card(s)
     */
    public synchronized void pickACard(PickedCard pickedCard){
        if(!turnCheck(pickedCard)){
            return;
        }

        if(pickedCard.getCardId() > model.getNumOfPlayers() - 1){
            pickedCard.getView().reportError("Insert a valid input");
            return;
        }
        if(model.isGodAvailable(pickedCard.getCardId())){
            String godName = model.assignCard(pickedCard.getPlayer(), pickedCard.getCardId()).getName();
            model.notifyMessage("You selected " + godName + ", good luck!");
        }
        else{
            pickedCard.getView().reportError("The selected god isn't available");
            return;
        }
        if(model.getLeftCards() == 1){
            model.updateTurn();
            String godName = model.assignCard(model.getActualPlayer(),0).getName();
            model.notifyMessage("Only " + godName + " was left, that's your card, good luck!");
            model.updatePhase();
            model.setNextMessageType(MessageType.SET_WORKER_1);
            model.setNextPlayerMessage(PlayerMessage.PLACE_FIRST_WORKER);
            model.notifyChanges();
        }
        else{
            model.updateTurn();
            model.setNextMessageType(MessageType.PICK_CARD);
            model.setNextPlayerMessage(PlayerMessage.PICK_CARD + "\n0 - " + model.getGods().get(0).getName() + "\n1 - " + model.getGods().get(1).getName());
            model.notifyChanges();
        }
    }

    /**
     * It overrides the Controller's method, it adds a function to reset some internal data referred to certain gods if needed, then it calls
     * another god card method to handle the move action and controls. If the controls's result is false, it means that there isn't any particular action that need to be done, then it
     * does the normal move, otherwise the normal control is skipped and each god card modifies the game flow as the power has defined.
     * Before confirming the normal move action, another function is called, the normalMoveModifier can change same model parameter, for example
     * Atlas card make a normal move but set next phase to ask god power.
     * At first it checks if it's the turn of the player who has sent the message
     * Then:
     *      - It checks if the selected worker can move using the function canMove
     *      - It calls checkCellAround to check if the selected cell is available in the map
     * If every control has a positive outcome, it sets the next model phase to BUILT, it updates messages and MessageTypes and updates the turn
     * It then updates the model with the new board configuration and notifies clients about the update
     * At the end it checks if anyone won or not
     *
     * @param move is the Message subclass containing the information needed
     */
    @Override
    public synchronized void move(PlayerMove move) {
        int oldLevel=model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId();
        if(!turnCheck(move)){
            return;
        }

        move.getPlayer().getGodCard().beforeMoveHandler(model, this, move);

        HashMap<Cell, Boolean> availableCells = checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));

        try{
            if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.setNextPhase(Phase.BUILD);
                    if(!move.getPlayer().getGodCard().handlerMove(model,this , move)) {
                        move.getPlayer().getGodCard().normalMoveModifier(model, this, move);
                        model.move(move);
                        model.notifyChanges();
                    }
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId() == 3 && oldLevel!=3){
                        model.setNextMessageType(MessageType.BUILD);
                        model.setNextPlayerMessage(PlayerMessage.BUILD);
                        model.setNextPhase(Phase.BUILD);
                        model.victory(move.getPlayer());
                    }
                    checkVictory();
                    if(move.getPlayer().getGodCard().checkCanBuild(move.getPlayer().getWorker(move.getWorkerId()), this).size()==0){
                        model.loose(move.getPlayer());
                        move.getView().reportError("You can't build anywhere, you lost");
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println(e.getMessage());
                }
            } else {
                move.getView().reportError("Unable to move in selected position");
            }
        }catch(IllegalArgumentException e){
            move.getView().reportError("Cell index must be between 0 and 4 (included)");
        }

    }

    /**
     * This method checks whether the worker which has been used to build the first time can build again
     * It's in particular useful with gods that can build two or more times.
     * If the player chooses to build again, but the worker can't, the model's loose function is called.
     * @param playerBuild is the build message received by the view
     */
    public synchronized void checkCantBuild(PlayerBuild playerBuild){
        Cell cell = model.getBoard().getCell(playerBuild.getX(), playerBuild.getY());
        Board board = model.getBoardClone();
        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                if((x >= 0 && x <= 4) && (y >= 0 && y <= 4)){
                    if(board.getCell(x,y).getLevel().getBlockId() != 4 && board.getCell(x,y).isFree()){
                        return;
                    }
                }
            }
        }
        model.loose(playerBuild.getPlayer());
    }

    /**
     * This method returns the amount of cells in which the checkCellAround returns true. The returned cell
     * represents the ability of a worker to move or not (if it returns zero it means the worker can't move)
     * @param worker is the player's chosen worker to move
     * @param player is the player who is trying to take the action
     * @return the number of cells in which the selected worker can move
     */
    @Override
    public synchronized int canMove(Worker worker, Player player){
        HashMap<Cell, Boolean> availableCells=checkCellsAround(worker);
        int numOfAvailableCells=0;
        for (Boolean can:availableCells.values()) {
            if(can){
                numOfAvailableCells++;
            }
        }
        return numOfAvailableCells;
    }

    /**
     *  At first it checks if the player which has sent the message is the turn's player
     *      It then calls the checkBuild function to verify whether the player can build there, otherwise it throws a new IllegalArgumentException
     *      After that, a god card build handler is called in order to check if the player card can modify the game flow or controls for that build
     *      If it can, the model is being updated with the next phase, the next turn and the next message by the card controller else a normal build is done
     *      At the end, another god card action is called to clear some flags if needed
     *      and then it checks whether anyone won or lost
     * @param playerBuild the Message subclass containing the whole information
     * @throws IllegalArgumentException if the cell is out of range
     */
    @Override
    public synchronized void build(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!turnCheck(playerBuild)){
            return;
        }
        if(playerBuild.getPlayer().getGodCard().checkBuilt(this, model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()), playerBuild)){
            if(!playerBuild.getPlayer().getGodCard().handlerBuild(model, this, playerBuild, model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()))){
                model.setNextMessageType(MessageType.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.updatePhase();
                model.updateTurn();
                godIncreaseLevel(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()).getLevel().getBlockId(), model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));
            }

            playerBuild.getPlayer().getGodCard().afterBuildHandler(model, this, playerBuild, model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));

        } else{
            throw new IllegalArgumentException();
        }
        checkVictory();
        //checkCantBuild(playerBuild);
    }



    /**
     * Modified increase level for gods card.
     * At this poing of the game the turn is already updated, a function to configure the god card turn start is then called
     * At the end the superclass increase level is called
     * @param blockId is the level id of the cell
     * @param buildingCell is the cell where we built
     */
    public void godIncreaseLevel(int blockId, Cell buildingCell) {
        model.getActualPlayer().getGodCard().turnStartHandler(this, blockId, buildingCell);
        super.increaseLevel(blockId, buildingCell);
    }

    /**
     * This function counts the amount of complete tower built in the board.
     * A complete tower is a build made from level 0 to 4 without using any god power
     * @return the calculated amount
     * */
    public synchronized int countTowers(){
        int counter=0;
        Board board=model.getBoard();
        for(int i=0; i<5;i++){
            for(int j=0; j<5; j++){
                if(board.getCell(i,j).isFull()){
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * This method checks if any of the win conditions within the cards in the game affects the victory of any player.
     * If none of them changed the game flow, the default function is recalled
     */
    @Override
    protected synchronized void checkVictory() {
        for(GodCard godCard : model.getGodsInPlay()){
            if(godCard.checkVictory(model, this))  return;
        }
        super.checkVictory();
    }

    /**
     * This method is used to determine in which cells the worker can move, associating every cell around the worker to a boolean value
     *  The check is made by a god card function by default, if the god card doesn't have any different implementation
     *   the GodCard.checkCell call the controller check cell {@link GodCard}
     * @param actualWorker is the worker who wants to move
     * @return a map containing the board cell as key and a boolean representing whether cell is available for the move or not
     */
    @Override
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker actualWorker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell actualWorkerCell = actualWorker.getCell();
        Board board = model.getBoard();
        Player actualPlayer = model.getActualPlayer();
        int maxUpDifference=2;
        if (model.isMovedUp()){
            maxUpDifference=1;
        }

        for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
            for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), actualPlayer.getGodCard().checkCell(this,x,y,actualWorker,maxUpDifference));
                }
                catch (IllegalArgumentException e){
                    Cell notAvailableCell = new Cell(x,y);
                    availableCells.put(notAvailableCell, false);
                }
            }
        }
        return availableCells;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }
}