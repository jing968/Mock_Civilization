package unsw.gloriaromanus.troop;

public class Horse_alone extends Horse{

	/**
     *
     */
    private static final long serialVersionUID = 4382944947689292388L;

    public Horse_alone(boolean mercenary) {
        super(mercenary);
        setCost(10);
        setTraining(1);
        setHealth(30);
        setArmour(1.2);
        setMorale(3);
        setSpeed(40);
        setAttack(20);
        setShieldDefense(0);
        setChain(3);
        setSlots(2);
		if(this.isMercenary()){
			this.setCost(3*this.getCost());
		}
	}
	
    @Override
    public String getType() {return "horse_alone";}

    @Override
    public Horse_alone clone(){
        return new Horse_alone(false);
    }
    
}
