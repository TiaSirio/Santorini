package it.polimi.ingsw.server;


import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.EndGameServerMessage;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.utils.ConnectionMessage;
import it.polimi.ingsw.utils.PlayerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class extends the abstract class named ClientConnection
 * This one is used to let the clients communicate through the socket
 */
public class SocketClientConnection extends ClientConnection implements Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;
    private boolean active = true;

    /**
     * Class' constructor
     * @param socket is the socket instance after a connection gets accepted by the server
     * @param server is the main server's instance which manages the connections
     */
    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    /**
     * This function checks if the connection is already active
     * @return true if the connection is active
     */
    private synchronized boolean isActive(){
        return active;
    }

    /**
     * This method writes the string message "out" on the socket
     * Every type of object is sent to the client except the EndGameServerMessage object, which is used
     * to configure a new lobby at the end of the game, just in case a player decides to play again
     * @param message is an Object representing the message that needs to be sent to the client
     */
    @Override
    public synchronized void send(Object message) {
        if(message instanceof EndGameServerMessage){
            server.addLobbyEndGame((EndGameServerMessage) message);
            return;
        }
            try {
                out.reset();
                out.writeObject(message);
                out.flush();
            } catch(IOException e){
                System.err.println(e.getMessage());
            }

    }

    /**
     * This function calls the socketClose function in order to close the connection.
     * It also sets the isActive flag to false
     */
    @Override
    public synchronized void closeConnection() {
        send(ConnectionMessage.CLOSE_SOCKET);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(ConnectionMessage.CLOSE_SOCKET_ERROR);
        }
        active = false;
    }

    /**
     * Call closeConnection and print on standard output that a connection had being closed
     */
    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    /**
     * This method create a new Thread runnable and from it call the send method
     * @param message Object representing the message to be sent to client
     */
    @Override
    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    /**
     * {@inheritDoc}
     * Create a new Scanner object and use it to read from socket
     * In this part is handled the configuration of the player
     * First ask his name, then an action between creating a lobby or join it
     * Case new lobby:
     *      ask the number of players
     *      ask the name of the lobby
     *      ask if the player want a simple play
     *      then call server addLobby method
     *
     * Case join lobby:
     *      ask server the list of available lobbies
     *      get the selected lobby id
     *      call server joinLobby function
     *
     * For all configuration, if input not match the step is asked again
     *
     * After created the lobby and while the isActive flag is true notify the observer
     * {@link it.polimi.ingsw.view.RemoteView} with the received message
     *
     */
    @Override
    public void run() {
        Scanner in;
        String name;
        int numPlayer = 0;
        boolean isConfig = false;
        try{
            String read;
            String lobbyName;
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            do{
            send(new ViewMessage(MessageType.PLAYER_NAME, PlayerMessage.WELCOME,null));
                read = in.nextLine();
                name = read;
            } while(read.isEmpty());
            int choice = 0;
            do {
                do {

                    send(new ViewMessage(MessageType.JOIN_OR_CREATE_LOBBY, PlayerMessage.GAME_MODE_SELECTOR,null));
                    if(in.hasNextInt()){
                        choice = in.nextInt();
                        in.nextLine();
                    }
                    else
                        in.next();
                } while (choice != 1 && choice != 2);
                if (choice == 1) {
                    do {
                        send(new ViewMessage(MessageType.NUMBER_OF_PLAYERS, PlayerMessage.ASK_NUM_PLAYER,null));
                        if(in.hasNextInt()) {
                            numPlayer = in.nextInt();
                            in.nextLine();
                        }else
                            in.next();
                    } while (numPlayer < 2 || numPlayer > 3);
                    do{
                    send(new ViewMessage(MessageType.LOBBY_NAME, PlayerMessage.ASK_LOBBY_NAME,null));
                    lobbyName = in.nextLine();
                    } while(lobbyName.isEmpty());
                    do{
                        send(new ViewMessage(MessageType.SIMPLE_OR_NOT, PlayerMessage.PLAY_MODE,null));
                        read = in.nextLine();
                        read = read.toLowerCase();
                    }while(!read.equals("y") && !read.equals("n"));
                    boolean simplePlay = false;
                    if (read.equals("y"))
                        simplePlay = true;
                    server.addLobby(lobbyName, this, name, numPlayer, simplePlay);
                    isConfig = true;

                } else {
                    try {
                        send(new ViewMessage(MessageType.LOBBY_SELECTOR, server.getLobbiesNames(),null));
                        int lobbyId;
                        if(in.hasNextInt()){
                            lobbyId = in.nextInt();//qua si bugga
                            in.nextLine();
                            if (lobbyId != 0) {
                                server.joinLobby(lobbyId, this, name);
                                isConfig = true;
                            }
                        }
                        else{
                            send("Invalid input");
                        }

                    } catch (FullLobbyException | InvalidLobbyException | NoLobbyException e){
                        send(e.getMessage());
                    } catch (UnavailablePlayerNameException e1){
                        send(e1.getMessage());
                        do {
                        send(new ViewMessage(MessageType.PLAYER_NAME, PlayerMessage.WELCOME,null));
                        name = in.nextLine();
                        read = name;
                        } while(read.isEmpty());
                    }

                }
            }while (!isConfig);

            //DA QUA INIZIA LA PARTITA
            while(isActive()){
                read = in.nextLine();
                notifyObservers(read);
            }
            /*read = in.nextLine();
            notifyObservers(read);*/
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            close();
        }
    }
}
