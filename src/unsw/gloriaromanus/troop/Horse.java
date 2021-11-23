package unsw.gloriaromanus.troop;

public class Horse extends Cavalry{
    /**
     *
     */
    private static final long serialVersionUID = 7741629532275868691L;

    public Horse(boolean mercenary) {
        super(mercenary);
    }
    
    @Override
    public String getType() {return "horse";}
    
    @Override
    public Horse clone(){
        return new Horse(false);
    }
}
