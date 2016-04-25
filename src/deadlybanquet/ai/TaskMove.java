package deadlybanquet.ai;

import org.newdawn.slick.util.pathfinding.Path; // not sure this is the correct Path

import deadlybanquet.model.Direction;
import deadlybanquet.model.MasterPath;
import deadlybanquet.model.Position;
import deadlybanquet.ai.MoveTypes;

public class TaskMove implements Task{
	
	String target;
	TaskExecuter taskEx;
	MoveTypes moveType;
	
	public TaskMove(String targ, TaskExecuter tEx, MoveTypes mt){
		target = targ;
		taskEx = tEx;
		moveType = mt;
	}

	@Override
	public boolean execute(AIControler aiControler) {
		if(!aiControler.hasPath()){
			if(moveType == MoveTypes.DOOR){
				return taskEx.attemptCreatePathToDoor(aiControler, target);
			}else if(moveType == MoveTypes.PERSON){
				return taskEx.attemptCreatePathToPerson(aiControler, target);
			}else{
				return taskEx.attemptCreateMasterPath(aiControler, target);
			}
		}
		return false;
	}
	

}
