package it.polimi.ingsw.exceptions;

/**
 * General worker exception
 */
public class WorkerException extends RuntimeException{
    public WorkerException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }

    public WorkerException(String errorMessage){
        super(errorMessage);
    }


}
