package deadlybanquet.ai;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import deadlybanquet.ai.Condition.ConditionState;
import deadlybanquet.model.Character;
import deadlybanquet.model.Debug;
import deadlybanquet.model.Direction;
import deadlybanquet.model.Door;
import deadlybanquet.speech.SpeechAct;
import deadlybanquet.model.World;
import deadlybanquet.speech.SpeechActFactory;
import deadlybanquet.speech.TextPropertyEnum;


public class StateBasedAI {

    private enum AIState {
        IDLE_STATE,
        TALKING_STATE,
        MOVEING_STATE
    }


    private SpeechAct intendedPhrase;

    private AIState state;

    private List<Condition> conditions;

    private Queue<Task> schedule;

    private List<Character> characters;
    private List<Door> doors;

    public StateBasedAI() {
        schedule = new LinkedList<Task>();
        state = AIState.IDLE_STATE;
        conditions = new ArrayList<Condition>();
        characters = new ArrayList<Character>();
        doors = new ArrayList<Door>();
    }
    

    public void think(AIControler aic, World world, TaskExecuter taskEx) { //method is not runnable

    	
        getObjectsInRoom(aic, world);
        
        genConditions(aic);

        selectState(aic);

        if (schedule.isEmpty()) {
            Debug.printDebugMessage(aic.getCharacterName() + " has an empty schedule!", Debug.Channel.NPC,
                    aic.getCharacterName());
        }
        
        generateSchedule(aic, world, taskEx);

        conditions.clear();

        runSchedule(aic);


    }

    //Should optimally utilize the schedule created in think() to
    //select an appropriate phrase for the conversation
    public SpeechAct selectPhrase(ArrayList<SpeechAct> acts) {
        int index = (int) (Math.random() * 4 + 0.5);
        SpeechAct choice = acts.get(index);
        return choice;
    }

    //Should return the phrase the AI wanted to express when initiating a conversation
    public SpeechAct getIntendedPhrase() {
        //TODO revise and complete implementation
        //Remove this if this method becomes up-to-date complete
        //Debug.printDebugMessage();("getIntendedPhrase() in StateBasedAI is not yet complete");

        return intendedPhrase;
    }

    private void getObjectsInRoom(AIControler aic, World world) {
        characters.clear();
        characters.addAll(world.getRoomOfCharacter(aic.getCharacter()).getAllCharacters());
        doors.clear();
        doors.addAll(world.getRoomOfCharacter(aic.getCharacter()).getAllDoors());
    }

    private void generateSchedule(AIControler aic, World world, TaskExecuter taskEx) {
        if (!schedule.isEmpty()){}
           /* return;					/*this should not be neccessary, but for some reason
                                        the condition below always returns true! */
        Debug.printDebugMessage("Amount of conditions = " + conditions.size(), Debug.Channel.NPC, aic.getCharacterName());
        for (Condition c : conditions) {
            Debug.printDebugMessage(c.toString(), Debug.Channel.NPC, aic.getCharacterName());
        }


        if (schedule.isEmpty() || conditions.contains(new Condition(ConditionState.INTERUPTED))) {
            Debug.printDebugMessage("generating new schedule!", Debug.Channel.NPC, aic.getCharacterName());
            schedule.clear();
            switch (state) {
                case IDLE_STATE:
                    if (aic.getCharacterName().equals("BURT"))
//                    	schedule.add(new TaskMove("Bedroom", taskEx, MoveTypes.ROOM));
                        talkToCharacterSchedule("ChooseName", world, aic, taskEx);
                    //schedule.add(new TaskTurn(getDirectionToClosestCharacter(aic)));
                	if(characters.size()>1){
                		for(Character c: characters){
                			if(!c.equals(aic.getCharacter()) && Math.random()>0.999){
                				talkToCharacterSchedule(c.getName(), world, aic, taskEx);
                				break;
                			}
                			schedule.add(new TaskIdle());
                		}
                	}
                	for(Door d: doors){
	                	if(Math.random()>0.999){
	                		System.out.println(aic.getCharacter().getName() + " walks to " + d.getDestinationRoom());
	                		schedule.add(new TaskMove(d.getDestinationRoom(), taskEx, MoveTypes.ROOM));
	            			break;
	            		}
                	}
                	if(conditions.contains(new Condition(ConditionState.STANDING_NEXT_TO_DOOR))){
                		moveAwayFromDoorSchedule(aic, world);
                	}
	            	schedule.add(new TaskIdle());
//                    if (aic.getCharacterName().equals("BURT"))
//                    	//schedule.add(new TaskMove("Bedroom", taskEx, MoveTypes.ROOM));
//                        talkToCharacterSchedule("Frido", world, aic, taskEx);
//                    //schedule.add(new TaskTurn(getDirectionToClosestCharacter(aic)));
//                    if (aic.getCharacterName().equals("BURT")) {
//                        schedule.clear();
//                        talkToCharacterSchedule("ChooseName", world, aic, taskEx);
//                    }
                    break;
                case TALKING_STATE:
                    //schedule.add(new TaskIdle());
                    break;
                case MOVEING_STATE:
                	//talkToCharacterSchedule("Daisy", world, aic, taskEx);
                    //schedule.add(new TaskMove("Bedroom", taskEx, MoveTypes.ROOM));
                    //schedule.add(new TaskMove("Daisy", taskEx, MoveTypes.PERSON));
                    break;
                default:
                    break;
            }
        }
    }

    private void moveAwayFromDoorSchedule(AIControler aic, World world) {
    	for(Door d: doors){
        	if(Math.abs(d.getPos().getX()-aic.getCharacter().getPos().getX()) < 2 
        			&& Math.abs(d.getPos().getY()-aic.getCharacter().getPos().getY()) < 2){
        		if(d.getPos().getX()-aic.getCharacter().getPos().getX() < 0){
					schedule.add(new TaskMoveStep(Direction.EAST, world));
        		}else if(d.getPos().getX()-aic.getCharacter().getPos().getX() > 0){
        			schedule.add(new TaskMoveStep(Direction.WEST, world));
        		}else if(d.getPos().getY()-aic.getCharacter().getPos().getY() < 0){
        			schedule.add(new TaskMoveStep(Direction.SOUTH, world));
        		}else{
        			schedule.add(new TaskMoveStep(Direction.NORTH, world));
        		}
    			break;
    		}
    	}
	}


	private void genConditions(AIControler aic) {
        if (aic.isTalking()) {
            conditions.add(new Condition(ConditionState.TALKING));
        }
        /*if (aic.isBlocked()) {
        	if(aic.getCharacterName().equals("BURT")){
        		System.out.println("ildsaksdah");
        	}
            aic.setBlocked(false);
            conditions.add(new Condition(ConditionState.INTERUPTED));
<<<<<<< HEAD
        }*/
        
        
        for(Door d: doors){
        	if(Math.abs(d.getPos().getX()-aic.getCharacter().getPos().getX()) < 2 
        			&& Math.abs(d.getPos().getY()-aic.getCharacter().getPos().getY()) < 2){
        		conditions.add(new Condition(ConditionState.STANDING_NEXT_TO_DOOR));
        	}
        }


        //add more conditions
    }

    public void setCondition(ConditionState condState){
    	conditions.add(new Condition(condState));
    }
    
    private void selectState(AIControler aic) {
        switch (state) {
            case IDLE_STATE:
                if (conditions.contains(ConditionState.TALKING)) {
                    Debug.printDebugMessage("Talking condition discovered, swapping state to talking state",
                            Debug.Channel.NPC, aic.getCharacterName());
                    // Cant see the point of this one, wont it just disrupt the talking state on in the next frame?
                    //   /Hampus
                    //conditions.add(new Condition(ConditionState.INTERUPTED));
                    state = AIState.TALKING_STATE;
                } else if (aic.hasPath()) {
                    state = AIState.MOVEING_STATE;
                }
                break;
            case TALKING_STATE:
                if (!conditions.contains(new Condition(ConditionState.TALKING))) {
                	System.out.println(aic.getCharacter().getName() +" is no longer Talking.");
                    state = AIState.IDLE_STATE;
                }
                break;
            case MOVEING_STATE:
                if (!aic.hasPath()) {
                    state = AIState.IDLE_STATE;
                }
                break;
        }
    }

    private void runSchedule(AIControler aic) {
        if (!schedule.isEmpty() ) {
        	if(schedule.peek().execute(aic)){
	            Debug.printDebugMessage(aic.getCharacterName() + " executed " + schedule.peek().toString() +
	                            " Remaining tasks = " + schedule.size(),
	                    Debug.Channel.NPC, aic.getCharacterName());
	            schedule.poll();
        	}else{
        		
        	}
        }
    }

    private void talkToCharacterSchedule(String character, World world, AIControler aic, TaskExecuter taskEx) {
    	 
        for (Character c : characters) {
            if (c.getName().equals(character)) {
                schedule.add(new TaskMove(character, taskEx, MoveTypes.PERSON));
                Debug.printDebugMessage(aic.getCharacterName() + " added taskMove in talkToCharacterSchedule",
                        Debug.Channel.NPC, aic.getCharacterName());
                schedule.add(new TaskTurnTo(taskEx, c));
                intendedPhrase = SpeechActFactory.convertIThoughtToSpeechAct(new Whereabouts("Candy", ""),
                        TextPropertyEnum.NEUTRAL, aic.getCharacterName(), character);
            }
        }
        
        
		/*if(characters.contains(character)){
			schedule.add(new TaskMove(character.getName(),taskEx,MoveTypes.PERSON));
		}else{
			//schedule.add(new TaskMove(world.getRoomOfCharacter(character).getName(), taskEx, MoveTypes.ROOM));
		}*/
        schedule.add(new TaskInteract(taskEx));
        Debug.printDebugMessage(aic.getCharacterName() + " added taskInteract in talkToCharacterSchedule",
                Debug.Channel.NPC, aic.getCharacterName());
        //find character
        //walk to character
        //interact (with character)
    }

    private void moveFromAway() {
        //get closest character
        //move one step away from that character
    }

    private void findCharacterSchedual(Character character) {
        if (!characters.contains(character)) {
            //walk to next room
        }
    }

    private Direction getDirectionToClosestCharacter(AIControler aic) {
        Character closest = getClosestCharacter(aic);
        if (closest == null || getDistance(aic.getCharacter(), closest) > 4) {
            return Direction.SOUTH;
        }

        if (Math.abs(aic.getCharacter().getPos().getX() - closest.getPos().getX()) <
                Math.abs(aic.getCharacter().getPos().getY() - closest.getPos().getY())) {
            if (aic.getCharacter().getPos().getY() > closest.getPos().getY()) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        } else {
            if (aic.getCharacter().getPos().getX() > closest.getPos().getX()) {
                return Direction.WEST;
            } else {
                return Direction.EAST;
            }
        }
    }

    private Character getClosestCharacter(AIControler aic) {
        Character tmp = null;
        for (Character c : characters) {
            if (tmp == null || (getDistance(tmp, aic.getCharacter()) > getDistance(c, aic.getCharacter()))) {
                if (!c.equals(aic.getCharacter())) {
                    tmp = c;
                }
            }
        }

        return tmp;
    }

    private int getDistance(Character c1, Character c2) {
//		return Math.min(Math.abs(c1.getPos().getX()-c2.getPos().getX()),Math.abs(c1.getPos().getY()-c2.getPos().getY()));
        return Math.abs(c1.getPos().getX() - c2.getPos().getX()) + Math.abs(c1.getPos().getY() - c2.getPos().getY());
    }

}
