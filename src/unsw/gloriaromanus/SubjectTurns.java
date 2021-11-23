package unsw.gloriaromanus;

public interface SubjectTurns {
	/**
	 * Attach the observer to the subjects
	 * @param o
	 */
	public void attach(ObserverTurns o);
	
	/**
	 * After changes has been made notify all observers
	 */
	public void notifyObservers();

	/**
	 * IterateTurns() within game state by 1.
	 * 	So after everyone has made their turn (cycle)
	 * 	We update turns.
	 */
	public void iterateTurns();

	/**
	 * 
	 * @return Retrieve the turns within subject
	 */
	public int getTurns();
}
