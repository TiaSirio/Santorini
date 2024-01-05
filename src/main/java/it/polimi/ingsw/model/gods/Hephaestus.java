
package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 This class is intended to represent the Hephaestus's GodCard
 */
public class Hephaestus extends GodCard {
    private Cell firstBuilt;

    /**
     * {@inheritDoc}
     */
    public Hephaestus() {
        super(Gods.HEPHAESTUS, Phase.BUILD);
    }

    /**
     * This method is used to get the first cell built by the player in this turn.
     * @return the first built cell.
     */
    public Cell getFirstBuilt() {
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
     * This method makes a player's worker build another time, in the same cell built in this turn.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     * @see Model {@link Model} In the model is contained the increaseLevel Method.
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model) objectList.get(0);
        if (model.getNextPlayerGC().getCardGod() == Gods.PROMETHEUS) {
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
        } else {
            model.setNextPhase(Phase.next(getPhase()));
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.setNextMessageType(MessageType.MOVE);
        }
        model.updateTurn();
        model.increaseLevel(firstBuilt, Blocks.getBlock(firstBuilt.getLevel().getBlockId() + 1));
    }

    /**
     * This method change game flow if the cell used to built by the player have a level less then 2, so Hepheastus can built an other time
     * It change game model phase to ask the usage of god power
     * @param model the play model
     * @param controller the play controller
     * @param build the message received by the view
     * @param buildingCell the cell where the player wants to build
     * @return true if the gale flow has been modified, else false
     */
    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(model.getBoard().getCell(build.getX(), build.getY()).getLevel().getBlockId()<2){
            setFirstBuilt(model.getBoard().getCell(build.getX(), build.getY()));
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
            return true;
        }
        return false;

    }
}
