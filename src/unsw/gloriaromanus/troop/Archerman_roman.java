package unsw.gloriaromanus.troop;

public class Archerman_roman extends Archerman{

    /**
     *
     */
    private static final long serialVersionUID = -5427548377643570621L;

    public Archerman_roman(boolean mercenary) {
        super(mercenary);
    }

    @Override
    public boolean isRanged(){return true;}

    @Override
    public String getType() {return "archerman_roman";}

    @Override
    public Archerman_roman clone(){
        return new Archerman_roman(false);
    }
    
}
    

