package unsw.gloriaromanus.troop;

public class Ballistatower extends Troop{

    /**
     *
     */
    private static final long serialVersionUID = 8334829819346641861L;

    public Ballistatower() {
        super();
        setHealth(100);
        setArmour(1);
        setMorale(3);
        setSpeed(0);
        setAttack(80);
        setShieldDefense(0);

    }

    @Override
    public String getType() {
        return "ballista";
    }

    
    @Override
    public boolean isRanged(){
        return true;
    }
    
}
