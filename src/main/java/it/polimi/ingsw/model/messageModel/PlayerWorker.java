package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class notifies the position where a certain player wants to place a worker
 */
public class PlayerWorker extends Message{

    private final int x;
    private final int y;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param x is the x value of the cell
     * @param y is the y value of the cell
     */
    public PlayerWorker(Player player, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;

    }

    /**
     *
     * @return the x value of the cell
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the y value of the cell
     */
    public int getY() {
        return y;
    }

    /**
     * It calls the controller's setPlayerWorker function
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        controller.setPlayerWorker(this);
    }
}


