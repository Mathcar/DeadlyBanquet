package deadlybanquet.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import deadlybanquet.ai.Condition.ConditionState;
import deadlybanquet.model.Direction;

public class StateBasedAI {
	
	private enum AIState{
		IDLE_STATE,
		TALKING_STATE,
		MOVEING_STATE
	}
	
	private Queue<Task> schedual;
	
	private AIState state;
	
	public StateBasedAI(){
		schedual = new LinkedList<Task>();
		state = AIState.IDLE_STATE;
	}
	
	public void think(){ //method is not runnable
		
		List<String> characters = null;//= getCharactersInRoom()
		//This should maybe be as a parameter to the function?
		
		List conditions = genConditions(characters);
		//Brainstorming: Combination of a parameter and condition generated from memory?  
		
		selectState();
		
		if(conditions.contains(ConditionState.INTERUPTED) || schedual.isEmpty()){
			schedual.clear();
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
