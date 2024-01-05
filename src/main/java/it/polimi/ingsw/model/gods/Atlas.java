
package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 This class represents Atlas's GodCard
 */
public class Atlas extends GodCard {
    private boolean usedPower = false;

    /**
     * {@inheritDoc}
     */
    public Atlas() {
        super(Gods.ATLAS, Phase.MOVE);
    }

    /**
     * This method makes a player's worker build a DOME at any level; it could only be used if the player decides to activate it.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(Phase.next(getPhase()));
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
     * Atlas' power has to be activated after a normal move. This function modifies the model's next phase and messages when a normal move is performed
     * @param model is the game's model
     * @param controller is the game's controller
     * @param move is the move message received by the view
     */
    @Override
    public void normalMoveModifier(Model model, GodCardController controller, PlayerMove move) {
        model.setNextPhase(Phase.WAIT_GOD_ANSWER);
        model.setNextPlayerMessage(PlayerMessage.USE_POWER);
        model.setNextMessageType(MessageType.USE_POWER);
    }

    /**
     * If the power is activated this method build a DOME in the selected cell and return true
     * @param model is the game's model
     * @param controller is the game's controller
     * @param build is the message received by the view
     * @param buildingCell is the cell where the player wants to build
     * @return true if a DOME has been built, false otherwise
     */
    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(hasUsedPower()) {
            setUsedPower(false);
            model.setNextMessageType(MessageType.MOVE);
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.updatePhase();
            model.updateTurn();
            model.getActualPlayer().getGodCard().turnStartHandler(controller, 0, null);
            model.increaseLevel(buildingCell, Blocks.DOME);
            return true;
        }

        return false;
    }

    /**
     * @param usedPower is a flag that if it's set to true it means that the power has already been used.
     */
    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }
}
