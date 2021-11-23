package unsw.gloriaromanus.troop;

public class Trebuchet extends Artillery{

    /**
     *
     */
    private static final long serialVersionUID = 126041707401805579L;

    public Trebuchet() {
        super();
        setCost(85);
        setTraining(5);
        setHealth(200);
        setArmour(1.3);
        setMorale(5);
        setSpeed(15);
        setAttack(90);
        setShieldDefense(0);
        setChain(5);
        setSlots(4);
    }

    @Override
    public boolean isRanged() {return true;}
    
    @Override
    public String getType() {return "trebuchet";}

    @Override
    public Trebuchet clone(){
        return new Trebuchet();
    }
    
}
