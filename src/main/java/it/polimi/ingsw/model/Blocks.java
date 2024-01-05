package it.polimi.ingsw.model;

/**
 * This class defines the different blocks' levels in the game.
 */
public enum Blocks {
    EMPTY(0),
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3),
    DOME(4)
    ;

    private final int block;

    /**
     * This is the class' constructor
     * @param block is the level of a cell.
     */
    Blocks(int block) {
        this.block = block;
    }

    /**
     * @return an int representing the actual level of a cell.
     */
    public int getBlockId() {
        return block;
    }

    /**
     * This method assigns a value to its specific block.
     * @param id defines the block's id. The accepted values are: 0 = EMPTY, 1 = LEVEL1, 2 = LEVEL2, 3 = LEVEL3, 4 = DOME.
     * @return the block assigned to an id.
     * @throws IllegalArgumentException if the id is less than 0 or higher than 4.
     */
    public static Blocks getBlock(int id) throws IllegalArgumentException {
        switch (id){
            case 0:
                return Blocks.EMPTY;
            case 1:
                return Blocks.LEVEL1;
            case 2:
                return Blocks.LEVEL2;
            case 3:
                return Blocks.LEVEL3;
            case 4:
                return Blocks.DOME;
            default:
                throw new IllegalArgumentException();
        }
    }
}
