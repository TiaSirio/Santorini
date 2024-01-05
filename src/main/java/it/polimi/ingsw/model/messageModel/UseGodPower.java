package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class notifies the controller the player's choice about using or not a god power
 */
public class UseGodPower extends Message {
    final char usePower;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param usePower char representing the player's choice
     */
    public UseGodPower(Player player, View view, char usePower) {
        super(player, view);
        this.usePower = usePower;
    }

    /**
     * If the player chooses to use the god power (y answer), it calls the god card instance's usePower function
     * otherwise it sets the next default model's configuration for each phase, based on the stage of the game
     * @param controller is the game controller's instance
     */
    @Override
    public void handler(Controller controller) {
        Model model = controller.getModel();
        GodCard playerGodCard = player.getGodCard();
        if(usePower == 'y'){
            playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
        }
        else{//se io dico di  no
            playerGodCard.performGodMessageForPhaseWithNegativeAnswer(playerGodCard.getPhase(), controller);
        }
    }
}
