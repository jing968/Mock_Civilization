package unsw.gloriaromanus.infrastructure;

import unsw.gloriaromanus.TroopStat;
import unsw.gloriaromanus.troop.*;
import java.util.ArrayList;

public class TroopProduction extends Infrastructure {
    /**
     *
     */
    private static final long serialVersionUID = 5276266848247276704L;

    private int maxSlots; // indicating current max, not total max.
    private int nSlots;   // indicating current used
    private ArrayList<Troop> queue;
    TroopStat stat;

    public TroopProduction() {
        super(1000.0, 120.0, 4, 1.0, 1, 5); 
        queue = new ArrayList<Troop>();
        maxSlots = 1;  // Max troop you can build is set to 1 initially.
        nSlots = 0; 
        stat = new TroopStat();
    }

    /********** Max Slots ************/
    public void setMaxSlots(int maxSlots) {
        this.maxSlots = maxSlots;
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    /********** nSlots ************/
    public void setNSlots(int nSlots) {
        this.nSlots = nSlots;
    }

    public int getNSlots() {
        return nSlots;
    }

    public void addNSlots(int slots)  {
        nSlots += slots;
    }

    public void subtractNSlots(int slots) {
        nSlots -= slots;
    }

    /********** Remaining Slots ************/
    public int getRemainingSlots() {
        return maxSlots - nSlots;
    }
    
    /********** Queue ************/
    /**
     * Insert it to a queue, not really a queue, as it just
     *  appends the object to the end of the list
     * @param troop
     */
    public void addToQueue(Troop troop) {
        queue.add(troop);
    }

    /**
     * Gets the queue 
     * @return  new list of the queue
     */
    public ArrayList<Troop> getQueue() {
        ArrayList<Troop> q = new ArrayList<Troop>();
        for (Troop t : queue) q.add(t);
        return q;
    }

    /**
     * Retrieve troop/s thats meant to pop out this turn.
     * @return list of troop ready at this turn
     */
    public ArrayList<Troop> popQueue() {
        ArrayList<Troop> q = new ArrayList<Troop>();
        for (int i = 0; i < queue.size(); i++) {
            Troop t = queue.get(i);
            if (t.getTraining() == getTurn()) q.add(t);
        }
        return q;
    }

    /**
     * Remove troop from queue and free up slots.
     * @param troop
     */
    public void removeTroop(Troop troop) {
        queue.remove(troop);
        subtractNSlots((int) troop.getSlots());
    }
    
    /********** Train Unit ************/
    /**
     * Attempt to train the unit
     *  Checks if it has enough gold, 
     *  has the required chain and if we have the slots
     *  available for training, if it does go to updateState
     *  and update these variables since the troop is viable for 
     *  training
     * @param troop
     * @return
     */
    public String trainUnit(String troop) {
        Troop t = stat.generateTroop(troop);
        if (t == null) return "Troop Doesn't exist!";
        if (t.getCost() > getGold()) return "Not enough gold!";
        if (t.getChain() > getChain()) return "Need to upgrade building!";
        if (t.getSlots() > getRemainingSlots()) return "Need more slots!";
        updateState(t);
        return troop + " now training";
    }

    /**
     * Updates glod, nSlots and updates training time for unit
     * @param t
     */
    public void updateState(Troop t) {
        setGold(- t.getCost());
        addNSlots((int) t.getSlots());
        t.setTraining(t.getTraining() + getTurn());
        addToQueue(t);
    }

    /**
     * Given a farm update stats accordingly.
     *  Increases max slot by 1, for every upgrade on the farm.
     * @param farm
     */
    public void updateTroopProduction(Farm farm) {
        if (farm == null) return;
        setMaxSlots(farm.getChain() + 1);
    }


    @Override
    public String toString() {
        return getChain() + " MaxSlots: " + getMaxSlots() + ", Remaining: " + getRemainingSlots();
    }

    /**
     * Converts the queue to a more readable format for front end.
     * @return
     */
    public String queueToString() {
        String s = "";
        for (Troop t : queue) {
            s += t.getType() + " finishes training at turn: " + t.getTraining() + "\n"; 
        }
        return s;
    }
}

