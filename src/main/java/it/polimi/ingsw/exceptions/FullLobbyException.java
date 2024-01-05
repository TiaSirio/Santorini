package it.polimi.ingsw.exceptions;

/**
 * Exception used to notify that a lobby is full
 */
public class FullLobbyException extends LobbyException {
    public FullLobbyException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public FullLobbyException(String errorMessage) {
        super(errorMessage);
    }
}
