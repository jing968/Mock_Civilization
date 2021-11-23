package unsw.gloriaromanus.infrastructure;

public class Ballista extends Wall {
    /**
     *
     */
    private static final long serialVersionUID = 4273685456179071240L;
    private double ballista_att_dmg;
    private boolean ballista_range;

    public Ballista() {
        super();
        super.setTowerHealth(2000.0);
        super.setTowerCost(220.0);
        super.setBuildTime(2.0);
        super.setChain(2);
        ballista_att_dmg = 100;
        ballista_range = true;
    }

    /********** Ballista Tower DMG ************/
    public void setBallistaAttDmg(double damage) {
        ballista_att_dmg = damage;
    }

    public double getBallistaAttDmg() {
        return ballista_att_dmg;
    }
    
    /********** Ballista Tower Range ************/
    public boolean getBallistaRange() {
        return ballista_range;
    }

    public void setBallistaRange(boolean range) {
        ballista_range = range;
    }
    
    /**
     * Display the type of wall this wall is.
     */
    @Override
    public String walltype(){
        return "ballista";
    }
}
