package unsw.gloriaromanus.troop;

public class Cannon extends Artillery{

    /**
     *
     */
    private static final long serialVersionUID = -7044964097251890727L;

    public Cannon() {
        super();
        setCost(90);
        setTraining(5);
        setHealth(180);
        setArmour(1.4);
        setMorale(5);
        setSpeed(10);
        setAttack(100);
        setShieldDefense(0);
        setChain(5);
        setSlots(4);
    }

    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "cannon";}

    @Override
    public Cannon clone(){
        return new Cannon();
    }
    
    
}
