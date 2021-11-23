package unsw.gloriaromanus.troop;

public class Swordman extends Infantry{

    /**
     *
     */
    private static final long serialVersionUID = -4382554441378230711L;

    public Swordman(boolean mercenary) {
        super(mercenary);
        setCost(12);
        setTraining(1);
        setHealth(30);
        setArmour(1.5);
        setMorale(5);
        setSpeed(15);
        setAttack(80);
        setShieldDefense(0);
        setChain(1);
        setSlots(1);
    }

    
    @Override
    public String getType() {return "swordman";}

    
    @Override
    public Swordman clone(){
        return new Swordman(false);
    }
}
