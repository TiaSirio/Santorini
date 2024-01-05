package it.polimi.ingsw.exceptions;

/**
 * General Lobby exception
 */
public class LobbyException extends RuntimeException {
    public LobbyException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }

    public LobbyException(String errorMessage){
        super(errorMessage);
    }
}
