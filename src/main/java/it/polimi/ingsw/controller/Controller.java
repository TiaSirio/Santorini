package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is the main Controller class. It defines some abstract methods used by subclasses and implements common methods
 */
public class Controller implements Observer<Message> {
    protected final Model model;
    protected final ArrayList<String> playersName = new ArrayList<>();
    protected final ArrayList<ClientConnection> clientConnections = new ArrayList<>();
    protected int counter = 0;
    protected int answers = 0;
    protected final Map<Player, ClientConnection> activeClients = new LinkedHashMap<>();

    /**
     * Class constructor
     * @param model isd the model instance generated for the game
     */
    public Controller(Model model){
        super();
        this.model = model;
    }

    /**
     *
     * @return the game model
     */
    public Model getModel(){
        return model;
    }

    /**
     * This function checks if the player who tried to do anything is actually allowed to,
     *  by checking if it's its turn
     * @param message is the message received from the view
     * @return true if now it's the given player's turn
     */
    public synchronized boolean turnCheck(Message message){
        if(!model.isPlayerTurn(message.getPlayer())){
            message.getView().reportError(PlayerMessage.TURN_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Default checker for a move
     * @param x is the x value of the cell
     * @param y is the y value of the cell
     * @param actualWorker is the worker which is performing the move
     * @param maxUpDifference represents the higher gap the worker can move upward. Is usually is set to 1, but this value can be changed by some god's powers.
     * @return true if the player can perform the desired move
     * @throws IllegalArgumentException if the cell values (x and y) are not between 0 and 4
     */
    public boolean checkCell (int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = model.getBoard().getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        return nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() - actualCell.getLevel().getBlockId() < maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
    }

    /**
     * This function updates the model giving it a new cell level
     * @param blockId is the integer id representing the Blocks enum instance
     * @param buildingCell the cell which level needs to be increased
     */
    public synchronized void increaseLevel(int blockId, Cell buildingCell){
        switch(blockId) {
            case 0:
                model.increaseLevel(buildingCell, Blocks.LEVEL1);
                break;
            case 1:
                model.increaseLevel(buildingCell, Blocks.LEVEL2);
                break;
            case 2:
                model.increaseLevel(buildingCell, Blocks.LEVEL3);
                break;
            case 3:
                model.increaseLevel(buildingCell, Blocks.DOME);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * It checks if the given building action can be performed.
     * It checks:
     *  1) if the given cell is next to the worker
     *  2) if it is not the cell where the player's worker is
     *  3) if the cell's coordinates are within the board range
     *  4) if the cell's level is not greater than level 3 (it can build the last level = DOME)
     *  5) if the cell is free
     * @param buildingCell is the cell where the player wants to build
     * @param playerBuild The Message subclass containing the information for this action
     * @return true if the player could build in the given cell
     */
    public synchronized boolean checkBuild(Cell buildingCell, PlayerBuild playerBuild){
        return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3) &&
                (buildingCell.isFree());
    }

    /**
     * This method checks if any player has won
     * This method uses the function canMove to decide whether a worker is able to move or not
     * If any player's workers can't move, it calls the model's loose method, giving it the player who looses as parameter
     */
    protected synchronized void checkVictory(){
        Player[] players = model.getPlayers();
            for(int i = 0; i < model.getNumOfPlayers(); i++){
                try{
                    players[i].getWorker(0).setStatus(canMove(players[i].getWorker(0), players[i])!=0);
                    players[i].getWorker(1).setStatus(canMove(players[i].getWorker(1), players[i])!=0);
                    //No Worker can move anywhere
                    if(!players[i].getWorker(0).getStatus() && !players[i].getWorker(1).getStatus()){// controllo se nessun worker si può muovere
                        model.loose(players[i]);
                    }
                }catch(IndexOutOfBoundsException e){
                    //Ignored: this happen only when the player is placing his worker
                }

            }

    }

    /**
     * This method is called when the game is over
     * When the number of received answers is equal to the number of players in the game it:
     *  CLEARS THE MODEL: it happens only if every player answered 'yes', then the model is cleared and a new game starts
     *  SENDS A MESSAGE TO THE SERVER: when the last player has answered and the number of 'yes' are less than
     *                       the number of players in the game, a EndGameServerMessage is sent to the server, containing the following information:
     *                       -  The name of the previous lobby, to create a new one using the old name.
     *                       -  The name of the last player which answered 'yes' (and he becomes the new first player of the game)
     *                       -  The name of the second player (if it exists) who answered yes
     *                       -  The instance of ClientSocketConnection of every player
     *                       -  The actual game mode (hard or simple)
     * CLOSES THE CONNECTION of every players who answered 'no'
     *
     * @param newGameMessage contains the player's reply after the following question has been given: "Would you like to play again?"
     */
    public synchronized void endGame(NewGameMessage newGameMessage){
        answers++;
        if(newGameMessage.getChoice() == 'y'){
            this.counter++;
            activeClients.put(newGameMessage.getPlayer(), newGameMessage.getClientConnection());
            if(counter == model.getNumOfPlayers()){
                newGameMessage.getLobby().resetEndGame();
                counter=0;
                answers=0;
                activeClients.clear();
                playersName.clear();
                model.startOver();
            } else if(answers == model.getNumOfPlayers()){
                for (Map.Entry<Player, ClientConnection> names: activeClients.entrySet()) {
                    this.playersName.add(names.getKey().getPlayerName());
                    this.clientConnections.add(names.getValue());
                }
                EndGameServerMessage endGameServerMessage = new EndGameServerMessage(newGameMessage.getLobby(),clientConnections,playersName,answers, model.isSimplePlay());
                newGameMessage.getClientConnection().send(endGameServerMessage);
            }

        } else {
            if(answers == model.getNumOfPlayers() && counter != 0){
                for (Map.Entry<Player, ClientConnection> names: activeClients.entrySet()) {
                    this.playersName.add(names.getKey().getPlayerName());
                    this.clientConnections.add(names.getValue());
                }
                EndGameServerMessage endGameServerMessage = new EndGameServerMessage(newGameMessage.getLobby(),clientConnections,playersName,answers, model.isSimplePlay());
                newGameMessage.getClientConnection().send(endGameServerMessage);
            }

            newGameMessage.getClientConnection().closeConnection();
        }

    }

    /**
     * This method checks if a given worker can move
     * For each cell around the worker it checks:
     *  - If the cell is free
     *  - If the cell is not the same one where the worker is
     *  - If the difference between the actual cell level and the next one is at most one
     *  - If the cell's level is less than 4 (meaning there is not a dome in that cell)
     *  If every test has been passed, a counter is incremented
     * @param worker is the player's worker which needs to be moved
     * @param player is the player who is trying to move
     * @return the number of cells which have passed the tests
     */
    protected synchronized int canMove(Worker worker, Player player){
        Cell actualCell = worker.getCell();
        int numOfAvailableCells=0;
        for (int x = actualCell.getX() - 1; x <= actualCell.getX() + 1; x++) {
            for (int y = actualCell.getY() - 1; y <= actualCell.getY() + 1; y++) {
                if(x >= 0 && y >= 0 && x < 5 && y < 5){
                    Cell nextCell = model.getBoard().getCell(x,y);
                    if(nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() -  actualCell.getLevel().getBlockId() < 2) && nextCell.getLevel().getBlockId() != 4){
                        numOfAvailableCells++;
                    }
                }
            }
        }
        return numOfAvailableCells;
    }

    /**
     * This method receives a the information to place a worker on the board.
     *
     * At first it checks if the player who has sent the message is the actual turn's player
     * If he is, the worker is placed on the board using the model's setWorker function
     * Then:
     *      - If the phase is SETWORKER1 then it changes the model phase to SETWORKER2 but it doesn't change the turn
     *      - If the phase is SETWORKER2 we can have 2 different alternatives:
     *                     - If the player wasn't the only one who didn't place the worker, then this method updates the turn and sets the phase to SETWORKER1 again
     *                     - Otherwise it updates the turn and sets model's phase to MOVE
     *
     * If an error has been caught, it is sent only to che client which has generated itù
     *
     * @param playerWorker the Message subclass containing the information about the player and the cell chosen for the worker to be placed
     *
     */
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
     * This method receives a move action
     *
     * At first it checks if the player who has sent the message is turn's player
     * Then:
     *      - it checks if the selected worker can move, calling the canMove method
     *      - it checks if the selected cell is in the map returned by the checkCellAround function, meaning you can move there
     * If every test is positive, it sets the next model's phase to BUILD, it updates the messages, and updates the turn
     * It then updates the model using the new board and notifies the clients
     * At the end, it checks if anyone won
     * If an error is caught, it is sent to the client which generated it
     *
     * @param move is the Message subclass containing every information needed
     */
    public synchronized void move(PlayerMove move) {
        boolean canMove;
        if(!turnCheck(move)){
            return;
        }
        canMove = move.getPlayer().getWorker(move.getWorkerId()).getStatus();
        if(!canMove){
            move.getView().reportError("This worker can't move anywhere");
            return;
        }
        HashMap<Cell, Boolean> availableCells = checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));

        try{
            if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.updatePhase();
                    model.move(move);
                    model.notifyChanges();
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel()==Blocks.LEVEL3){
                        model.victory(move.getPlayer());
                    }
                    checkVictory();
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
     * This method receives a build action
     *
     * At first it checks if the player who sent the message is the turn's player
     * Then it uses the checkBuilt function to check if the player can built there, otherwise it throws a new IllegalArgumentException
     * If it is possible, it updates the model setting the next phase, the next turn and the next message
     * The increase level function is then called
     *
     * At last it checks if anyone won/lost
     *
     * @param playerBuild is the Message subclass containing the every information needed
     * @throws IllegalArgumentException if the player can't built in the selected cell
     */
    public synchronized void build(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!turnCheck(playerBuild)){
            return;
        }

        Cell buildingCell = this.model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()); //ottengo la cella sulla quale costruire
        Blocks level = buildingCell.getLevel();//ottengo l'altezza della cella

        //qui devo fare i controlli
        if(checkBuild(buildingCell, playerBuild)){
            model.setNextMessageType(MessageType.MOVE);
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.updatePhase();
            model.updateTurn();

            increaseLevel(level.getBlockId(), buildingCell);
        }
        else{
            throw new IllegalArgumentException();
        }
        checkVictory();

    }

    /**
     *This method is used to determine in which cells the worker can move, associating every cell around the worker to a boolean value
     *
     * This method checks every cell around the worker, then using the checkCell function it associates a cell to the returned boolean
     * The IllegalArgumentException is caught for the perimeter cells
     *
     * @param actualWorker is the worker who wants to move
     * @return a map containing the board cell as key and a boolean representing whether cell is available for the move or not
     */
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker actualWorker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell actualWorkerCell = actualWorker.getCell();
        Board board = model.getBoard();
        for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
            for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), checkCell(x,y,actualWorker,2));
                }
                catch (IllegalArgumentException e){
                    Cell c= new Cell(x,y);
                    availableCells.put(c, false);
                }
            }
        }
        return availableCells;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }


}
