package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gods.*;

/**
 * This class defines the different simpleGods of the game.
 */
public enum Gods {
    APOLLO(0),
    ARTEMIS(1),
    ATHENA(2),
    ATLAS(3),
    DEMETER(4),
    HEPHAESTUS(5),
    MINOTAUR(6),
    PAN(7),
    PROMETHEUS(8),
    CHRONUS(9),
    HESTIA(10),
    POSEIDON(11),
    TRITON(12),
    ZEUS(13)
    ;

    private final int simplegod;

    /**
     * Class' constructor
     * @param simplegod represents a specific God.
     */
    Gods(int simplegod) {
        this.simplegod = simplegod;
    }

    /**
     * This method assigns a value to its specific simpleGod.
     * @param id defines the simpleGod's id. The values accepted are: 0 = new Apollo(), 1 = new Artemis(), 2 = new Athena(), 3 = new Atlas(), 4 = new Demeter(), 5 = new Hephaestus(), 6 = new Minotaur(), 7 = new Pan(), 8 = new Prometheus(), 9 = new Chronus(), 10 = new Hestia(), 11 = new Poseidon, 12 = new Triton, 13 = new Zeus.
     * @return the simpleGod assigned to a given id.
     * @throws IllegalArgumentException if the id is less than 0 or higher than 13.
     */
    public static GodCard getGod(int id) throws IllegalArgumentException{
        switch(id) {
            case 0:
                return new Apollo();
            case 1:
                return new Artemis();
            case 2:
                return new Athena();
            case 3:
                return new Atlas();
            case 4:
                return new Demeter();
            case 5:
                return new Hephaestus();
            case 6:
                return new Minotaur();
            case 7:
                return new Pan();
            case 8:
                return new Prometheus();
            case 9:
                return new Chronus();
            case 10:
                return new Hestia();
            case 11:
                return new Poseidon();
            case 12:
                return new Triton();
            case 13:
                return new Zeus();
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * @return an int corresponding to a specific God.
     */
    public int getGodId() {
        return simplegod;
    }

}
