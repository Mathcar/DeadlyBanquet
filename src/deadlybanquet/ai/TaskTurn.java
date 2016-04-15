package deadlybanquet.ai;

import deadlybanquet.model.Direction;

public class TaskTurn implements Task{

	private Direction direction;
	
	public TaskTurn(Direction dir){
		this.direction = dir;
	}
	
	@Override
	public boolean execute(AIControler aiControler) {
		aiControler.turn(direction);
		return true;
	}

}
