package unsw.gloriaromanus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

import unsw.gloriaromanus.troop.*;

public class Faction implements Serializable ,ObserverTurns {
    /**
     *
     */
    private static final long serialVersionUID = 8031217501907202630L;
    private GameState gameState;
    private ArrayList<Province> provinces;
    private double wealth;
    private String faction;
    private double gold;
    private int turn;

    public Faction(String faction) {
        this.faction = faction;
        this.gold = 2000;
        wealth = 0;
        turn = 1;
        provinces = new ArrayList<Province>();
    }

    /********** GameState ************/
    public void setGameState(GameState state) {
        gameState = state;
    }

    public SubjectTurns getGameState() {
        return gameState;
    }

    /********** Provinces ************/
    public void addProvince(Province province) {
        provinces.add(province);
        province.setFaction(this);
    }

    public void removeProvince(Province province) {
        provinces.remove(province);
    }

    /**
     * @return list of own province
     */
    public ArrayList<Province> getProvinces() {
        ArrayList<Province> p = new ArrayList<Province>();
        for (Province province : provinces) {
            p.add(province);
        }
        return p;
    }

    /**
     * Get province inside this faction given provide province name.
     * @param province
     * @return  province/ nothing.
     */
    public Province getProvince(String province) {
        for (Province p : provinces) {
            if (p.getProvince().equals(province)) return p;
        }
        return null;
    }

    public Integer getNumProvince(){
        return provinces.size();
    }
    
    /********** Wealth ************/
    public void setWealth(int wealth) {
        this.wealth = wealth;
    }

    public double getWealth() {
        return wealth;
    }

    public void addWealth(double wealth) {
        this.wealth += wealth;
    }

    /********** Faction ************/
    public void setFaction(String faction) {
        this.faction = faction; 
    }

    public String getFaction() {
        return faction;
    }

    /**
     * Compare faction name given another faction
     */
    public boolean compareFactionName(Faction f) {
        return f.getFaction().equals(getFaction());
    }

    /********** Gold ************/
    /**
     * Add the gold and prevent it from gold past 0
     * @param gold
     */
    public void setGold(double gold) {
        if ((getGold() + gold) < 0) this.gold = 0;
        else this.gold = getGold() + gold;
    }

    public double getGold()  {
        return gold;
    }

    /********** Turn ************/
    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getTurn() {
        return turn;
    }
    

    /********** Tax Rate ************/
    /**
     * Select a province and then set its taxRate.
     * @param province
     * @param taxRate
     */
    public String setTaxRate(String province, String taxRate) {
        Province p = getProvince(province);
        if (p == null) return "Invalid Proivnce";
        
        return p.setTaxRate(taxRate);
    }


    /********** Train Unit ************/
    /**
     * Choose which province faction will choose to train troop.
     * 
     * @param province
     * @param troop
     */
    public String trainUnit(String province, String troop) {
        Province p = getProvince(province);
        if (p == null) return "Province Does not exist";
        
        return p.trainUnit(troop);
    }


    /********** Infrastructure ************/
    /**
     * Select the province to train the building
     * @param province
     * @param building
     */
    public String buildInfrastructure(String province, String infrastructure) {
        Province p = getProvince(province);
        if (p == null) return "You don't own this province";
        
        return p.buildInfrastructure(infrastructure);
    }


    /**
     * Select province to upgrade the building
     * @param province
     * @param building
     */
    public String upgradeInfrastructure(String province, String building) {
        Province p = getProvince(province);
        if (p == null) return "You don't own this province";
        
        return p.upgradeBuilding(building);
    }



    /********** Attack/Move ************/
    /**
     * Make province a attack province b.
     * @param a     province a (attacker)
     * @param b     province b (defender)
     */
    public String fightProvince(String a, String b) {
        Province pro_a = getProvince(a);
        if (pro_a == null) return "You do not own this province";
        return pro_a.fightProvince(b);
    }



    /**
     * Move units from src to dest.
     * @param a         src
     * @param b         dest
     * @param chosenUnits   selection of units
     */
    public String moveTroop(String a, String b, String chosenUnits) {
        Province src = getProvince(a);
        Province dest = getProvince(b);
        if (src == null || dest == null) return "You don't the province/s";
        Map<String, Unit> units = src.getUnitMap(chosenUnits);
        if (units.isEmpty()) return "Units do not exist!";
        return src.moveUnits(dest, units);
    }


    @Override
    public void update(SubjectTurns obj) {
        setTurn(obj.getTurns());
        for (Province p : provinces) {
            p.update();
        }
    }


    @Override
    public String toString() {
        String s = "";
        for (Province province : provinces) {
            s += province.toString();
        }
        return s;        
    }

    /**
     * Check if a faction completes sets of win conditions
     * @return return true if the faction completes win conditions, false otherwised
     */
    public Boolean reachedConditions(){
        WinCondition conds = gameState.getCond();
        Boolean wealth_c = false;
        Boolean treasure_c = false;
        Boolean conquer_c = false;
        if(gold >= 100000) treasure_c = true;
        if(wealth >= 400000) wealth_c = true;
        if(gameState.ifConquerAll(this) == true) conquer_c =true;
        for(ArrayList<String> cond: conds.getConditions()){
            Boolean complete = true;
            for(String goal : cond){
                if(goal.equals("treasure")){
                    if(!treasure_c) complete = false;
                }else if(goal.equals("conquest")){
                    if(!conquer_c) complete = false;
                }else{
                    if(!wealth_c) complete = false;
                }
            }
            if(complete) return true;
        }
        return false;
    }
}
