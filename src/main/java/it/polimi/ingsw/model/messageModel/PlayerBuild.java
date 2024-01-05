package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class notifies the controller that the player is trying to perform a build
 */
public class PlayerBuild extends Message {

    private final int x;
    private final int y;
    private final int workerId;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param workerId is the selected worker's id
     * @param x is the x value of the cell
     * @param y is the y value of the cell
     */
    public PlayerBuild(Player player, int workerId, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;
        this.workerId = workerId;
    }

    /**
     *
     * @return the selected x value of the cell
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the selected y value of the cell
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return the selected worker's id
     */
    public int getWorkerId(){
        return workerId;
    }

    /**
     * It calls the controller's build function
     * If it is not allowed by checkers, the IllegalArgumentException is catch and it
     * sends an error to the client requesting this action
     * @param controller is the game controller's instance
     */
    @Override
    public void handler(Controller controller) {

        try{
            controller.build(this);
        }catch(IllegalArgumentException e){
            this.getView().reportError("Can't build there");
        }
    }
}