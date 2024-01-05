package it.polimi.ingsw.model.gods;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.PlayerBuild;

/**
 * This class represents Zeus's GodCard
 */
public class Zeus extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Zeus() {
        super(Gods.ZEUS, Phase.BUILD);
    }

    /**
     * This method overrides the standard build controls
     * As documented, Zeus can also build in the same cell where the used worker is.
     * It controls if the cell level is not the third one and the cell is the selected worker's one
     * @param controller is the game controller
     * @param buildingCell is the cell where the player wants to build
     * @param playerBuild is the message received by the view
     * @return true if
     */
    @Override
    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {

        if(buildingCell.getLevel().getBlockId()==3 && buildingCell.equals(playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell())){
            return false;
        }

        return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (buildingCell.isFree() || (!buildingCell.isFree() && playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().equals(buildingCell))) &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3);
    }

}
