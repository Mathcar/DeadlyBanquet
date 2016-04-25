package deadlybanquet.ai;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.model.*;

import deadlybanquet.speech.SpeechAct;
import org.newdawn.slick.util.pathfinding.Path;

import deadlybanquet.model.Character;

public class AIControler {
	private Character character;
	private StateBasedAI statebasedAI;
	private int pathStep;
	private final static int MOVEMNET_DELAY = 32;
	private int movmentTimer = 0;
	private MasterPath masterPath;
	private Path path;
	private int movtim = 0;
	
	public AIControler(ActionListener al){
		this.character = new Character(al, "Frido", 3, 3);
		statebasedAI = new StateBasedAI();
        pathStep = 0;
	}
	
	public AIControler(ActionListener al, Character c){
		this.character = c;
		statebasedAI = new StateBasedAI();
		pathStep = 0;
	}
	
	public void moveNPC(World world){
		Character c = getCharacter();
        int targetX = path.getStep(pathStep).getX();
        int targetY = path.getStep(pathStep).getY();
        int x = c.getPos().getX();
        int y = c.getPos().getY();
        if (targetX != x) {
            if(x < targetX){
            	c.setDirection(Direction.EAST);
            }else{
            	c.setDirection(Direction.WEST);
            }
            world.attemptMove(getCharacter(), getCharacterDirection());
           
        	//x += (targetX - x) / Math.abs(targetX - x); //Move one pixel per update in the correct x direction
        } else if (targetY != y) {
        	if(y < targetY){
            	c.setDirection(Direction.SOUTH);
            }else{
            	c.setDirection(Direction.NORTH);
            }
            world.attemptMove(c, getCharacterDirection());
          
        	//y += (targetY - y) / Math.abs(targetY - y); //Move one pixel per update in the correct x direction
        } else if (path.getLength() > pathStep+1) {
            pathStep++;  //If a grid has been reached, increment the path so the next grid will be the target
        } else{
            path = null; //Remove current path if destination has been reached
        }
        

	}

	//Called on every person in origin and destination rooms on room change.
	public void observeRoomChange(String who, String origin, String destination){
		//DEBUG
		System.out.println(getCharacterName() + " has seen " + who + " enter " + destination + " from " + origin);
        //
		NPCBrain myBrain = World.getControlerBrain(this);
		myBrain.observeRoomChange(who,origin,destination);
	}

	//called on entering a room
	public void seePeople (ArrayList<String> people){
        NPCBrain myBrain = World.getControlerBrain(this);
        myBrain.seePeople(people);
        System.out.println(getCharacterName() + " sees these people upon entering the room: ");
        for(String s : people){
            System.out.println(s);
        }
	}

	public void observeInteraction(String who, String with){
        NPCBrain myBrain = World.getControlerBrain(this);
        myBrain.observeInteraction(who, with);
		//TODO IMPLEMENT
	}

	public void stepPath(){
		if(path==null){
			if(masterPath != null){
				//Last path ended but masterpath still has steps to go
				requestPath();
			}
		}else{
        }
	}

    public MasterPath requestMasterPath(){
    	return masterPath;
    }

	public Path requestPath(){return new Path();}

	public void setPath(Path p){
		//Reset path counter?
        pathStep  = 0;
		path = p;
	}

	public void setMasterPath(MasterPath mp){
		masterPath = mp;

	}

	public SpeechAct selectPhrase(ArrayList<SpeechAct> acts){
		return statebasedAI.selectPhrase(acts);
	}


	public String getCharacterName(){
		return character.getName();
	}
	
	public int getCharacterId(){
		return character.getId();
	}

    /*OBOLETE, blocked status is no longer in character but instead flagged
	private boolean checkBlocked(){
		if(npc.isBlocked()){
			npc.unblock();
			return true;
		}else{
			return false;
		}
	}
	*/
	
	public Character getCharacter(){
		return this.character;
	}
	
	public void turn(Direction dir){
		character.setDirection(dir);
	}
	
	public Direction getCharacterDirection(){
		return character.getDirection();
	}
	
	public boolean isTalking() {
		return character.isTalking();
	}

	public void setTalking(boolean talking) {
		character.setTalking(talking);
	}
	
	//ugly test code
	public void update(World world, int deltaTime){
		/*Task t = new TaskTurn(Direction.EAST);
		t.execute(this);'
		*/
		
		/*if(masterPath != null && !masterPath.isEmpty() && path == null){
			world.attemptCreatePathToDoor(this, masterPath.getNext());
			world.attemptChangeRooms(this.getCharacter());
			world.attemptCreatePathToPerson(this, "Cindy");
		}*/
		
		
		if(hasPath() && movtim < 1){
			moveNPC(world);
			movtim = MOVEMNET_DELAY/2;
		}
		
		if(masterPath != null && !masterPath.isEmpty() && hasPath() == false){
			world.attemptCreatePathToDoor(this, masterPath.getNext());
			if(world.attemptChangeRooms(getCharacter())){
				masterPath.removeNext();
			}
		}
	
        if(!getCharacter().isMoving()){
        	Direction d = world.getRoomOfCharacter(getCharacter()).getAdjacentDoorDirection(getCharacter().getPos());
        	if(d != null){
        		getCharacter().setDirection(d);
        	}	
        }
		
		movtim--;
		
		statebasedAI.think(this, world, world);
	}
	//end

	public void setBlocked(boolean blocked) {
		character.setBlocked(blocked);
	}
	
	public boolean isBlocked() {
		return character.isBlocked();
	}
	
	public void abortPath(){
		path = null;
	}
	
	public boolean hasPath(){
		if(path==null){
			return false;
		}else{
			return true;
		}
	}

	public Path getPath(){
		return this.path;
	}
}
