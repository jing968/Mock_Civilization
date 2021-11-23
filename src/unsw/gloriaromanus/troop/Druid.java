package unsw.gloriaromanus.troop;

public class Druid extends Infantry {

    /**
     *
     */
    private static final long serialVersionUID = -2734443778265694625L;

    public Druid(boolean mercenary) {
        super(mercenary);
        setCost(14);
        setTraining(2);
        setHealth(80);
        setArmour(1.2);
        setMorale(3);
        setSpeed(25);
        setAttack(45);
        setShieldDefense(0);
        setChain(3);
        setSlots(1);
    }

    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "druid";}


    @Override
    public Druid clone(){
        return new Druid(false);
    }
    
}
