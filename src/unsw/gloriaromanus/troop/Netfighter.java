package unsw.gloriaromanus.troop;

public class Netfighter extends Infantry{

    /**
     *
     */
    private static final long serialVersionUID = 532478435435709821L;

    public Netfighter(boolean mercenary) {
        super(mercenary);
        setCost(12);
        setTraining(1);
        setHealth(20);
        setArmour(1.2);
        setMorale(3);
        setSpeed(22);
        setAttack(45);
        setShieldDefense(0);
        setChain(2);
        setSlots(1);
    }

    @Override
    public boolean isRanged(){return true;}
    
    @Override
    public String getType() {return "netfighter";}
    
    @Override
    public Netfighter clone(){
        return new Netfighter(false);
    }
}
