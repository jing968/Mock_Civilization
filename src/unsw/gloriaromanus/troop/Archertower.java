package unsw.gloriaromanus.troop;

public class Archertower extends Troop{

    /**
     *
     */
    private static final long serialVersionUID = -5756906534902747773L;

    public Archertower() {
        super();
        setHealth(100);
        setArmour(1);
        setMorale(3);
        setSpeed(0);
        setAttack(70);
        setShieldDefense(0);
       
    }

    @Override
    public String getType() {
        return "archertower";
    }

    @Override
    public boolean isRanged(){
        return true;
    }
    
}
