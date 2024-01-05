package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUIClient;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.StrictMath.abs;

/**
 * Client executable class
 */
public class ClientApp {

    private static File propFile = new File("server.properties");
    private static Properties properties;

    /**
     * Client entry point main class
     * If the command line's arguments are null, the GUI client is created and started
     * If the command line's argument is -cli, the CLI client is created and started
     * Other cases generate errors that are printed in the standard output
     * @param args command arguments
     */
    public static void main(String[] args){
        Toolkit tk = Toolkit.getDefaultToolkit();
        properties = new Properties();
        String serverIp = "";
        try {
            InputStream inputStream = new FileInputStream(propFile);
            properties.load(inputStream);
            inputStream.close();
            serverIp = properties.getProperty("serverIp");
        } catch (IOException e){
            e.printStackTrace();
        }
        try{
            if(args.length!=0){
                if(args[0].equals("-cli")){
                    Client client = new Client(serverIp, 15986);
                    client.run();
                }
                else if(args[0].equals("-help")){
                    System.out.println("Here are the accepted arguments:\n" +
                            "EMPTY\t\t\t\t\t\t\tTo run the GUI client in full screen mode\n" +
                            "-*int 30 to 100*\t\t\t\tTo scale the GUI client based on the given number\n" +
                            "-*int 500 to screen size*\t\tTo set the GUI width to the given number\n" +
                            "-cli\t\t\t\t\t\t\tTo run the CLI client\n");
                }
                else{
                    try{
                        //se viene fornita una larghezza tra 500 e la larghezza dello schermo
                        if(abs(Integer.parseInt(args[0]))>500 && abs(Integer.parseInt(args[0]))<tk.getScreenSize().getWidth()){
                            GUIClient client = new GUIClient(serverIp, 15986, abs(Integer.parseInt(args[0])));
                            client.run();
                        }
                        else if(abs(Integer.parseInt(args[0]))>30 && abs(Integer.parseInt(args[0]))<100){
                            GUIClient client = new GUIClient(serverIp, 15986, abs(Integer.parseInt(args[0])));
                            client.run();
                        }
                        else{
                            System.out.println("Wrong args");
                        }
                    }catch(NumberFormatException e){//se non si sono inseriti numeri
                        System.out.println("Wrong args");
                    }
                }
            }
            else{
                GUIClient client = new GUIClient(serverIp, 15986, (int)tk.getScreenSize().getWidth());
                client.run();
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
