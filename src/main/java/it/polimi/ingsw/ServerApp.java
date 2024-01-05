package it.polimi.ingsw;

import it.polimi.ingsw.server.Server;

import java.io.IOException;

/**
 * Server executable class
 */
public class ServerApp
{
    /**
     * Server entry point main class
     * It creates a new server and runs it.
     * it prints errors in the standard output
     * @param args command parameters
     */
    public static void main( String[] args )
    {
        Server server;
        try {
            server = new Server();
            server.run();
        } catch (IOException e) {
            System.err.println("Impossible to initialize the server: " + e.getMessage() + "!");
        }
    }
}
