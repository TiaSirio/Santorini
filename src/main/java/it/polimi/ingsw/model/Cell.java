package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class defines a cell on the board.
 */
public class Cell implements Serializable, Cloneable{
    private Blocks level;
    private Blocks previousLevel = Blocks.EMPTY;
    private boolean isFree = true;
    private boolean isFull = false;

    private final int x;
    private final int y;

    /**
     * Constructor of the class
     * @param x is the x value of the new cell
     * @param y is the y value of the new cell
     */
    public Cell(int x, int y){
        this.level = Blocks.EMPTY;
        this.x = x;
        this.y = y;
    }

    /**
     * Private constructor to create a new cell using the clone method
     * @param x is the x value of the cloneable cell
     * @param y is the y value of the cloneable cell
     * @param level is the level of the cloneable cell
     * @param isFree is the boolean representing the cell's status
     * @param isFull is a boolean representing the fact that the cell has been built from level 0 to dome (meaning not using Atlas)
     */
    private Cell(int x, int y, Blocks level, boolean isFree, boolean isFull, Blocks previousLevel){
        this.x = x;
        this.y = y;
        this.isFree = isFree;
        this.level = level;
        this.isFull=isFull;
        this.previousLevel = previousLevel;
    }

    /**
     * @return true if the cell has been built from level 0 to dome
     */
    public boolean isFull(){
        return this.isFull;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Cell) {
            Cell otherCell = (Cell) obj;
            return otherCell.getX() == this.x && otherCell.getY() == this.y;
        } else return false;
    }

    /**
     * @return a string representing the cell's level and a boolean representing if it is free
     */
    @Override
    public String toString() {
        int full = 0;
        if(this.isFull()) full = 1;
        return "L:" + this.getLevel().getBlockId() + " F:" + full;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new Cell(x, y, level, isFree, isFull, previousLevel);
    }

    /**
     * @return the cell's x value
     */
    public int getX() {
        return x;
    }

    /**
     * @return the cell's y value
     */
    public int getY() {
        return y;
    }

    /**
     * @return the cell's level as a Blocks instance
     */
    public Blocks getLevel () {
        return level;
    }

    /**
     * @param level the Block's enum instance to be assigned to the cell
     */
    public void setLevel (Blocks level) {
        if(level==Blocks.DOME && this.level == Blocks.LEVEL3){
            this.isFull=true;
        }
        this.previousLevel = this.level;
        this.level = level;
    }

    /**
     * @return true if the cell is not used
     */
    public boolean isFree () {
        return isFree;
    }

    /**
     * This function sets the cell to free
     */
    public void freeCell () {
        this.isFree = true;
    }

    /**
     * This function sets the cell as used
     */
    public void useCell () {
        this.isFree = false;
    }

    /**
     * @return the cell's previews level as a Blocks instance
     */
    public Blocks getPreviousLevel(){
        return this.previousLevel;
    }

}