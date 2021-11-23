package unsw.gloriaromanus.troop;


public class Flagbearer extends Infantry {

    /**
     *
     */
    private static final long serialVersionUID = -2668520162840757414L;

    public Flagbearer(boolean mercenary) {
        super(mercenary);
        setCost(12);
        setTraining(1);
        setHealth(25);
        setArmour(1.3);
        setMorale(4);
        setSpeed(20);
        setAttack(60);
        setShieldDefense(0);
        setChain(2);
        setSlots(1);
    }
    
    @Override
    public String getType() {return "flagbearer";}



    @Override
    public Flagbearer clone(){
        return new Flagbearer(false);
    }

}
