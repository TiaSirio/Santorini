package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class notifies the controller about the player's selected card
 */
public class PickedCard extends Message {
    private final int card;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param card is an int value representing the card selected by the player.
     */
    public PickedCard(Player player, View view, int card) {
        super(player, view);
        this.card = card;
    }

    /**
     * It calls the controller's pickACard function for this configuration
     * @param controller is the game controller's instance
     */
    @Override
    public void handler(Controller controller) {
        ((GodCardController)controller).pickACard(this);
    }

    /**
     *
     * @return the id of the selected card
     */
    public int getCardId() {
        return card;
    }
}
