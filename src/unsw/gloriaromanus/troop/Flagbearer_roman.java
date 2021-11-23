package unsw.gloriaromanus.troop;

public class Flagbearer_roman extends Flagbearer{

    /**
     *
     */
    private static final long serialVersionUID = 7753724682633358842L;

    public Flagbearer_roman(Boolean mercenary) {
        super(mercenary);
    }

    
    @Override
    public String getType() {return "flagbearer_roman";}
    

    @Override
    public Flagbearer_roman clone(){
        return new Flagbearer_roman(false);
    }
}
