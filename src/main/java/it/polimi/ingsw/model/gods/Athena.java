
package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 This class represents Athena's GodCard
 */
public class Athena extends GodCard {

    /**
     * {@inheritDoc}
     */
    public Athena() {
        super(Gods.ATHENA, Phase.MOVE);
    }

    /**
     * This method makes the setMovedUp static boolean (contained in the Model) to true, so the other player(s) can't move up; it could only be used if the player decides to activate his power.
     * @param objectList contains the actual game's model (objectList.get(0)).
     * @see Model {@link Model} The model has the MovedUp value.
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.setMovedUp(true);
    }

    /**
     * Before Athena performs a move, it resets the flag's value
     * @param model is the game's model
     * @param controller is the game's controller
     * @param move is the move message received by the view
     */
    @Override
    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move) {
        model.setMovedUp(false);
    }

    /**
     * Athena's power is automatically activated. If she moved up the flag is set to true, otherwise false
     * @param model is the game's model
     * @param controller is the game's controller
     * @param move is the move message received by the view
     * @return always true because the flag has changed
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId() > model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId()){
            move.getPlayer().getGodCard().usePower(new ArrayList<Object>(Collections.singletonList(model)));
        }
        else{
            model.setMovedUp(false);
        }
        model.move(move);
        model.notifyChanges();
        return true;
    }
}
