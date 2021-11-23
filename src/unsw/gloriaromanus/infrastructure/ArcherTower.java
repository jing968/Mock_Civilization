package unsw.gloriaromanus.infrastructure;

public class ArcherTower extends Wall{
    /**
     *
     */
    private static final long serialVersionUID = -4652428986869966793L;
    private double archer_tower_att_dmg;
    private boolean archer_tower_range;

    public ArcherTower() {
        super();
        super.setTowerHealth(1800.0);
        super.setTowerCost(220.0);
        super.setBuildTime(2.0);
        super.setChain(2);
        archer_tower_att_dmg = 80;
        archer_tower_range = true;
    }

    /********** Archer Tower DMG ************/
    public void setArcherTowerAttDmg(double damage) {
        archer_tower_att_dmg = damage;
    }

    public void setArcherTowerRange(boolean range) {
        archer_tower_range = range;
    }

    /********** Archer Tower Range ************/
    public double getArcherTowerAttDmg() {
        return archer_tower_att_dmg;
    }

    public boolean getArcherTowerRange() {
        return archer_tower_range;
    }

    /**
     * Display the type of wall this wall is.
     */
    @Override
    public String walltype(){
        return "archer";
    }
}
