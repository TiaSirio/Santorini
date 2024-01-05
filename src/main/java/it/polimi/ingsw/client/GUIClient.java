package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.Game;
import it.polimi.ingsw.client.GUI.Initialization;
import it.polimi.ingsw.model.messageModel.ClientConfigurator;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * GUI Client class
 */
public class GUIClient extends Observable<Object> {
    private final String ip;
    private final int port;
    private boolean active = true;
    private final Initialization initialization;
    private final Game game;
    private PrintWriter socketOut;
    private boolean isConfig = false;

    /**
     * Class constructor
     * @param ip the server ip
     * @param port the server port
     * @param width a width for scaling mode
     */
    public GUIClient(String ip, int port, int width){
        this.ip = ip;
        this.port = port;
        initialization= new Initialization(this);
        game = new Game(this, width);
        addObserver(initialization);
        addObserver(game);
    }

    /**
     * Open the game initializer
     */
    public synchronized void openInitializator() {
        initialization.setVisible(true);
    }

    /**
     * Close the game initializer
     */
    public synchronized void closeInitializator() {
        game.setEnabled(true);
        this.isConfig = true;
    }

    /**
     *
     * @return true if the client has been already cofing
     */
    public boolean isConfig() {
        return isConfig;
    }

    /**
     *
     * @return true if the connection is active
     */
    public synchronized boolean isActive(){
        return active;
    }

    /**
     *
     * @param active boolean containing the connection status
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
                        if(inputObject instanceof ClientConfigurator) {//Configurator for client data
                            ClientConfigurator clientConfigurator = (ClientConfigurator) inputObject;
                            game.setPlayer(clientConfigurator.getMyself());
                            game.setClientConfigurator(clientConfigurator);
                        } else {
                            notifyObservers(inputObject);
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
     * Send a message to the socket using the main thread
     * @param s a string containing the message
     */
    public synchronized void send(String s){
        socketOut.println(s);
        socketOut.flush();
    }

    /**
     * This method creates all objects the client need
     * @throws IOException if the input stream give an error
     */
    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);
        game.setVisible(true);
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
