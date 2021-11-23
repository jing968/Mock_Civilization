package unsw.gloriaromanus.troop;

public class Flagbearer_egyptian extends Flagbearer{

    /**
     *
     */
    private static final long serialVersionUID = 2356165497800870021L;

    public Flagbearer_egyptian(boolean mercenary) {
        super(mercenary);
    }


    @Override
    public String getType() {return "flagbearer_egyptian";}
    
    @Override
    public Flagbearer_egyptian clone(){
        return new Flagbearer_egyptian(false);
    }
}
