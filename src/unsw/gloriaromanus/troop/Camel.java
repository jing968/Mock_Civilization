package unsw.gloriaromanus.troop;

public class Camel extends Cavalry{

    /**
     *
     */
    private static final long serialVersionUID = 5430378915895271248L;

    public Camel(boolean mercenary) {
        super(mercenary);
        setCost(12);
        setTraining(1);
        setHealth(80);
        setArmour(1.1);
        setMorale(3);
        setSpeed(40);
        setAttack(30);
        setShieldDefense(0);
        setChain(3);
        setSlots(2);
    }


    @Override
    public String getType() {return "camel";}

    @Override
    public Camel clone(){
        return new Camel(false);
    }
    
    
}
