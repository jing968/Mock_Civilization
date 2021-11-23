package unsw.gloriaromanus.troop;

public class Cavalry extends Troop{
    /**
     *
     */
    private static final long serialVersionUID = -8194597663858373277L;
    private boolean mercenary;
    public Cavalry(boolean mercenary) {
        super();
        this.mercenary = mercenary;
        this.setMovement(15);
    }
    
   
    public boolean isMercenary(){
        return mercenary;
    }

    public void setMercenary(boolean mercenary){
        this.mercenary = mercenary;
    }
    
}
