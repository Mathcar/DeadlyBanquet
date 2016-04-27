package deadlybanquet.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import deadlybanquet.ai.Condition.ConditionState;
import deadlybanquet.model.Character;
import deadlybanquet.model.Direction;
import deadlybanquet.speech.SpeechAct;
import deadlybanquet.model.World;
import deadlybanquet.speech.SpeechActFactory;
import deadlybanquet.speech.TextPropertyEnum;


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
	
	public void think(AIControler aic, World world, TaskExecuter taskEx){ //method is not runnable
		
		getCharactersInRoom(aic, world);
		
		genConditions(aic); 
		
		selectState(aic);
		
		generateSchedule(aic,world ,taskEx);

		conditions.clear();

		runSchedule(aic);

		
	}

	//Should optimally utilize the schedule created in think() to
	//select an appropriate phrase for the conversation
	public SpeechAct selectPhrase(ArrayList<SpeechAct> acts){
		int index = (int)(Math.random()*4 + 0.5);
		SpeechAct choice = acts.get(index);
		return choice;
	}
	//Should return the phrase the AI wanted to express when initiating a conversation
	public SpeechAct getIntendedPhrase(){
		//TODO revise and complete implementation
		//Remove this if this method becomes up-to-date complete
		System.out.println("getIntendedPhrase() in StateBasedAI is not yet complete");
		SpeechAct act = ;
		return SpeechActFactory.convertIThoughtToSpeechAct(act, tpe, );
	}
	
	private void getCharactersInRoom(AIControler aic, World world) {
		characters.clear();
		characters.addAll(world.getRoomOfCharacter(aic.getCharacter()).getAllCharacters());
	}

	private void generateSchedule(AIControler aic,World world, TaskExecuter taskEx) {
		if(schedule.isEmpty() || conditions.contains(new Condition(ConditionState.INTERUPTED))){
			schedule.clear();
			switch(state){
				case IDLE_STATE:
					
					schedule.add(new TaskTurn(getDirectionToClosestCharacter(aic)));
					break;
				case TALKING_STATE:
					schedule.add(new TaskIdle());
					break;
				case MOVEING_STATE:
					schedule.add(new TaskMove("Bedroom", taskEx, MoveTypes.ROOM));
					schedule.add(new TaskMove("Daisy", taskEx, MoveTypes.PERSON));
					break;
				default:
					break;
			}
		}
	}

	private void genConditions(AIControler aic){
		if(aic.isTalking()){
			conditions.add(new Condition(ConditionState.TALKING));
		}
		if(aic.isBlocked()){
			aic.setBlocked(false);
			conditions.add(new Condition(ConditionState.INTERUPTED));
		}
		
		//add more conditions
	}
	
	private void selectState(AIControler aic){
		switch(state){
			case IDLE_STATE:
				if(conditions.contains(ConditionState.TALKING)){
					conditions.add(new Condition(ConditionState.INTERUPTED));
					state=AIState.TALKING_STATE;
				}else if(aic.hasPath()){
					state=AIState.MOVEING_STATE;
				}
				break;
			case TALKING_STATE:
				if(!conditions.contains(new Condition(ConditionState.TALKING))){
					state=AIState.IDLE_STATE;
				}
				break;
			case MOVEING_STATE:
				if(!aic.hasPath()){
					state=AIState.IDLE_STATE;
				}
				break;
		}
	}
	
	private void runSchedule(AIControler aic){
		if(!schedule.isEmpty() && schedule.peek().execute(aic)){
			schedule.poll();
		}
	}
	
	private void talkToCharacterSchedule(Character character,World world, TaskExecuter taskEx){
		
		if(characters.contains(character)){
			schedule.add(new TaskMove(character.getName(),taskEx,MoveTypes.PERSON));
		}else{
			schedule.add(new TaskMove(world.getRoomOfCharacter(character).getName(), taskEx, MoveTypes.ROOM));
		}
		schedule.add(new TaskInteract(taskEx));
		
		//find character
		//walk to character
		//interact (with character)
	}
	
	private void moveFromAway(){
		//get closest character
		//move one step away from that character
	}
	
	private void findCharacterSchedual(Character character){
		if(!characters.contains(character)){
			//walk to next room
		}
	}
	
	private Direction getDirectionToClosestCharacter(AIControler aic){
		Character closest = getClosestCharacter(aic);
		if(closest == null || getDistance(aic.getCharacter(), closest)>4){
			return Direction.SOUTH; 
		}
		
		if(Math.abs(aic.getCharacter().getPos().getX() - closest.getPos().getX()) < 
				Math.abs(aic.getCharacter().getPos().getY() - closest.getPos().getY())){
			if(aic.getCharacter().getPos().getY() > closest.getPos().getY()){
				return Direction.NORTH;
			}else{
				return Direction.SOUTH;
			}
		}else{
			if(aic.getCharacter().getPos().getX() > closest.getPos().getX()){
				return Direction.WEST;
			}else{
				return Direction.EAST;
			}
		}
	}
	
	private Character getClosestCharacter(AIControler aic){
		Character tmp=null;
		for(Character c: characters){
			if(tmp==null || (getDistance(tmp,aic.getCharacter())>getDistance(c, aic.getCharacter()))){
				if(!c.equals(aic.getCharacter())){
					tmp=c;
				}
			}
		}
		
		return tmp;
	}
	
	private int getDistance(Character c1, Character c2){
//		return Math.min(Math.abs(c1.getPos().getX()-c2.getPos().getX()),Math.abs(c1.getPos().getY()-c2.getPos().getY()));
		return Math.abs(c1.getPos().getX()-c2.getPos().getX()) + Math.abs(c1.getPos().getY()-c2.getPos().getY());
	}
	
}
