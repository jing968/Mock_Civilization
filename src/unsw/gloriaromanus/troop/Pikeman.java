package unsw.gloriaromanus.troop;

public class Pikeman extends Infantry {

    /**
     *
     */
    private static final long serialVersionUID = 4581783632374074244L;

    public Pikeman(boolean mercenary) {
        super(mercenary);
        setCost(16);
        setTraining(2);
        setHealth(40);
        setArmour(1.2);
        setMorale(5);
        setSpeed(15);
        setAttack(75);
        setShieldDefense(0);
        setChain(2);
        setSlots(1);
    }
    
    
    @Override
    public String getType() {return "pikeman";}

    @Override
    public Pikeman clone(){
        return new Pikeman(false);
    }
}
