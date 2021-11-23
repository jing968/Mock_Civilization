package unsw.gloriaromanus;

import unsw.gloriaromanus.infrastructure.*;
import unsw.gloriaromanus.troop.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class Province implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -2222215240838297766L;
    private Faction faction;
    private String province;  
    private Wealth wealth;
    private Map<String, Unit> units;
    private ArrayList<Province> neighbours;
    private Structure structures;

    public Province(String province) {
        this.province = province;
        units = new HashMap<String, Unit>();
        neighbours = new ArrayList<Province>();
        faction = null;
        wealth = new Wealth(this);
        structures = new Structure(this);
    }

    /********** Turn ************/
    /**
     * Get the current game turn.
     * @return
     */
    public int getTurn() {
        return faction.getTurn();
    }

    /********** Faction ************/
    /**
     * Set faction name
     * @param faction
     */
    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    /**
     * Get the faction allocated to this province.
     * @return
     */
    public Faction getFaction() {
        return faction;
    }

    /********** Province ************/
    /**
     * Get the province name.
     * @return
     */
    public String getProvince() {
        return province;
    }

    /**
     * Cinoare province name given another province.
     * @param p
     * @return
     */
    public boolean compareProvinceName(Province p) {
        return p.getProvince().equals(getProvince());
    }

    /********** Wealth ************/
    /**
     * @return province's wealth
     */
    public double getWealth() {
        return wealth.getWealth();
    }

    /**
     * Wealth generated from province is added to faction
     * that owns it.
     * @param wealth
     */
    public void addWealthToFaction(double wealth) {
        faction.addWealth(wealth);
    }

    /**
     * Reduces all units morale in the province by 1.
     */
    public void reduceMorale() {
        for (Map.Entry<String, Unit> unit : units.entrySet()) {
            Unit u = unit.getValue();
            Troop t = u.getTroop();
            t.setMorale(t.getMorale() - 1);
        }
    }

    /********** Tax Rate ************/
    /**
     * Set province's tax rate
     * @param taxRate
     */
    public String setTaxRate(String taxRate) {
        return wealth.setTaxRate(taxRate);
    }


    
    public String getTaxRate() {
        return wealth.getTaxRate();
    }



    /********** Gold ************/
    /**
     * Update the gold in allocated faction
     * @param gold
     */
    public void setGold(double gold) {
        faction.setGold(gold);
    }


    /**
     * Get the allocated faction gold count.
     * @return
     */
    public double getGold() {
        return faction.getGold();
    }


    /********** Structures ************/
    public Structure getStructures() {
        return structures;
    }



    /**
     * Set infrastructure within structures.
     */
    public void setInfrastructure() {
        structures.setInfrastructure();
    }


    /********** Farm ************/
    public void setFarm(Farm farm) {
        structures.setFarm(farm);
    }



    /********** Troop Production ************/
    public void setTroopProduction(TroopProduction troopProduction) {
        structures.setTroopProduction(troopProduction);
    }


    /**
     * Get troopProduction from structures.
     * @return
     */
    public TroopProduction getTroopProduction() {
        return structures.getTroopProduction();
    }


    /********** Wall ************/
    public void setWall(Wall wall) {
        structures.setWall(wall);
    }


    /**
     * Check if a province has a wall
     * @return return true if wall exist, false otherwise
     */
    public boolean haveWall() {
        return structures.haveWall();
    }



    /**
     * Getter method for type of the wall
     * @return return wall if not upgraded, return ballista if upgraded to ballista tower, return archer if upgraded to archer tower
     */
    public String walltype(){
        String s = "none";
        if (haveWall()) s = getWallType();
        return s;
    }

    public String getWallType() {
        return structures.getWall().walltype();
    }


    /**
     * Retrive the building queue from structures.
     * @return
     */
    public ArrayList<Infrastructure> getQueue() {
        return structures.getQueue();
    }



    /**
     * Return a Ballistatower or Archertower object
     */
    public Troop getDefendtower(){
        if(walltype().equals("archer")) {
            Ballistatower btower = new Ballistatower();
            return btower;
        }
        else{
            Archertower atower = new Archertower();
            return atower;
        }
    }


    /********** Road ************/
    public void setRoad(Road road) {
        structures.setRoad(road);
    }



    /**
     * Get road's movement points.
     * @return
     */
    public int getMovementPoints() {
        return structures.getMovementPoints();
    }


    /********** Neighbour ************/
    /**
     * Adds neighbours to the province so its connected.
     * @param province
     */
    public void addNeighbour(Province province) {
        neighbours.add(province);
    }



    /**
     * Get surrounding neighbour provinces.
     * @return
     */
    public ArrayList<Province> getNeighbours() {
        ArrayList<Province> n = new ArrayList<Province>();
        for (Province neighbour : neighbours) {
            n.add(neighbour);
        }
        return n;
    }



    /**
     * Find neighbour if they exist.
     * 
     * @param p
     * @return
     */
    public Province findNeighbour(String p) {
        for (Province n : neighbours) {
            System.out.println(n.getProvince());
            if (p.equals(n.getProvince())) return n;
        }
        return null;
    }



    /********** Unit ************/
    /**
     * Get units within the province.
     * @return
     */
    public Map<String, Unit> getUnits() {
        Map<String, Unit> u = new HashMap<String, Unit>();
        for (Map.Entry<String, Unit> unit : units.entrySet()) {
            u.put(unit.getKey(), unit.getValue());
        }
        return u;
    }



    /**
     * Convert units into a arraylist.
     * @return
     */
    public ArrayList<Unit> getUnitsList() {
        ArrayList<Unit> u = new ArrayList<Unit>();
        for (Map.Entry<String, Unit> unit : units.entrySet()) {
            u.add(unit.getValue());
        }
        return u;
    }



    /**
     * Get unit via unitName
     * @param unitName
     * @return  Unit of unitname.
     */
    public Unit getUnit(String unitName) {
        if (units.containsKey(unitName))
            return units.get(unitName);
        return null;
    }



    /**
     * Get new Map<String, Unit> via a list of names
     * @param names
     * @return  Set of units.
     */
    public Map<String, Unit> getUnitMap(String names) {
        Map<String, Unit> u = new HashMap<String, Unit>();
        String[] unitIndex = names.split(", ");
        for (String s : unitIndex) {
            Unit unit = getUnit(s);
            if (unit != null) u.put(s, unit);
        }
        return u;
    }



    /**
     * Restore movement points for all units at the end of the turn.
     */
    public void restoreMovementPoints() {
        for (Map.Entry<String, Unit> unit : units.entrySet()) {
            Unit u = unit.getValue();
            u.restoreMovement();
        }
    }



    /********** Train Unit ************/
    /**
     * Train unit in troopProduction given troop object
     * @param troop
     */
    public String trainUnit(String troop) {
        if (getTroopProduction() != null) return getTroopProduction().trainUnit(troop);
        return "Troop Production Doesn't exist";
    }



    /**
     * If troops provided from troopProduction queue is not null
     *  We can attempt to store each troop unit inside Units.
     *  After than we can remove it from the queue within troopProduction.
     * @param troops
     */
    public void addTroopList(ArrayList<Troop> troops) {
        if (! troops.isEmpty()) {
            for (Troop t : troops) {
                addUnit(t);
                structures.getTroopProduction().removeTroop(t);
            }
        }
    }



     /**
     * After producing a unit check if it exists within units.
     *  If so iterate troop count by 1.
     *  Otherwise, do nothing.
     * @param troop
     */
    public void addUnit(Troop troop) {
        if (units.containsKey(troop.getType())) {
            Unit u = units.get(troop.getType());
            u.increaseTroopCount();
        } else {
            units.put(troop.getType(), new Unit(1, troop));
        }
    }

    /********** Commands ************/



    /********** Movement ************/
    /**
     * Move units from src to dest if conditions are met.
     * @param dest      Destination province.
     * @param units     Selection of units to move.
     */
    public String moveUnits(Province dest, Map<String, Unit> units) {
        Movement movement = new Movement(this, dest, units);
        return movement.moveUnit();
    }
    



    /**
     * Does the same thing as the method above, but grabs the number of troops
     *  that exists within the unit.
     * Also change the current movement values for these troops.
     * There will be a bug, where if we combine units then that was originally in the province
     * Will lose their movement points without moving  at all.
     * 
     */
    public void combineUnits(Map<String, Unit> transferUnits, double movement) {
        for (Map.Entry<String, Unit> unit : transferUnits.entrySet()) {
            String unitName = unit.getKey();
            Unit tUnits = unit.getValue();
            if (units.containsKey(unitName)) {
                Unit cUnits = units.get(unitName);
                cUnits.increaseTroopCount(tUnits.getNumTroops());
                cUnits.subtractMovement(movement);
            } else {
                tUnits.subtractMovement(movement);
                units.put(unitName, tUnits);
            }
        }
    }



    /**
     * Remove a set amount of units from units.
     * @param removeUnits   Collection of units to remove
     */
    public void clearUnits(Map<String, Unit> removeUnits) {
        for (Map.Entry<String, Unit> u : removeUnits.entrySet()) {
            units.remove(u.getKey());
        }
    }



    /********** Build Infrastructure ************/
    /**
     * Attempt to build infrastructure given infrastructure object.
     * @param infrastructure
     */
    public String buildInfrastructure(String infrastructure) {
        return structures.buildInfrastructure(infrastructure);
    }



    /********** Upgrade Infrastructure ************/
    /**
     * Attempt to upgrade infrastructure within structures.
     * @param infrastructure
     */
    public String upgradeBuilding(String infrastructure) {
        return structures.upgradeBuilding(infrastructure);
    }



    /********** Fight Province ************/
    /**
     * When there is battle there will be a winner.
     *  So if attacker wins then they take the provionce
     *  If defender wins they take the attack's province.
     * Province must be next to eachother.
     * @param p 
     */
    public String fightProvince(String p) {
        Province enemy = findNeighbour(p);
        if (enemy == null) return "Province is not a neighbour";
        Battle battle = new Battle(this, enemy);
        return battle.start();
    }



    /**
     * When an engagement finishes, if any unit is destoryed, remove the unit from the army list
     */
    public void updateArmy(){
        String name = "";
        Unit u = null;
        for (Map.Entry<String, Unit> unit : units.entrySet()) {
            name = unit.getKey();
            u = unit.getValue();
        }
        if((!name.equals("")) && u != null){
            if (u.getNumTroops() <= 0 ){
                units.remove(name);
            }
        }
    }

    /**
     * Checks troopProduction queue.
     * Checks infrastructure queue
     * Generate wealth/gold for province and faction.
     * Restore Movement points for all units
     */
    public void update() {
        if (getTroopProduction() != null) addTroopList(getTroopProduction().popQueue());
        setInfrastructure();
        wealth.generateWealth();
        restoreMovementPoints();
    }

    @Override
    public String toString() {
        return "Province: " + getProvince() + "\n";
    }

    public String toStringUnits() {
        String s = "";
        for (Map.Entry<String, Unit> unit : units.entrySet()) {
            s += unit.getKey() + ": " + unit.getValue().getNumTroops() + "\n";
        }
        return s;
    }
}
