package it.polimi.ingsw.model.messageModel;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class notifies the controller that the player is trying to perform a move
 */
public class PlayerMove extends Message{
    private final int x;
    private final int y;
    private final int workerId;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param workerId is the selected worker's id
     * @param x is the new x cell value
     * @param y is the new y cell value
     */
    public PlayerMove(Player player, int workerId, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;
        this.workerId = workerId;
    }

    /**
     *
     * @return selected worker's id
     */
    public int getWorkerId(){
        return workerId;
    }

    /**
     *
     * @return return the selected c value
     */
    public int getRow() {
        return x;
    }

    /**
     *
     * @return the selected y value
     */
    public int getColumn() {
        return y;
    }


    /**
     * It calls the controller's move
     * @param controller is the game controller's instance
     */
    @Override
    public void handler(Controller controller) {
        controller.move(this);
    }
}