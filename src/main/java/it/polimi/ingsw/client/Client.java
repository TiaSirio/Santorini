package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * CLI Client class
 */
public class Client {
    private final String ip;
    private final int port;
    private boolean active = true;
    private Player player;

    /**
     * Class constructor
     * @param ip server ip
     * @param port server port
     */
    public Client(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    /**
     *
     * @return true if the connection is active
     */
    public synchronized boolean isActive(){
        return active;
    }

    private synchronized void handleTurnMessage(ViewMessage arg, Player player) {
        if(arg.getMessageType() == MessageType.VICTORY || arg.getMessageType() == MessageType.END_GAME || arg.getMessageType() == MessageType.LOSE){
            System.out.println(arg.getMessage());
            return;
        }
        if (this.player.equals(player)) {
            if(arg instanceof GameBoardMessage){
                System.out.println(((GameBoardMessage) arg).getBoard().print());
            }
            System.out.println(arg.getMessage());
        } else if ((arg.getPhase() == Phase.BEGINNING) && !this.player.equals(player)) {
            if(arg instanceof GameBoardMessage){
                System.out.println(((GameBoardMessage) arg).getBoard().print());
            }
            System.out.println("It's now " + player.getPlayerName() + "'s turn");
        }
    }

    private synchronized void messageHandler(ViewMessage arg) {
        if(arg instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) arg;
            handleTurnMessage(gameMessage, gameMessage.getPlayer());
        } else {
            System.out.println(arg.getMessage());
        }
    }

    /**
     *
     * @param active a boolean representing the connection status
     */
    public synchronized void setActive(boolean active){
        this.active = active;
    }

    /**
     * This method is used to receive messagges from the server throw the network using a different thread
     * @param socketIn ObjectInputStream instance for receiving messages from the socket
     * @return the created thread
     */
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if(inputObject instanceof String){//se viene passata una stringa
                            System.out.println((String)inputObject);
                        } else if(inputObject instanceof ClientConfigurator) {
                            player = ((ClientConfigurator) inputObject).getMyself();
                        } else if (inputObject instanceof Board) { // se viene passata una board
                            ((Board) inputObject).print();
                        } else if (inputObject instanceof ViewMessage) {
                            ViewMessage viewMessage=(ViewMessage)inputObject;
                            messageHandler(viewMessage);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                } catch (Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    /**
     * This method is used to send messages throw the socket using a different thread
     * @param stdin Scanner instance for reading inputs from command line
     * @param socketOut the PrintWriter instance for writing messages in the socket
     * @return the created thread
     */
    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        socketOut.println(inputLine);
                        socketOut.flush();
                    }
                }catch(Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    /**
     * This method creates all objects the client need
     * @throws IOException if the input stream give an error
     */
    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);

        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            t1.join();//non dovrebbe fare qualcosa?
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
