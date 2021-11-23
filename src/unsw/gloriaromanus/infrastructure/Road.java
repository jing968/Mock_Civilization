package unsw.gloriaromanus.infrastructure;

public class Road extends Infrastructure {
    /**
     *
     */
    private static final long serialVersionUID = 5354575586862615677L;

    private int movement_points;

    public Road() {
        super(99999.0, 80.0, 2.0, 99999.0, 1, 2);
        // Set movement points to three since, four means no road.
        movement_points = 3;
    }

    /********** Movement Points ************/
    public void setMovementPoints(int movement_points) {
        this.movement_points = movement_points;
    }

    public int getMovementPoints() {
        return movement_points;
    }

    public void reduceMovementPoints() {
        movement_points--;
    }

    /**
     * Override the method from infrastructure so that we don't
     *  change the health and decrease the movement points after
     *  every upgrade.
     */
    @Override
    public void upgradeStats() {
        setGold(-getTowerCost());
        addChain();
        reduceMovementPoints();
        setTowerCost(getTowerCost() * 2);
    }

    @Override
    public String toString() {
        return "" + getChain();
    }
}
