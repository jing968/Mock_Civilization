package unsw.gloriaromanus.troop;

import java.io.Serializable;

/**
 * Represents a basic unit of soldiers
 * 
 *  
 * current version represents a heavy infantry unit (almost no range, decent armour and morale)
 */
public class Unit implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = -4107196453023989714L;
    private double numTroops; // the number of troops in this unit (should reduce based on depletion)
    private Troop troop;

    public Unit(double num, Troop troop){
        this.numTroops = num;
        this.troop = troop;
    }

    /**
     * Getter method for the number of troop in the current unit
     */
    public double getNumTroops(){
        return numTroops;
    }

    /**
     * Getter method for unit attack range
     * @return return true if unit is ranged unit, false otherwise
     */
    public boolean isRanged(){
        return troop.isRanged();
    }

    /**
     * Getter method of the troop obj
     * @return Troop object in the current unit
     */
    public Troop getTroop(){
        return troop;
    }


    /**
     * Update number of the unit
     * @param num number of casualties 
     */
    public void updateNum( double  num){
        this.numTroops = numTroops - num;
        if(numTroops < 0) numTroops = 0;
    }

    /**
     * Iterates numTroops by 1, if we are adding one troop to the unit.
     */
    public void increaseTroopCount() {
        numTroops++;
    }

    /**
     * Adds numberOfTroops to numTroops.
     */
    public void increaseTroopCount(double numberOfTroops) {
        numTroops += numberOfTroops;
    }

    /**
     * Instead of getting max movement points we get the current one.
     */
    public double getCurrentMovement() {
        return troop.getCurrentMovement();
    }

    /**
     * Subtract movement (points) from current movement points
     * @param movement
     */
    public void subtractMovement(double movement) {
        troop.subtractCurrentMovement(movement);
    } 

    /**
     * Restore movement points of troops after every turn.
     */
    public void restoreMovement() {
        troop.restoreCurrentMovement();
    }

    @Override
    public String toString() {
        return "Unit [numTroops=" + numTroops + ", troop=" + troop.getType() + "]";
    }

    
}
