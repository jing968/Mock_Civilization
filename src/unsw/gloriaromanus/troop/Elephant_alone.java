package unsw.gloriaromanus.troop;

public class Elephant_alone extends Elephant{

    /**
     *
     */
    private static final long serialVersionUID = -4459009257784742260L;

    public Elephant_alone(boolean mercenary) {
        super(mercenary);
        setCost(56);
        setTraining(4);
        setHealth(200);
        setArmour(1.3);
        setMorale(5);
        setSpeed(20);
        setAttack(40);
        setShieldDefense(0);
        setChain(5);
        setSlots(3);
        if(this.isMercenary()){
            this.setCost(3*this.getCost());
            setMorale(0.8*getMorale());
		}
    }


    @Override
    public String getType() {return "elephant_alone";}

    @Override
    public Elephant_alone clone(){
        return new Elephant_alone(false);
    }
    
    
}
