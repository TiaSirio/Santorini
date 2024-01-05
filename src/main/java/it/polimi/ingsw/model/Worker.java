package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class defines a worker inside the game.
 */
public class Worker implements Serializable {
    private Cell cell;
    public boolean available = true; //può muoversi

    /**
     * Class' constructor.
     * @param cell the cell where place the worker.
     */
    public Worker(Cell cell){
        this.cell = cell;
    }

    /**
     *
     * @return the actual worker's cell.
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * This function changes the worker's cell when it moves or it gets pushed/swapped by a God
     * @param cell a new cell for the worker
     */
    public void setCell(Cell cell){
        this.cell = cell;
    }

    /**
     *
     * @return return false if the worker can't move
     */
    public boolean getStatus () {
        return available;
    }

    /**
     * This function sets the worker's status (can move or not)
     * @param status is a flag indicating if the worker can move
     */
    public void setStatus (boolean status) {
        this.available = status;
    }
}
