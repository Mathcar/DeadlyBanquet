package deadlybanquet.ai;

import org.newdawn.slick.util.pathfinding.Path; // not sure this is the correct Path

import deadlybanquet.model.Position;

public class TaskMove implements Task {
	
	private String room;
	private Position pos;
	private Path path;
	
	public TaskMove(Path path, String room, Position pos){
		this.room = room;
		this.pos = pos;
		this.path = path;
	}

	@Override
	public boolean execute(AIControler aiControler) {
		// TODO Auto-generated method stub
		return false;
	}

}
