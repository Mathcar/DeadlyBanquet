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

	
	private AIState state;
	
	List<Condition> conditions;
	
	private Queue<Task> schedule;
	
	public StateBasedAI(){
		schedule = new LinkedList<Task>();
		state = AIState.IDLE_STATE;
		conditions = new ArrayList<Condition>();
	}
	
	public void think(AIControler aic){ //method is not runnable
		
		List<String> characters = null;//= getCharactersInRoom()
		
		genConditions(aic); 
		
		selectState();
		
		generateSchedule();
		
		runSchedule(aic);
		
	}
	
	private void generateSchedule() {
		if(schedule.isEmpty()){
			schedule.clear();
			switch(state){
				case IDLE_STATE:
					schedule.add(new TaskTurn(Direction.EAST));//only turn the character east at the moment
					break;
				case TALKING_STATE:
					schedule.add(new TaskIdle());
					break;
				case MOVEING_STATE:
					schedule.add(new TaskIdle());
					break;
				default:
					break;
			}
		}
	}

	private void genConditions(AIControler aic){
		conditions.clear();
		if(aic.isTalking()){
			conditions.add(new Condition(ConditionState.TALKING));
		}
		//if talking: add Talking-condition
		
		//add more conditions
	}
	
	private void selectState(){
		for(Condition c: conditions){
			if(c.getCondition()==ConditionState.TALKING){
				state=AIState.TALKING_STATE;
			}
		}
	}
	
	private void runSchedule(AIControler aic){
		if(schedule.peek().execute(aic)){
			schedule.poll();
		}
	}
	
}
