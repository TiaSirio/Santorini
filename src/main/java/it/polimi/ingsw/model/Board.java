package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class defines the game's board.
 */
public class Board implements Serializable, Cloneable {
    private Cell[][] board;
    private final Player[] players;

    /**
     * This is the class' constructor.
     * @param players are the game's players
     */
    public Board(Player[] players){
        this.players = players;
        this.reset();
    }

    /**
     * @param x is the x value of the cell.
     * @param y is the y value of the cell.
     * @return the cell (x,y).
     * @throws IllegalArgumentException if the value 'x' or 'y' is higher or equal than 4 and if they are lower than 0.
     */
    public Cell getCell(int x, int y) throws IllegalArgumentException{
        if((x < 0 || x >= 5) || (y < 0 || y >= 5)){
            throw new IllegalArgumentException();
        }
        return board[x][y];
    }

    /**
     * This method resets the board and sets all the previous cells to new ones.
     */
    public void reset(){
        board=new Cell[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                board[i][j] = new Cell(i,j);
            }
        }
    }

    /**
     * @return the board as a string.
     */
    public synchronized String print(){
        StringBuilder s=new StringBuilder();
        try {
            s.append("         0         1         2         3         4\n");
            s.append("    ---------------------------------------------------\n");
            for (int i = 0; i < 5; i++) {
                s.append(i).append("   | ");
                for (int j = 0; j < 5; j++) {//prima riga
                    s.append(board[i][j]).append(" | ");
                }
                s.append("\n");
                //qui devo stampare player e worker (seconda riga)
                s.append("    ");
                for (int j = 0; j < 5; j++) {
                    s.append("| ");
                    if (!board[i][j].isFree()) {
                        for (int p = 0; p < players.length; p++) {
                            Player player = players[p];

                            if (player.getWorker(0).getCell().equals(board[i][j])) {
                                s.append("P:").append(p).append(" W:0 ");
                                break;
                            } else if (player.getWorker(1).getCell().equals(board[i][j])) {
                                s.append("P:").append(p).append(" W:1 ");
                                break;
                            }

                        }
                    } else {
                        s.append("P:n W:n ");
                    }
                }
                s.append("|");
                s.append("\n    ---------------------------------------------------\n");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    /**
     * @return the players using this board
     */
    public Player[] getPlayers(){
        return players;
    }

    /**
     * @return a new instance of the class as a clone
     * @throws CloneNotSupportedException if it can't be cloneable
     */
    @Override
    protected final Board clone() throws CloneNotSupportedException{
        super.clone();
        final Board result = new Board(players);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++)
            result.board[i][j] = (Cell) board[i][j].clone();
        }
        return result;
    }
}
