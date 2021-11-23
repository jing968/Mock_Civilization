package unsw.gloriaromanus.troop;

public class Archerman_egyptian extends Archerman {

    /**
     *
     */
    private static final long serialVersionUID = 7275189195728168075L;

    public Archerman_egyptian(boolean mercenary) {
        super(mercenary);
    }

    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "archerman_egyptian";}
    
    @Override
    public Archerman_egyptian clone(){
        return new Archerman_egyptian(false);
    }
}
