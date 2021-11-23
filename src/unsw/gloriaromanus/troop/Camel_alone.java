package unsw.gloriaromanus.troop;

public class Camel_alone extends Camel{
 
    /**
     *
     */
    private static final long serialVersionUID = -4447519023748917057L;

    public Camel_alone(boolean mercenary) {
        super(mercenary);

    }

    @Override
    public String getType() {return "camel_alone";}

    @Override
    public Camel_alone clone(){
        return new Camel_alone(false);
    }
    
    
}
