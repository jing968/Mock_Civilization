package unsw.gloriaromanus.infrastructure;

import java.io.Serializable;

import unsw.gloriaromanus.Structure;

public class Infrastructure implements Serializable, Chain {
    private static final long serialVersionUID = 6200731143171916567L;
    private Structure structure;
    private double health;
    private double cost;
    private double time;
    private double armour;
    private double finished_time;
    private int chain;
    private int max_chain;

    public Infrastructure(Double health, double cost, double time, Double armour, int chain, int max_chain) {
        this.health = health;
        this.cost = cost;
        this.time = time;
        this.armour = armour;
        this.finished_time = 0;
        this.chain = chain;
        this.max_chain = max_chain;
    }

    /********** Structure ************/
    public void setStructure(Structure structure) {
        this.structure = structure;
    }

    public Structure getStructure() {
        return structure;
    }

    /********** Tower Health ************/
    public void setTowerHealth(double health) {
        this.health = health;
    }

    public double getTowerHealth() {
        return health;
    }

    /********** Tower Cost ************/
    public void setTowerCost(double cost) {
        this.cost = cost;
    }

    public double getTowerCost() {
        return cost;
    }

    /********** Build Time ************/
    public void setBuildTime(double time) {
        this.time = time;
    }

    public double getBuildTime() {
        return time;
    }

    /********** Armour ************/
    public void setArmour(double armour) {
        this.armour = armour;
    }

    public double getArmour() {
        return armour;
    }

    /********** Gold ************/
    public void setGold(double gold) {
        structure.setGold(gold);
    }

    public double getGold() {
        return structure.getGold();
    }

    /********** Finish Build Time ************/
    public void setFinishedBuildTime(int time) {
        finished_time = time + getBuildTime();
    }

    public double getFinishedBuildTime() {
        return finished_time;
    }

    /********** Chain ************/
    public void setChain(int chain) {
        this.chain = chain;
    }

    public int getChain() {
        return chain;
    }

    public void addChain() {
        chain++;
    }

    /********** Max Chain ************/
    public void setMaxChain(int max_chain) {
        this.max_chain = max_chain;
    }

    public int getMaxChain() {
        return max_chain;
    }

    /********** Turns ************/
    public int getTurn() {
        return structure.getTurn();
    }

    @Override
    public String upgrade() {
        if (checkEnoughGoldForUpgrade()) return "Insufficient Gold";
        if (checkChain()) return "Building is already at max upgrade";
        upgradeStats();
        return "Upgrade Successful";
    }

    @Override
    public void upgradeStats() {
        setGold(- getTowerCost());
        addChain();
        setTowerCost(getTowerCost() * 2);
        setTowerHealth(getTowerHealth() * 2);
    }

    @Override
    public boolean checkChain() {
        return getChain() > getMaxChain();
    }

    @Override
    public boolean checkEnoughGoldForUpgrade() {
        return getGold() < getTowerCost();
    }

    /**
     * Get the type of infrastructre it is.
     * @return Simplename of class
     */
    public String getType() {
        return this.getClass().getSimpleName();
    }

}
