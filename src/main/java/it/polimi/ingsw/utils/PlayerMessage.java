package it.polimi.ingsw.utils;

/**
 * Standard messages used during the play
 */
public class PlayerMessage {

    /* Configuration messages */

    public static final String WELCOME = "Welcome! \nWhat's your name?";
    public static final String ASK_NUM_PLAYER = "How many players?";
    public static final String GAME_MODE_SELECTOR = "Press 1 to create a new lobby, press 2 to join one.";
    public static final String ASK_LOBBY_NAME = "Insert a name for the lobby:";
    public static final String JOIN_LOBBY = "Select which lobby you want to join: ";
    public static final String WAIT_PLAYERS = "Waiting for other players";
    public static final String START_PLAY = "Beginning";
    public static final String TURN_ERROR = "Error, it's not your turn.";
    public static final String PLAY_MODE = "Would you like to play simple mode? (y/n)";

    /* Playing messages */
    public static final String DRAW_CARD = "Choose which Gods you want to use:\n" +
            "0 - APOLLO \n\tYour Worker may move into an opponent Worker’s\n" +
            "\tspace by forcing their Worker to\n" +
            "\tthe space yours just vacated.\n" +

            "1 - ARTEMIS \n\tYour Worker may move one additional time, but not\n" +
            "\tback to its initial space.\n" +

            "2 - ATHENA \n\tIf one of your Workers moved up on your last\n" +
            "\tturn, opponent Workers cannot\n" +
            "\tmove up this turn.\n" +

            "3 - ATLAS \n\tYour Worker may build a dome at any level.\n" +

            "4 - DEMETER \n\tYour Worker may build one additional time, but not\n" +
            "\ton the same space.\n" +

            "5 - HEPHAESTUS \n\tYour Worker may build one additional block (not\n" +
            "\tdome) on top of your first block.\n"+

            "6 - MINOTAUR \n\tYour Worker may move into an opponent Worker’s\n" +
            "\tspace, if their Worker can be\n" +
            "\tforced one space straight backwards to an\n" +
            "\tunoccupied space at any level.\n"+

            "7 - PAN \n\tYou also win if your Worker moves down two or\n" +
            "\tmore levels.\n" +

            "8 - PROMETHEUS \n\tIf your Worker does not move up, it may build both\n" +
            "\tbefore and after moving.\n" +

            "9 - CHRONUS \n\tYou also win when there are at least five\n" +
            "\tComplete Towers on the board.\n" +

            "10 - HESTIA \n\tYour Worker may build one additional time, but this\n" +
            "\tcannot be on a perimeter space.\n" +

            "11 - POSEIDON \n\tIf your unmoved Worker is on the ground level,\n " +
            "\tit may build up to three times.\n" +

            "12 - TRITON \n\tEach time your Worker moves into a perimeter\n" +
            "\tspace, it may immediately move again.\n" +

            "13 - ZEUS \n\tYour Worker may build a block under itself.\n";



    public static final String PICK_CARD = "Pick a card:";
    public static final String YOUR_TURN = "It's your turn.";
    public static final String NOT_YOUR_TURN = "'s turn.";
    public static final String PLACE_FIRST_WORKER = "Please place your first worker";
    public static final String PLACE_SECOND_WORKER = "Please place now your second worker";
    public static final String MOVE = "Now you can move";
    public static final String BUILD = "Now you can build";
    public static final String USE_POWER = "Would you like to use your God's power?(y/n)";
    public static final String PROMETHEUS_ASK_WORKER = "Which worker would you like to move?";
}
