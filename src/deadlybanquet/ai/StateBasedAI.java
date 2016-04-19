package deadlybanquet.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import deadlybanquet.ai.Condition.ConditionState;
import deadlybanquet.model.Character;
import deadlybanquet.model.Direction;
import deadlybanquet.model.World;

public class StateBasedAI {

	private enum AIState{
		IDLE_STATE,
		TALKING_STATE,
		MOVEING_STATE
	}

	
	private AIState state;
	
	List<Condition> conditions;
	
	private Queue<Task> schedule;
	
	List<Character> characters;
	
	public StateBasedAI(){
		schedule = new LinkedList<Task>();
		state = AIState.IDLE_STATE;
		conditions = new ArrayList<Condition>();
		characters = new ArrayList<Character>();
	}
	
	public void think(AIControler aic, World world){ //method is not runnable
		
		getCharactersInRoom(aic, world);
		
		genConditions(aic); 
		
		selectState();
		
		generateSchedule();
		
		runSchedule(aic);
		
	}
	
	private void getCharactersInRoom(AIControler aic, World world) {
		characters.clear();
		characters.addAll(world.getRoomOfCharacter(aic.getCharacter()).getAllCharacters());
	}

	private void generateSchedule() {
		if(schedule.isEmpty() || conditions.contains(new Condition(ConditionState.INTERUPTED))){
			schedule.clear();
			switch(state){
				case IDLE_STATE:
					schedule.add(new TaskTurn(Direction.EAST));//only spins the character at the moment
					schedule.add(new TaskTurn(Direction.SOUTH));
					schedule.add(new TaskTurn(Direction.WEST));
					schedule.add(new TaskTurn(Direction.NORTH));
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
		if(aic.isBlocked()){
			aic.setBlocked(false);
			conditions.add(new Condition(ConditionState.INTERUPTED));
		}
		
		//add more conditions
	}
	
	private void selectState(){
//		for(Condition c: conditions){
//			if(c.getCondition()==ConditionState.TALKING){
			if(conditions.contains(new Condition(ConditionState.TALKING))){
				conditions.add(new Condition(ConditionState.INTERUPTED));
				state=AIState.TALKING_STATE;
			}
//		}
	}
	
	private void runSchedule(AIControler aic){
		if(!schedule.isEmpty() && schedule.peek().execute(aic)){
			schedule.poll();
		}
	}
	
}
