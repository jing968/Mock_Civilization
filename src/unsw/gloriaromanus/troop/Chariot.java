package unsw.gloriaromanus.troop;

public class Chariot extends Cavalry {

    /**
     *
     */
    private static final long serialVersionUID = 4678011663462123624L;

    public Chariot(boolean mercenary) {
        super(mercenary);
        setCost(44);
        setTraining(4);
        setHealth(40);
        setArmour(1.3);
        setMorale(4);
        setSpeed(40);
        setAttack(40);
        setShieldDefense(0);
        setChain(4);
        setSlots(2);
        if(this.isMercenary()){
            this.setCost(3*this.getCost());
            setMorale(0.8*getMorale());
		}
    }
    
    @Override
    public String getType() {return "chariot";}
  
    @Override
    public Chariot clone(){
        return new Chariot(false);
    }
    
}
