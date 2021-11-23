package unsw.gloriaromanus.infrastructure;

public class Farm extends Infrastructure {
    /**
     *
     */
    private static final long serialVersionUID = -8990300577688898374L;

    public Farm() {
        super(800.0, 90.0, 2.0, 1.0, 1, 7);
    }

    /**
     * Display the chain this infrastructure is.
     */
    @Override
    public String toString() {
        return "" + getChain();
    }
}
