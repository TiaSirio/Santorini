package it.polimi.ingsw.exceptions;

/**
 * Exception used to notify that a player has already placed every worker
 */
public class FullWorkerException extends WorkerException {

    public FullWorkerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public FullWorkerException(String errorMessage) {
        super(errorMessage);
    }
}
