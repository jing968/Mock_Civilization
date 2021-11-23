package unsw.gloriaromanus.troop;

public class Horse_archer extends Horse {

    /**
     *
     */
    private static final long serialVersionUID = -1825352492276435789L;

    public Horse_archer(boolean mercenary) {
        super(mercenary);
        setCost(30);
        setTraining(3);
        setHealth(38);
        setArmour(1.22);
        setMorale(4);
        setSpeed(35);
        setAttack(75);
        setShieldDefense(0);
        setChain(3);
        setSlots(2);
		if(this.isMercenary()){
			this.setCost(2*this.getCost());
		}
    }

    @Override
    public boolean isRanged(){return true;}
    
    @Override
    public String getType() {return "horse_archer";}

    @Override
    public Horse_archer clone(){
        return new Horse_archer(false);
    }
    
}
