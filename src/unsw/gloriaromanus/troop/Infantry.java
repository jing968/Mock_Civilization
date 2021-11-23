package unsw.gloriaromanus.troop;

public class Infantry extends Troop{
    /**
     *
     */
    private static final long serialVersionUID = -6552528750608334074L;
    private boolean mercenary;
    public Infantry(boolean mercenary) {
        super();
        this.mercenary = mercenary;
        this.setMovement(10);
        if(this.isMercenary()){
            this.setCost(2*this.getCost());
            
        }
    }

    public boolean isMercenary(){
        return mercenary;
    }

    public void setMercenary(boolean mercenary){
        this.mercenary = mercenary;
    }
    
}
