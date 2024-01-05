
package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 This class is intended to represent the Demeter's GodCard
 */
public class Demeter extends GodCard {
    private Cell firstBuilt;
    private boolean usedPower;

    /**
     * {@inheritDoc}
     */
    public Demeter() {
        super(Gods.DEMETER, Phase. BUILD);
    }

    /**
     * This method is used to get the first cell built by the player in this turn.
     * @return the first built cell.
     */
    public Cell getFirstBuild() {
        return firstBuilt;
    }

    /**
     * This method is used to set the first building's cell built by the player.
     * @param firstBuilt represent the first cell built by the player in this turn.
     */
    public void setFirstBuilt(Cell firstBuilt) {
        this.firstBuilt = firstBuilt;
    }

    /**
     * This method makes a player's worker build another time, but not on the same position; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     * @see Model {@link Model} In the model is contained the increaseLevel Method.
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }

    /**
     * @return true if the power has already been used.
     */
    public boolean hasUsedPower() {
        return usedPower;
    }

    /**
     * @param usedPower is a flag that if it's set to true means that the power has already been used.
     */
    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }

    /**
     * Modified controller for build action
     * Demeter can built two times but not in the same cell
     * This method check that if the build is the first, modify the game flow in order to ask the player to use the god power
     * Instead, if this is the second built, check that the cell is not the same (if it is, report an error and return true to block the standard build)
     * if not, reset the build flag and return false for using the standard control
     * @param model the play model
     * @param controller the play controller
     * @param build the message received by the view
     * @param buildingCell the cell where the player wants to build
     * @return true if this is the first built or the player is trying to build in the same cell, else false
     */
    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(hasUsedPower()){
            if(getFirstBuild() == model.getBoard().getCell(build.getX(), build.getY())){
                build.getView().reportError("You can't build into the previous cell");
                return true;
            } else{
                setUsedPower(false);
                return false;
            }

        } else{
            setFirstBuilt(model.getBoard().getCell(build.getX(), build.getY()));
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
            return true;
        }

    }
}
