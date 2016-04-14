package deadlybanquet.ai;

public interface Task {

	/**
	 * Execute the task on the given character.
	 * @param aiControler The controller of the AIs character.
	 * @return true if the task is completed.
	 */
	public boolean execute(AIControler aiControler);
	
}
