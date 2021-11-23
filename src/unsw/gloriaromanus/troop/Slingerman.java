package unsw.gloriaromanus.troop;

public class Slingerman extends Infantry{

	/**
     *
     */
    private static final long serialVersionUID = 2602976619479447650L;

    public Slingerman(boolean mercenary) {
        super(mercenary);
        setCost(10);
        setTraining(1);
        setHealth(30);
        setArmour(1.2);
        setMorale(3);
        setSpeed(20);
        setAttack(60);
        setShieldDefense(0);
        setChain(2);
        setSlots(1);
    }
    
    @Override
    public String getType() {return "slingerman";}

    @Override
    public Slingerman clone(){
        return new Slingerman(false);
    }
}
