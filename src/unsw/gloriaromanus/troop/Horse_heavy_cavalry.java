package unsw.gloriaromanus.troop;

public class Horse_heavy_cavalry extends Horse{

    /**
     *
     */
    private static final long serialVersionUID = 3883070260547793231L;

    public Horse_heavy_cavalry(boolean mercenary) {
        super(mercenary);
        setCost(40);
        setTraining(4);
        setHealth(80);
        setArmour(1.3);
        setMorale(6);
        setSpeed(20);
        setAttack(80);
        setShieldDefense(0.3);
        setChain(4);
        setSlots(2);
		if(this.isMercenary()){
            setCost(3*getCost());
            setMorale(0.8*getMorale());
		}
    }

    
    @Override
    public String getType() {return "horse_heavy_cavalry";}

    @Override
    public Horse_heavy_cavalry clone(){
        return new Horse_heavy_cavalry(false);
    }
    
}
