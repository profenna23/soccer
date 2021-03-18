package cs301.Soccer;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // Declare and instantiate a Hashtable instance variable
    private HashMap<String, SoccerPlayer> database = new HashMap<>();

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        // checkpoint 1A
        String fullPlayerName = containsKey(firstName, lastName);
        // check if player is in database
        if (database.containsKey(fullPlayerName)){
            return false;
        } else{
            // create soccer player object
            SoccerPlayer newPlayer = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
            // put it into the hashtable
            database.put(fullPlayerName, newPlayer);
            return true;
        }

    }

    private String containsKey(String firstName, String lastName) {
        return firstName + " ## " +lastName;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        // checkpoint 2
        String key = containsKey(firstName, lastName);
        if (database.containsKey(key)) {
            // player found; remove
            database.remove(key);
            return true;
        }
        // player not found
        return false;

    }

    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {

        // checkpoint 1B
        String key = containsKey(firstName, lastName);
        if (database.containsKey(key)){
            return database.get(key);
        } else{
            return null;
        }

    }

    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        // checkpoint 3
        SoccerPlayer player = getPlayer(firstName, lastName);
        if (player != null){
            player.bumpGoals();
            return true;
        } else{
            return false;
        }
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        // checkpoint 3
        SoccerPlayer player = getPlayer(firstName, lastName);
        if (player != null){
            player.bumpYellowCards();
            return true;
        } else{
            return false;
        }
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        // checkpoint 3
        SoccerPlayer player = getPlayer(firstName, lastName);
        if (player != null){
            player.bumpRedCards();
            return true;
        } else{
            return false;
        }
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    // report number of players on a given team (or all players, if null)
    public int numPlayers(String teamName) {
        // checkpoint 4
        if (teamName == null){
            return database.size();
        }
        int counter = 0;
        for (SoccerPlayer player: database.values()){
            if (player.getTeamName().equals(teamName)){
                counter++;
            }
        }
        return counter;
    }

    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        // checkpoint 5
        int counter = 0;
        for (SoccerPlayer player: database.values()){
            if (teamName == null) {
                if (counter == idx) {
                    return player;
                } else {
                    counter++;
                }
            } else if (player.getTeamName().equals(teamName)){

                    if (counter == idx) {
                        return player;
                    } else {

                        counter++;
                    }
            }
        }

        return null;

    }

    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(java.io.File)
     */
    // read data from file
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean readData(File file) {
        try {
            //File theFile = new File(String.valueOf(file));
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {

                String firstName = myReader.nextLine();
                String lastName = myReader.nextLine();
                int uniformNum = Integer.parseInt(myReader.nextLine());
                String teamName = myReader.nextLine();
                int goalsScored = Integer.parseInt(myReader.nextLine());
                int yellowCards = Integer.parseInt(myReader.nextLine());
                int redCards = Integer.parseInt(myReader.nextLine());

                String fullPlayerName = firstName + "##" + lastName;
                SoccerPlayer newPlayer = new SoccerPlayer(firstName, lastName, uniformNum, teamName);

                if (database.containsKey(fullPlayerName)){
                    database.replace(fullPlayerName, newPlayer);
                } else{
                    database.put(fullPlayerName, newPlayer);
                }
                for (int i=0; i< goalsScored; i++){
                    newPlayer.bumpGoals();
                }
                for (int j=0; j< yellowCards; j++){
                    newPlayer.bumpYellowCards();
                }
                for (int k=0; k< redCards; k++){
                    newPlayer.bumpRedCards();
                }
                //database.put(fullPlayerName, newPlayer);
            }

            myReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        if (file.exists()){
            return true;
        } else {
            return false;
        }

    }

    /**
     * write database data to a file
     *
     * @see SoccerDB#writeData(File)
     */
    // write data to file
    @Override
    public boolean writeData(File file) {

        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrintWriter pw = new PrintWriter(writer);

        for (SoccerPlayer player: database.values()){
            pw.println(logString(player.getFirstName()));
            pw.println(logString(player.getLastName()));
            pw.println(logString(String.valueOf(player.getUniform())));
            pw.println(logString(player.getTeamName()));
            pw.println(logString(String.valueOf(player.getGoals())));
            pw.println(logString(String.valueOf(player.getYellowCards())));
            pw.println(logString(String.valueOf(player.getRedCards())));
        }

        // closes stream
        pw.close();

        // checks if file is empty
        if (file.length() == 0){
            return false;
        } else {
            return true;
        }

    }

    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see cs301.Soccer.SoccerDB#getTeams()
     */
    // return list of teams
    @Override
    public HashSet<String> getTeams() {

        HashSet<String> teams = new HashSet<String>();

        for (SoccerPlayer player: database.values()){
            // check if team is part of HashSet
            if (teams.contains(player.getTeamName())){
                // skip - team already exists in set
                continue;
            } else {
                teams.add(player.getTeamName());
            }
        }

        return teams;

    }

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
