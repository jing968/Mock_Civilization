
package unsw.gloriaromanus;

import unsw.gloriaromanus.infrastructure.*;

import java.io.Serializable;
import java.util.ArrayList;

public class Structure implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -7797417957583097952L;
    private Province province;
    private TroopProduction troopProduction;
    private Road road;
    private Wall wall;
    private Farm farm;
    private ArrayList<Infrastructure> queue;
    InfrastructureStat stat;

    public Structure(Province province) {
        this.province = province;
        troopProduction = null;
        road = null;
        wall = null;
        farm = null;
        stat = new InfrastructureStat();
        queue = new ArrayList<Infrastructure>();
    }

    /********** Troop Production ************/
    public void setTroopProduction(TroopProduction troopProduction) {
        this.troopProduction = troopProduction;
        troopProduction.setStructure(this);
        updateTroopProduction(farm);
    }

    public TroopProduction getTroopProduction() {
        return troopProduction;
    }

    /**
     * Given farm update the troopProduction attributes.
     * @param farm
     */
    public void updateTroopProduction(Farm farm) {
        if (troopProduction != null) {
            troopProduction.updateTroopProduction(farm);
        }
    }

    /**
     * Getter method for chain level for troop production
     * @return return chain level as int
     */
    public int getProductionchain(){
        if(troopProduction == null) return 0;
        return troopProduction.getChain();
    }

    /********** Farm ************/
    public void setFarm(Farm farm) {
        this.farm = farm;
        farm.setStructure(this);
        updateTroopProduction(farm);
    }

    public Farm getFarm() {
        return farm;
    }

    /**
     * Getter method for chain level for farm
     * @return return chain level as int
     */
    public int getFarmChain(){
        if(farm == null) return 0;
        return farm.getChain();
    }

    /********** Wall ************/
    public void setWall(Wall wall) {
        this.wall = wall;
        wall.setStructure(this);
    }

    public Wall getWall() {
        return wall;
    }

    /**
     * Check if a province has a wall
     * @return return true if wall exist, false otherwise
     */
    public boolean haveWall() {
        if (getWall() != null) return true;
        return false;
    }

    /********** Road ************/
    public void setRoad(Road road) {
        this.road = road;
        road.setStructure(this);
    }

    public Road getRoad() {
        return road;
    }

    /**
     * Getter method for chain level for road
     * @return return chain level as int
     */
    public int getRoadChain(){
        if(road == null) return 0;
        return road.getChain();
    }

    /**
     * Get road's movement points
     * @return
     */
    public int getMovementPoints() {
        if (getRoad() == null) return 4;
        return road.getMovementPoints();
    }

    /********** Gold ************/
    public void setGold(double gold) {
        province.setGold(gold);
    }

    public double getGold() {
        return province.getGold();
    }

    /********** Turns ************/
    public int getTurn() {
        return province.getTurn();
    }

    /********** Queue ************/
    /**
     * Add infrastructure to the queue.
     * @param infrastructure
     */
    public void addQueue(Infrastructure infrastructure) {
        queue.add(infrastructure);
    }

    public void removeQueue(Infrastructure i) {
        queue.remove(i);
    }

    /**
     * Returns a ArrayList<Infrastructure> for all the buildings that is finished this turn
     * @return
     */
    public ArrayList<Infrastructure> popQueue() {
        ArrayList<Infrastructure> i = new ArrayList<Infrastructure>();
        for (Infrastructure q : queue) {
            if (q.getFinishedBuildTime() == getTurn()) {
                i.add(q);
            } 
        }
        return i;
    }

    /**
     * Retrieve the queue for building queue.
     * @return
     */
    public ArrayList<Infrastructure> getQueue() {
        ArrayList<Infrastructure> qq = new ArrayList<Infrastructure>();
        for (Infrastructure q : queue) {
            qq.add(q);
        }
        return qq;
    }
    
    /********** Build Infrastructure ************/
    /**
     * Attempt to build infrastructure and add it to the queue
     * @param i
     */
    public String buildInfrastructure(String i) {
        Infrastructure infrastructure = stat.generateInfrastructure(i);
        if (infrastructure == null) return "Structure Does Not Exist!";
        if (infrastructure.getTowerCost() > getGold()) return "Not Enough Gold!";
        String s = inQueue(infrastructure);
        if (!s.equals("false")) return s;
        if (checkExists(infrastructure) != null) return "Building already exists!";

        setGold(- infrastructure.getTowerCost());
        infrastructure.setFinishedBuildTime(getTurn());
        addQueue(infrastructure);
        return "Now Building!";
    }

    /**
     * Checks if infrastructure is within the queue
     * @param i  Infrastructure
     * @return   String that displays type of infrastructure and when it is complete, false otherwise
     */
    public String inQueue(Infrastructure i) {
        String s = i.getType();
        for (Infrastructure inf : queue) {
            if (inf.getType().equals(s)) return s + " finishes at turn: " + inf.getFinishedBuildTime();
        }
        return "false";
    }

    /**
     * Determines if the infrastructure attempting to build has already been constructed.
     * @param i
     * @return  Object of the infrastructure, null otherwise
     */
    public Infrastructure checkExists(Infrastructure i) {
        Infrastructure inf = null;
        switch (i.getType()) {
            case "TroopProduction": inf =  getTroopProduction();
            case "Farm": inf = getFarm();
            case "Wall": inf = getWall();
            case "Road": inf = getRoad();
        }
        return inf;
    }

    /********** Upgrade Infrastructure ************/
    /**
     * Upgrading Buildings will be upgraded immediately no queue time
     *  Since its not apart of the spec.
     */
    public String upgradeBuilding(String infrastructure) {
        switch (infrastructure) {
            case "troopProduction": return upgradeTroopProduction();
            case "farm": return upgradeFarm();
            case "road": return upgradeRoad();
            case "archertower":
                if (upgradeWall().equals("Upgrade Successful")) {
                    wall = new ArcherTower();
                    wall.setStructure(this);
                    return "Upgrade Successful";
                } return "Upgrade Unsuccessful";
            case "ballistatower":
            if (upgradeWall().equals("Upgrade Successful")) {
                wall = new Ballista();
                wall.setStructure(this);
                return "Upgrade Successful";
            } return "Upgrade Unsuccessful";
        }
        return "Structure Does Not exist!";
    }

    /**
     * Upgrade troopProduction if it exists.
     * @return string containing result
     */
    public String upgradeTroopProduction() {
        if (getTroopProduction() == null) return "TroopProduction doesn't exist";
        return troopProduction.upgrade(); 
    }

    /**
     * Upgrade farm if it exists.
     * @return string containing result
     */
    public String upgradeFarm() {
        if (getFarm() == null) return "Farm doesn't exist";
        String s = farm.upgrade();
        if (s.equals("Upgrade Successful")) updateTroopProduction(farm);
        return s;
    }

    /**
     * Upgrade wall if it exists.
     * @return string containing result
     */
    public String upgradeWall() {
        if (getWall() == null) return "Wall doesn't exist";
        return wall.upgrade();
    }

    /**
     * Upgrade road if it exists.
     * @return string containing result
     */
    public String upgradeRoad() {
        if (getRoad() == null) return "Road doesn't exist";
        return road.upgrade();
    }

    /**
     * Pop queue and set infrastructure accordingly.
     *  Once it has been set remove from the queue
     */
    public void setInfrastructure() {
        ArrayList<Infrastructure> buildings = popQueue();
        for (Infrastructure i : buildings) {
            switch(i.getType()) {
                case "TroopProduction": setTroopProduction((TroopProduction) i); break;
                case "Farm": setFarm((Farm) i); break;
                case "Wall": setWall((Wall) i); break;
                case "Road": setRoad((Road) i); break;
            }
            removeQueue(i);
        }
    }

    /********** toString ************/
    /**
     * @return calls toString method within this object, if object
     * doesn't exist return "null"
     */
    public String farmToString() {
        if (getFarm() == null) return "null";
        return farm.toString();
    }

    /**
     * @return calls toString method within this object, if object
     * doesn't exist return "null"
     */
    public String troopToString() {
        if (getTroopProduction() == null) return "null";
        return troopProduction.toString();
    }

    /**
     * @return calls toString method within this object, if object
     * doesn't exist return "null"
     */
    public String roadToString() {
        if (getRoad() == null) return "null";
        return road.toString();
    }

    /**
     * @return calls toString method within this object, if object
     * doesn't exist return "null"
     */
    public String wallToString() {
        if (getWall() == null) return "null";
        return wall.walltype();
    }

    /**
     * @return a string containing all relevant details about the infrastructure that
     *  is in the queue, type and finish building time.
     */
    public String queueToString() {
        String s = "";
        for (Infrastructure i : queue) {
            s += i.getType() + ": " + i.getFinishedBuildTime() + "\n";
        }
        return s;
    }
}
