package unsw.gloriaromanus.troop;

public class Crossbowman extends Infantry{

    /**
     *
     */
    private static final long serialVersionUID = -4588586660410441074L;

    public Crossbowman(boolean mercenary) {
        super(mercenary);
        setCost(11);
        setTraining(1);
        setHealth(20);
        setArmour(1.2);
        setMorale(3);
        setSpeed(25);
        setAttack(55);
        setShieldDefense(0);
        setChain(2);
        setSlots(1);
    }
    
    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "crossbowman";}

    @Override
    public Crossbowman clone(){
        return new Crossbowman(false);
    }
    
}
