package it.polimi.ingsw.exceptions;

/**
 * Exception used to notify that the selected lobby doesn't exist
 */
public class InvalidLobbyException extends LobbyException {

    public InvalidLobbyException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InvalidLobbyException(String errorMessage) {
        super(errorMessage);
    }
}
