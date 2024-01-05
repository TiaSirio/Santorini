package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 * This class represents Triton's GodCard
 */
public class Triton extends GodCard {
    private int usedWorkerID = -1;
    /**
     * {@inheritDoc}
     */
    public Triton() {
        super(Gods.TRITON, Phase.MOVE);
    }

    /**
     * @return the used worker's id
     */
    public int getUsedWorkerID(){
        return this.usedWorkerID;
    }

    /**
     *
     * @param worker is the id of the used worker at the first action
     */
    public void setUsedWorkerID(int worker){
        this.usedWorkerID =worker;
    }



    /**
     * This function sets the model's next messages and phase so that it makes the player move again
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.setNextMessageType(MessageType.MOVE);
        model.notifyChanges();
    }

    /**
     * This method adds a control before a move action is performed
     * If he tries to use the power but he cannot move, he looses
     * @param model is the game's model
     * @param controller is the game's controller
     * @param move is the move message received from the view
     */
    @Override
    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move) {
        if(controller.canMove(move.getPlayer().getWorker(move.getWorkerId()),move.getPlayer())==0){
            if(getUsedWorkerID()!=-1)
                model.loose(move.getPlayer());
        }
    }

    /**
     * This method changes the game's flow by asking the player to use the god's power after a move
     * In particular, this god can move every time he wants around the perimeter of the board. It checks if the selected worker is in the perimeter and if it's true
     * it then asks the player if he/she wants to use the power again, until he gets stuck, he moved away from the perimeter or he answered no
     * If the power has been used the worker flag is reset
     * @param model is the game's model
     * @param controller is the game's controller
     * @param move is the move's message received from the view
     * @return true if an error is reported or the flow has been modified, false otherwise
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(getUsedWorkerID()==-1){
            setUsedWorkerID(move.getWorkerId());
        }
        if(getUsedWorkerID() != move.getWorkerId()){
            move.getView().reportError("you have to move the same worker");
        }
        else if(move.getRow()==0 || move.getRow()==4 || move.getColumn()==0 || move.getColumn()==4 ){
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            model.move(move);
            model.notifyChanges();
        }
        else{
            setUsedWorkerID(-1);
            return false;
        }

        return true;
    }

    /**
     * When a no answer is given for the power, the flow is restored to built (as default)
     * @param phase is the god card phase given by the controller
     * @param controller is the game's controller
     */
    @Override
    public void performGodMessageForPhaseWithNegativeAnswer(Phase phase, Controller controller) {
        Model model = controller.getModel();
        setUsedWorkerID(-1);
        model.setNextPhase(Phase.BUILD);
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }
}
