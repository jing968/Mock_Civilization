package unsw.gloriaromanus.infrastructure;

public class Wall extends Infrastructure {
    /**
     *
     */
    private static final long serialVersionUID = -2538831631448749654L;

    public Wall() {
        super(1500.0, 140.0, 2, 1.0, 1, 1);
    }

    /**
     * Get the type of wall this wall is, super class of 
     *  archertower and ballista tower.
     * @return
     */
    public String walltype(){
        return "wall";
    }
}
