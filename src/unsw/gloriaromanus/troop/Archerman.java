package unsw.gloriaromanus.troop;

public class Archerman extends Infantry{

    /**
     *
     */
    private static final long serialVersionUID = 811413424728229838L;

    public Archerman(boolean mercenary) {
        super(mercenary);
        setCost(10);
        setTraining(1);
        setHealth(20);
        setArmour(1.2);
        setMorale(3);
        setSpeed(20);
        setAttack(60);
        setShieldDefense(0);
        setChain(1);
        setSlots(1);
    }
    
    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "archerman";}

    @Override
    public Archerman clone(){
        return new Archerman(false);
    }
    
}
