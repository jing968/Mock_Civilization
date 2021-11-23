package unsw.gloriaromanus.troop;


public class Hoplite extends Infantry {

    /**
     *
     */
    private static final long serialVersionUID = 3370500830037899147L;

    public Hoplite(boolean mercenary) {
        super(mercenary);
        setCost(20);
        setTraining(2);
        setHealth(40);
        setArmour(1.4);
        setMorale(5);
        setSpeed(15);
        setAttack(75);
        setShieldDefense(0);
        setChain(3);
        setSlots(1);
    }
    
    
    @Override
    public String getType() {return "hoplite";}

    @Override
    public Hoplite clone(){
        return new Hoplite(false);
    }
}
