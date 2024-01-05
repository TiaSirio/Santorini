
package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 This class represents Artemis' GodCard
 */
public class Artemis extends GodCard {
    private Cell firstMove;
    private boolean usedPower;
    private Worker previousWorker;

    /**
     * {@inheritDoc}
     */
    public Artemis() {
        super(Gods.ARTEMIS, Phase.MOVE);
    }

    /**
     * @return the cell corresponding to the first move made by the player.
     */
    public Cell getFirstMove(){
        return firstMove;
    }

    /**
     * @param firstBuilt is the cell corresponding to the first move made by the player.
     */
    public void setFirstMove(Cell firstBuilt){
        this.firstMove = firstBuilt;
    }

    /**
     * This method moves the player; it could be used only if the player decides to activate his power.
     * @param objectList contains the model of the actual game (objectList.get(0)).
     * @see PlayerMove {@link PlayerMove}
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.setNextMessageType(MessageType.MOVE);
        model.notifyChanges();
    }

    /**
     * Artemis can move twice but not in the starting cell
     * This method controls if it's the first time it moves and it asks if you want to use the power.
     * If this is the second time the player moves, it controls if the used worker is the same, the selected cell is not the one from which it moved
     * and then resets the flags and makes a normal move.
     * @param model is the game model
     * @param controller is the game controller
     * @param move is the move message received by the view
     * @return false only if this is the second time the player moves and every error control is negative
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(hasUsedPower()){
            if(getPreviousWorker() != move.getPlayer().getWorker(move.getWorkerId())){
                move.getView().reportError("You have to move the same worker");
            }
            else if(getFirstMove() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                move.getView().reportError("You can't move into the previous cell");
            }
            else{
                setUsedPower(false);
                return false;
            }
        }
        else{
            setFirstMove(model.getActualPlayer().getWorker(move.getWorkerId()).getCell());
            setPreviousWorker(model.getActualPlayer().getWorker(move.getWorkerId()));
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            model.move(move);
            if(controller.canMove(move.getPlayer().getWorker(move.getWorkerId()), move.getPlayer())==1){
                model.setNextPhase(Phase.BUILD);
                model.setNextPlayerMessage(PlayerMessage.BUILD);
                model.setNextMessageType(MessageType.BUILD);
            }
            model.notifyChanges();
        }
        return true;
    }
    /**
     * @return true if the power has already been used.
     */
    public boolean hasUsedPower() {
        return usedPower;
    }

    /**
     * @param usedPower is a flag that if it's true it means that the power has already been used.
     */
    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }

    /**
     * @return the worker that was used in the previous move (before using the power).
     */
    public Worker getPreviousWorker() {
        return previousWorker;
    }

    /**
     * @param previousWorker is the worker representing the previous move made by the player (before using the power).
     */
    public void setPreviousWorker(Worker previousWorker) {
        this.previousWorker = previousWorker;
    }
}
