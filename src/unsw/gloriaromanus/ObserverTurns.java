package unsw.gloriaromanus;

public interface ObserverTurns {
    /**
     * Given the subject update the observers accordingly.
     *  In this case it would be turns within subject
     * @param obj
     */
    public void update(SubjectTurns obj);
}
