package unsw.gloriaromanus.troop;

public class Elephant_archer extends Elephant {

    /**
     *
     */
    private static final long serialVersionUID = -7016995209633616455L;

    public Elephant_archer(boolean mercenary) {
        super(mercenary);
        setCost(64);
        setTraining(4);
        setHealth(160);
        setArmour(1.3);
        setMorale(6);
        setSpeed(20);
        setAttack(75);
        setShieldDefense(0);
        setChain(5);
        setSlots(3);
    }

    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "elephant_archer";}
    
    @Override
    public Elephant_archer clone(){
        return new Elephant_archer(false);
    }
}
