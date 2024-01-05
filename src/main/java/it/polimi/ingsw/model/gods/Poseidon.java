package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents Poseidon's GodCard
 */
public class Poseidon  extends GodCard {
    private int counter;
    private Worker unusedWorker = null;
    private Worker movedWorker = null;

    /**
     * {@inheritDoc}
     */
    public Poseidon() {
        super(Gods.POSEIDON, Phase.BUILD);
        counter = 0;
    }

    /**
     *
     * @return the number of times the player has built
     */
    public int getNumOfBuild() {
        return this.counter;
    }

    /**
     * Reset the build counter
     */
    public void resetBuild() {
        counter = 0;
    }

    /**
     * This method sets a variable containing the worker not used in the turn
     * @param worker the worker to set
     */
    public void setUnusedWorker(Worker worker) {
        this.unusedWorker = worker;
    }

    /**
     *
     * @return the worker not used in the turn, if it exists
     */
    public Worker getUnusedWorker() {
        return this.unusedWorker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {
        counter++;
        Model model = (Model) objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }

    /**
     *
     * @return the player's worker used in the turn
     */
    public Worker getMovedWorker() {
        return movedWorker;
    }

    /**
     * This function sets the player's worker used in the turn
     * @param movedWorker the used worker
     */
    public void setMovedWorker(Worker movedWorker) {
        this.movedWorker = movedWorker;
    }

    /**
     * This method modifies the normal build control.
     * Poseidon can build up to three more time with the worker he hasn't used, but only if that worker is at level 0 cell
     * If this is the first time the player builds, the used worker is set, and the unused one is computed and set (the same parameter is set in the player's instance)
     * After the first build, if the unused worker is at the level 0 and it didn't already build three times, it asks the player if he wants to use the god's power
     * Whenever one of the condition results false, the normal game flow is set and the flag reset
     * @param model is the game's model
     * @param controller is the game's controller
     * @param build is the message received from the view
     * @param buildingCell is the cell where the player wants to build
     * @return true if the unused worker is at level 0 and it didn't build three times, false otherwise
     */
    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(getMovedWorker()==null){
            setMovedWorker(build.getPlayer().getWorker(build.getWorkerId()));
            setUnusedWorker(build.getPlayer().getWorker((build.getWorkerId()+1)%2));
            build.getPlayer().setUsedWorker((build.getWorkerId()+1)%2);
        }
        ArrayList<Cell> availableCell=checkCanBuild(getUnusedWorker(), controller);
        boolean check=true;
        if(availableCell.size()==1){
            if(availableCell.get(0).getLevel()==Blocks.LEVEL3 && buildingCell == availableCell.get(0)){
                check=false;
            }
        }
        //se non ho costruito già 3 volte e se il worker inutilizzato e' al livello 0 e se il worker inutilizzato puo costruire
        if(getNumOfBuild()<3 && getUnusedWorker().getCell().getLevel().getBlockId()==0 && availableCell.size()>0 && check) {
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
            return true;
        }

        setUnusedWorker(null);
        setMovedWorker(null);
        resetBuild();
        return false;


    }

    /**
     * When the player doesn't want to use the power, the model keeps the usual flow, it then updates the turn and resets the flags
     * @param phase is the god card's phase given by the controller
     * @param controller is the game's controller
     */
    @Override
    public void performGodMessageForPhaseWithNegativeAnswer(Phase phase, Controller controller) {
        Model model = controller.getModel();
            setMovedWorker(null);
            setUnusedWorker(null);
            resetBuild();
            model.setNextPhase(Phase.MOVE);
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.setNextMessageType(MessageType.MOVE);
            model.updateTurn();
            model.getActualPlayer().getGodCard().turnStartHandler((GodCardController) controller, 0, null);
            model.notifyChanges();
    }
}
