package deadlybanquet.ai;

import deadlybanquet.model.Direction;
import deadlybanquet.model.World;

public class TaskMoveStep implements Task {

	private Direction direction;
	
	private World world;
	
	public TaskMoveStep(Direction direction, World world){
		this.direction = direction;
		this.world = world; 
	}
	
	@Override
	public boolean execute(AIControler aiControler) {
		world.attemptMove(aiControler.getCharacter(), direction);
		return true;
	}

}
