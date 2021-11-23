package unsw.gloriaromanus;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;
// import org.junit.platform.console.shadow.picocli.CommandLine.ExitCode;

import unsw.gloriaromanus.troop.*;

import java.util.ArrayList;
import java.util.Iterator;

public class GameState implements SubjectTurns, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 5574369157342160836L;
    private ArrayList<ObserverTurns> factions;
    private ArrayList<Province> provinces;
    private boolean status;
    private int turn;
    private WinCondition cond;
    private int faction_iterator;

    public GameState() {
        factions = new ArrayList<ObserverTurns>();
        provinces = new ArrayList<Province>();
        turn = 1;
        faction_iterator = 0;
        status = false;
        cond = null;
    }

    /********** Status ************/
    public void changeStatus() {
        if (status) status = false;
        status = true;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public boolean getGameStatus() {
        return status;
    }

    /********** Turn ************/
    public void setTurns(int turn) {
        this.turn = turn;
    }

    /********** Iterator ************/
    public void setIterator(int iterator) {
        this.faction_iterator = iterator;
    }

    public int getIterator() {
        return faction_iterator;
    }

    /**
     * Increments iterator by 1.
     *  This is used to get the faction() in getFactionObj() for frontend.
     */
    public void addIterator() {
        System.out.println(getFactions().size());
        faction_iterator++;
        if (faction_iterator > getFactions().size() - 1) {
            faction_iterator = 0;
            iterateTurns();
        }
    }

    /********** Cond ************/
    public void setCond(WinCondition cond) {
        this.cond = cond;
    }

    public WinCondition getCond() {
        return cond;
    }

    public void buildCondition() {
        WinCondition w = new WinCondition();
        w.generateConds();
        setCond(w);
    }

    /********** Factions ************/
    public void addFaction(ObserverTurns faction) {
        factions.add(faction);
    }

    public ArrayList<Faction> getFactions() {
        ArrayList<Faction> f = new ArrayList<Faction>();
        for (ObserverTurns faction : factions) {
            f.add((Faction) faction);
        }
        return f;
    }

    public Faction getFactionObj() {
        return (Faction) factions.get(faction_iterator);
    }

    /********** Provinces ************/
    public void addProvince(Province province) {
        provinces.add(province);
    }

    public ArrayList<Province> getProvinces() {
        ArrayList<Province> p = new ArrayList<Province>();
        for (Province province : provinces) {
            p.add(province);
        }
        return p;
    }

    /**
     * Gets province in name of province (key) is parsed in.
     * 
     * @param p
     * @return Province of the stirng/ null otherwise.
     */
    public Province getProvince(String key) {
        Province p = null;
        for (Province province : provinces) {
            if (key.equals(province.getProvince())) {
                p = province;
            }
        }
        return p;
    }


    /********** SubjectTurns ************/
    @Override
    public void attach(ObserverTurns o) {
        factions.add(o);
    }

    @Override
    public void iterateTurns() {
        int new_turn = getTurns() + 1;
        setTurns(new_turn);
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (ObserverTurns o : factions) {
            o.update(this);
        }
    }

    @Override
    public int getTurns() {
        return turn;
    }


    /**
     * Go through JSONObject and get contents, factions (keys) & province (values)
     * 
     * @param file
     * @throws Exception
     */
    public void readOwnership(String file) throws Exception {
        JSONObject obj = convertFileToJSONObject(file);
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Faction new_faction = new Faction(key);
            addFaction(new_faction);
            new_faction.setGameState(this);

            JSONArray jArr = obj.getJSONArray(key);
            for (int i = 0; i < jArr.length(); i++) {
                String p = jArr.getString(i);
                storeProvince(new_faction, p);
            }
        }
    }
    
    /**
     * Create a JSONObject given contents within initial_province_ownership.json
     * 
     * @return JSONObject
     * @throws Exception
     */
    public JSONObject convertFileToJSONObject(String file) throws Exception {
        String json = readFileAsString(file);
        return new JSONObject(json);
    }

    /**
     * Converts file path content into a huge string
     * 
     * @param file
     * @return
     * @throws Exception
     */
    public String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    /**
     * Goes through province_adjacency_matrix_fully_connected.json and connects Each
     * province to each other.
     * 
     * @param file file containing neighbours of provinces.
     * @throws Exception
     */
    public void readConnected(String file) throws Exception {
        JSONObject obj = convertFileToJSONObject(file);
        Iterator<String> keys = obj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Province province = getProvince(key);
            JSONObject jObj = obj.getJSONObject(key);
            storeNeighbours(province, jObj);
        }
    }

    /**
     * Look through the province's neighbours, store inside province.getNeighbour()
     * if true.
     * 
     * @param province
     * @param jArr
     */
    public void storeNeighbours(Province province, JSONObject jObj) {
        Iterator<String> keys = jObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            boolean value = jObj.getBoolean(key);
            if (value) {
                Province p = getProvince(key);
                province.addNeighbour(p);
            }
        }
    }

    /**
     * Store provinces within faction and provinces via jArr
     * 
     * @param jArr
     * @param faction
     */
    public void storeProvince(Faction faction, String p) {
        Province province = new Province(p);
        addProvince(province);
        faction.addProvince(province);
    }

    /**
     * Save the game progress
     * 
     * @throws IOException
     */
    public void save() throws IOException {
        FileOutputStream fileOut = new FileOutputStream("src\\unsw\\gloriaromanus\\savelog.txt");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(this);
        out.close();
    }

    /**
     * Initialise a new game with json files 
     * @throws Exception
     */
    public void newgame() throws Exception {
        readOwnership("src\\unsw\\gloriaromanus\\initial_province_ownership.json");
        readConnected("src\\unsw\\gloriaromanus\\province_adjacency_matrix_fully_connected.json");
        WinCondition cond = new WinCondition();
        cond.generateConds();
        setCond(cond);   
    }
    
    /** 
     * Load the game progress
     */
    public GameState load()throws FileNotFoundException, IOException, ClassNotFoundException {
        GameState game = null;

        FileInputStream fileIn = new FileInputStream("src\\unsw\\gloriaromanus\\savelog.txt");
        ObjectInputStream in = new ObjectInputStream(fileIn);
    
        game = (GameState) in.readObject();
        
        in.close();
        fileIn.close();
        return game;

    }

    /**
     * Given a faction as input check if that faction conquer all the possible province on the play ground
     * @return return true if the faction conquered all provicne, false otherwised
     */
    public Boolean ifConquerAll(Faction faction){
        Integer current = faction.getNumProvince();
        if(provinces == null) return false;
        Integer total = provinces.size();
        if(current == total) return true;
        return false;
    }

}

