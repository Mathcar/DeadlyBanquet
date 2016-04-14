package deadlybanquet.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import deadlybanquet.ai.Condition.ConditionState;
import deadlybanquet.model.Direction;

public class StateBasedAI {
	
	private Queue<Task> schedual;
	
	public void think(){ //method is not runnable
		List<String> characters = null;//= getCharactersInRoom()
		
		List conditions = genConditions(characters);
		
		selectState();
		
		if(conditions.contains(ConditionState.INTERUPTED) || schedual.isEmpty()){
			schedual = new LinkedList<Task>();
			schedual.add(new TaskTurn(Direction.EAST));//only turn the character east at the moment
		}
		
		schedual.peek().execute(new AIControler(null));//should send the AIControler of the character that should execute the task.
	}
	
	private List genConditions(List<String> characters){
		return new ArrayList();
	}
	
	private void selectState(){
		
	}
	
}
