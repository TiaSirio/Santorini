package it.polimi.ingsw.utils;

/**
 * This class includes some utilities to parse the strings
 */
public class Parser {

    /**
     * This function returns the capitalize version of the same string
     * @param string the string that needs to be capitalized
     * @return the string in capitalized
     */
    public static String toCapitalize(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }

    /**
     * This function transforms the lobbies list string to a 2dimensional array
     * it is used to fill the lobbies table
     * @param inputMessage the lobbies string list
     * @return a matrix containing for each lobby the id, the name, number of players and the game mode
     */
    public static String[][] parseLobbies(String inputMessage){
        String[] split = inputMessage.split("\n");
        String[][] lobbies = new String[split.length-2][4];
        for(int i=2; i<split.length; i++){
            String lobby = split[i];
            lobby = lobby.replace('\t', ' ');
            String id = lobby.substring(0, lobby.indexOf('-')).trim();
            lobby = lobby.substring(lobby.indexOf('-')+2);
            String name = lobby.substring(0, lobby.indexOf('[')).trim();
            lobby = lobby.substring(lobby.indexOf('[')+1);
            String players = lobby.substring(0, lobby.indexOf(']'));
            lobby = lobby.substring(lobby.indexOf(']')+1);
            String type = lobby.trim();
            lobbies[i-2][0] = id;
            lobbies[i-2][1] = name;
            lobbies[i-2][2] = players;
            lobbies[i-2][3] = type;
        }
        return lobbies;
    }

}
