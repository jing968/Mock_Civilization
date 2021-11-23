package unsw.gloriaromanus.troop;

public class Camel_archer extends Camel{

    /**
     *
     */
    private static final long serialVersionUID = 6749526203835184910L;

    public Camel_archer(boolean mercenary) {
        super(mercenary);
        setCost(30);
        setTraining(2);
        setHealth(38);
        setArmour(1.22);
        setMorale(4);
        setSpeed(35);
        setAttack(75);
        setShieldDefense(0);
        setChain(4);
        setSlots(2);

    }
    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "camel_archer";}

    @Override
    public Camel_archer clone(){
        return new Camel_archer(false);
    }
    
    
}
