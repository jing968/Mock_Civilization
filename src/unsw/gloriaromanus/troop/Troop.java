package unsw.gloriaromanus.troop;

import java.io.Serializable;

public abstract class Troop implements Serializable{
    /**
     *
     */
    private static final long serialVersionUID = 7580292968139132917L;
    private double cost;
    private double training;
    private double movement;            // Max movement
    private double current_movement;    // Current movement points left
    private double health;
    private double armour;  // armour defense
    private double morale;  // resistance to fleeing
    private double speed;  // ability to disengage from disadvantageous battle
    private double attack;  // can be either missile or melee attack to simplify. Could improve implementation by differentiating!
    private double shieldDefense; // a shield
    private double chain;
    private double slots;

    
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getTraining() {
        return training;
    }

    public void setTraining(double training) {
        this.training = training;
    }

    public double getMovement() {
        return movement;
    }

    public void setMovement(double movement) {
        this.movement = movement;
        setCurrentMovement(movement);
    }

    public double getCurrentMovement() {
        return current_movement;
    }

    public void setCurrentMovement(double movement) {
        current_movement = movement;
    }

    public void subtractCurrentMovement(double movement) {
        current_movement -= movement;
    }

    public void restoreCurrentMovement() {
        current_movement = movement;
    }

    public double getHealh(){
        return health;
    }

    public void setHealth(double hp){
        this.health = hp;
    }

    public double getArmour() {
        return armour;
    }

    public void setArmour(double armour) {
        this.armour = armour;
    }

    public double getMorale() {
        return morale;
    }

    public void setMorale(double morale) {
        this.morale = morale;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getAttack() {
        return attack;
    }

    public void setAttack(double attack) {
        this.attack = attack;
    }

    public double getShieldDefense() {
        return shieldDefense;
    }

    public void setShieldDefense(double shieldDefense) {
        this.shieldDefense = shieldDefense;
    }
    
    public double getChain(){
        return chain;
    }

    public void setChain(double chain){
        this.chain = chain;
    }

    public double getSlots() {
        return slots;
    }

    public void setSlots(double slots){
        this.slots = slots;
    }

    /**
     * Return a new troop object that is of the same type of current troop
     */
    public Troop clone(){ return null;}

    /**
     * Check if a troop is melee or ranged
     * @return return true if ranged, false otherwise;
     */
    public boolean isRanged() {return false;}

    /**
     * Getter method of the type of troop
     * @return return troop type as a string
     */
    public String getType() {return null;}


    /**
     * Increase troop's morale if its impacted by druid's special abilities
     * @param stack number of stack the troop is impacted by
     */
    public void buff_druidic_fevour (double stack){
        double buff = 1 + 0.1 * stack; 
        setMorale(morale * buff);

    }

    /**
     * Reduce troop's morale if its impacted by druid's special abilities
     * @param stack number of stack the troop is impacted by
     */
    public void debuff_druidic_fevour (double stack){
        double debuff = 1 - stack * 0.05;
        setMorale(morale * debuff);
    }
    
}
