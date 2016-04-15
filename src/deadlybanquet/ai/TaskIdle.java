package deadlybanquet.ai;

/**
 * 
 * @author Mathias
 *
 * An empty task that does not do anything.
 * 
 */

public class TaskIdle implements Task{

	@Override
	public boolean execute(AIControler aiControler) {
		return true;
	}

}
