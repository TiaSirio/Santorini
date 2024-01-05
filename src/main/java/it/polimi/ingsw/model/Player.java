package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullWorkerException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class defines a player in the game.
 */
public class Player implements Serializable {
    private final ArrayList<Worker> workers;
    private final String playerName;
    private GodCard godCard = null;
    private boolean hasLost = false;
    private boolean usePower = false;
    private boolean victory = false;
    private int usedWorker = 0;

    /**
     * Class' constructor
     * @param playerName is a string representing the player's name.
     */
    public Player(String playerName) {
        this.playerName = playerName;
        workers = new ArrayList<>();
    }

    /**
     *
     * @return true if this player has won the game
     */
    public boolean hasWon() {
        return victory;
    }

    /**
     * @param victory is a boolean representing whether the player has won or not
     */
    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    /**
     *
     * @return true if the player has used his god's power in his turn
     */
    public boolean getUsePower() {
        return usePower;
    }

    /**
     * Set the usePower to the default value (False)
     */
    public void resetUsePower(){
        usePower = false;
    }

    /**
     * @param usePower sets the value for the usePower's flag
     */
    public void setUsePower(boolean usePower){
        this.usePower = usePower;
    }

    /**
     *
     * @param status sets the hasLost flag
     */
    public void setHasLost(boolean status){
        hasLost = status;
    }

    /**
     *
     * @return true if the player has lost
     */
    public boolean getHasLost(){
        return hasLost;
    }

    /**
     *
     * @return a string containing the name of the Player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     *
     * @return the GodCard instance if the player has it, returns null otherwise
     */
    public GodCard getGodCard() {
        return this.godCard;
    }

    /**
     *
     * @param godCard is  the GodCard that needs to be assigned to the player
     */
    public void setGodCard(GodCard godCard){
        this.godCard = godCard;
    }

    /**
     *
     * @param number is the number of the worker we want, between 0 and 1
     * @return the Worker's instance corresponding to the number
     * @throws IndexOutOfBoundsException if the number is less than 0 or greater than 1
     */
    public Worker getWorker(int number) throws IndexOutOfBoundsException {
        return (workers.get(number));
    }


    /**
     *
     * @param worker is a worker's class instance
     * @throws FullWorkerException if the player already has two workers set
     */
    public void setWorkers(Worker worker) throws FullWorkerException{
        if(workers.size() == 2){
            throw new FullWorkerException("Went over the number of workers allowed");
        }
        this.workers.add(worker);
    }

    /**
     *
     * @return the selected worker used in the turn
     */
    public int getUsedWorker() {
        return usedWorker;
    }

    public int getUnusedWorker(){
        return  (usedWorker+1)%2;
    }

    /**
     *
     * @param usedWorker the worker that the player has decided to use in his turn
     */
    public void setUsedWorker(int usedWorker) {
        this.usedWorker = usedWorker;
    }

    /**
     * Reset every data except the name, this function is used when a neg game starts
     */
    public void reset(){
        this.hasLost = false;
        this.victory = false;
        godCard = null;
        workers.remove(1);
        workers.remove(0);
    }

    /**
     * Remove the player's workers
     */
    public void remove(){
        for (Worker w : workers){
            w.getCell().freeCell();
            //w.setCell(null);
        }


    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((playerName == null) ? 0 : playerName.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (playerName == null) {
            return other.playerName == null;
        } else return playerName.equals(other.playerName);
    }
}

