package unsw.gloriaromanus.troop;

public class Spearman extends Infantry {

    /**
     *
     */
    private static final long serialVersionUID = -959783586185984625L;

    public Spearman(boolean mercenary) {
        super(mercenary);
        setCost(16);
        setTraining(2);
        setHealth(40);
        setArmour(1.3);
        setMorale(5);
        setSpeed(18);
        setAttack(90);
        setShieldDefense(0.2);
        setChain(2);
        setSlots(1);
    }

    
    @Override
    public String getType() {return "spearman";}


    @Override
    public Spearman clone(){
        return new Spearman(false);
    }
    
}
