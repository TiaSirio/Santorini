package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;

/**
 * This class represents Chronus's GodCard
 */
public class Chronus extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Chronus() {
        super(Gods.CHRONUS, Phase.BEGINNING);
    }

    /**
     * This method changes the winning conditions. Chronus wins if there are 5 full towers (built from 0 to DOME) on the board
     * When called, it counts the tower and, if there are 5 or more, it calls the controller's victory function for the player
     * @param model is the game's model
     * @param controller is the game's controller
     */
    @Override
    public boolean checkVictory(Model model, GodCardController controller) {
        if(!model.getGCPlayer(Gods.CHRONUS).hasWon() && !model.getGCPlayer(Gods.CHRONUS).getHasLost()){
            if(controller.countTowers()>=5){
                model.victory(model.getGCPlayer(Gods.CHRONUS));
                return true;
            }
        }
        return false;
    }
}
