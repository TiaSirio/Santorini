package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 * This class represents Hestia's GodCard
 */
public class Hestia extends GodCard {
    private boolean hasBuilt=false;
    /**
     * {@inheritDoc}
     */
    public Hestia() {
        super(Gods.HESTIA, Phase.BUILD);
    }

    public void setHasBuilt(boolean hasBuilt){
        this.hasBuilt=hasBuilt;
    }

    public boolean hasBuilt(){
        return this.hasBuilt;
    }

    /**
     * {@inheritDoc}
     * In this case the only parameter is the model,
     * If this function is called, it lets the player build the first time and set te model parameters
     * for letting him build again
     */
    @Override
    public void usePower(List<Object> objectList) {
        this.hasBuilt=true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }

    /**
     * This method modifies the check cell's tests if Hestia built the second time.
     * If the player hasn't built, the super checkBuilt function is called
     * If it's the second build, it checks that the selected cell is not on the board's perimeter
     * @param controller is the game's controller
     * @param buildingCell is the cell where player wants to build
     * @param playerBuild is the message received by the view
     * @return the super method results if it is not the second built, otherwise it returns true if the selected cell is
     *      not in the perimeter, false otherwise
     */
    @Override
    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {
        if(hasBuilt()){
            return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                    Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                    (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                    (buildingCell.getX() > 0 && buildingCell.getX() < 4) &&
                    (buildingCell.getY() > 0 && buildingCell.getY() < 4) &&
                    (buildingCell.getLevel().getBlockId() <= 3) &&
                    (buildingCell.isFree());
        }
        return super.checkBuilt(controller,buildingCell, playerBuild);
    }

    /**
     * This method overrides the default build controls.
     * If the player just built the first time, it modifies the game flow and asks if the player wants to use the god power, otherwise it resets the build flag,
     * it returns false and uses the standard flow.
     * @param model is the game's model
     * @param controller is the game's controller
     * @param build is the message received from the view
     * @param buildingCell is the cell where the player wants to build
     * @return true if the cell's control is positive and if the second build isn't done, false otherwise
     */
    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(!hasBuilt()) {
            if (checkHestiaCells(model, build) == 1) {
                if (model.getBoard().getCell(build.getX(), build.getY()).getLevel() != Blocks.LEVEL3) {
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                    controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
                    return true;
                }
            } else if(checkHestiaCells(model, build) > 1)  {
                model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                model.setNextMessageType(MessageType.USE_POWER);
                controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
                return true;
            }
        }

        setHasBuilt(false);
        return false;
    }

    /**
     * Modified controller for Hestia, it checks if the cell is not in the perimeter
     * @param model is the game's model
     * @param playerBuild is the build message received from the view
     * @return true if it can build in the selected cell, false otherwise
     * The IllegalArgumentException is ignored due to return a false result
     */
    private int checkHestiaCells(Model model, PlayerBuild playerBuild){
        int x=playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX();
        int y=playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY();
        Board board=model.getBoard();
        int counter=0;
        for(int i=x-1; i<=x+1; i++){
            for(int j=y-1; j<=y+1; j++){
                try{
                    if(board.getCell(i,j).getLevel()!= Blocks.DOME && i!=0 && i!=4 && j!=0 && j!=4 && board.getCell(i,j).isFree()){
                        if(!(i==x && j==y)) {
                            counter++;
                        }
                    }
                }catch(IllegalArgumentException e){
                    //ignore
                }
            }
        }
        return counter;
    }
}
