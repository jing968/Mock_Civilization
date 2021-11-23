package unsw.gloriaromanus.infrastructure;

public interface Chain {
    /**
     *  Upgrades building by incrementing chain.
     *      If player doesn't have enough gold or has reach
     *      max chain, print out error message
     */
    public String upgrade();

    /**
     * After upgrade updates the infrastructure stats and
     *  other attributes such as movement points on road.
     */
    public void upgradeStats();

    /**
     * Checks if current chain is less than maximum chain for that building.
     * 
     * @return true if chain can be increased/false otherwise
     */
    public boolean checkChain();

    /**
     * Checks if player has enough gold to upgrade the building
     * 
     * @return true if enough gold/false otherwise.
     */
    public boolean checkEnoughGoldForUpgrade();
}
