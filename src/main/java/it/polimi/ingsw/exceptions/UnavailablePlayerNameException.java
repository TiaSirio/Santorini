package it.polimi.ingsw.exceptions;

/**
 * Exception used to notify that the selected player name is not available in a lobby
 */
public class UnavailablePlayerNameException extends LobbyException {
    public UnavailablePlayerNameException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public UnavailablePlayerNameException(String errorMessage) {
        super(errorMessage);
    }
}
