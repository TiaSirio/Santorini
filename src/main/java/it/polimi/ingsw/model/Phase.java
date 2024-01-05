package it.polimi.ingsw.model;

/**
 * This class defines the different phases of the game in a normal match.
 */
public enum Phase {
    WAIT_PLAYERS(-5),
    DRAWCARD(-4),
    PICK_CARD(-3),
    SETWORKER1(-2),
    SETWORKER2(-1),
    BEGINNING(0),
    MOVE(1),
    BUILD(2),

    WAIT_GOD_ANSWER(5),
    PROMETHEUS_WORKER(6),

    END_GAME(10)
    ;

    private final int phaseId;

    /**
     * Class' constructor
     * @param phaseId is an integer representing the actual phase in the game.
     */
    Phase(int phaseId) {
        this.phaseId = phaseId;
    }

    /**
     * @return an integer corresponding to a phase in the game.
     */
    public int getPhaseId() {
        return phaseId;
    }

    /**
     * This method returns the next game's phase.
     * @param p defines the id of the actual phase. The accepted values are: -5 = DRAWCARD, -4 = PICK_CARD, -3 = SETWORKER1, -2 = SETWORKER2, -1 || 0 || 2 = MOVE, 1 || 6 = BUILD, 10 = END_GAME.
     * @return the next phase.
     * @throws IllegalArgumentException if the id is less than -5 or higher than 2, except for 6, 10.
     */
    public static Phase next(Phase p) throws IllegalArgumentException{
        int id = p.getPhaseId();
        switch (id){
            case -5:
                return Phase.DRAWCARD;
            case -4:
                return Phase.PICK_CARD;
            case -3:
                return Phase.SETWORKER1;
            case -2:
                return Phase.SETWORKER2;
            case -1:
            case 0:
            case 2:
                return Phase.MOVE;
            case 1:
            case 6:
                return Phase.BUILD;
            case 10:
                return Phase.END_GAME;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * This method assigns a value to its specific phase.
     * @param id defines the phase's id. The accepted values are: -5 = WAIT_PLAYERS, -4 = DRAWCARD, -3 = PICK_CARD, -2 = SETWORKER1, -1 = SETWORKER2, 0 = BEGINNING, 1 = MOVE, 2 = BUILD, 5 = WAIT_GOD_ANSWER, 6 = PROMETHEUS_WORKER, 10 = END_GAME.
     * @return the phase assigned to a given id.
     * @throws IllegalArgumentException if the id is less than -5 or higher than 2, except for 5, 6, 10.
     */
    public static Phase getPhase(int id) throws IllegalArgumentException {
        switch (id){
            case -5:
                return Phase.WAIT_PLAYERS;
            case -4:
                return Phase.DRAWCARD;
            case -3:
                return Phase.PICK_CARD;
            case -2:
                return Phase.SETWORKER1;
            case -1:
                return Phase.SETWORKER2;
            case 0:
                return Phase.BEGINNING;
            case 1:
                return Phase.MOVE;
            case 2:
                return Phase.BUILD;
            case 5:
                return Phase.WAIT_GOD_ANSWER;
            case 6:
                return Phase.PROMETHEUS_WORKER;
            case 10:
                return Phase.END_GAME;
            default:
                throw new IllegalArgumentException();
        }
    }



}
