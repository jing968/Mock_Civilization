package unsw.gloriaromanus.troop;

public class Horse_lancer extends Horse{

    /**
     *
     */
    private static final long serialVersionUID = 5604033243590654242L;

    public Horse_lancer(boolean mercenary) {
        super(mercenary);
        setCost(34);
        setTraining(3);
        setHealth(50);
        setArmour(1.1);
        setMorale(6);
        setSpeed(30);
        setAttack(80);
        setShieldDefense(0.3);
        setChain(3);
        setSlots(2);
		if(this.isMercenary()){
            this.setCost(3*this.getCost());
            setMorale(0.8*getMorale());
		}
    }

    
    @Override
    public String getType() {return "horse_lancer";}

    @Override
    public Horse_lancer clone(){
        return new Horse_lancer(false);
    }
    
}
