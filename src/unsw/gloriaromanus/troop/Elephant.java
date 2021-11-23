package unsw.gloriaromanus.troop;

public class Elephant extends Cavalry {

    /**
     *
     */
    private static final long serialVersionUID = -6115914839777267200L;

    public Elephant(boolean mercenary) {
        super(mercenary);
    }
    

    @Override
    public String getType() {return "elephant";}


    @Override
    public Elephant clone(){
        return new Elephant(false);
    }
}
