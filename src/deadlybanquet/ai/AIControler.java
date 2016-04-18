package deadlybanquet.ai;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.model.*;

import org.newdawn.slick.util.pathfinding.Path;

import deadlybanquet.model.Character;

public class AIControler {
	private Character npc;
	private StateBasedAI statebasedAI;
	private int currentPathStep;
	private final static int MOVEMNET_DELAY = 32;
	private int movmentTimer = 0;
	private MasterPath masterPath;
	private Path path;
	
	public AIControler(ActionListener al){
		this.npc = new Character(al, "Frido", 3, 3);
		statebasedAI = new StateBasedAI();
        currentPathStep = 0;
	}
	
	public AIControler(ActionListener al, Character c){
		this.npc = c;
		statebasedAI = new StateBasedAI();
		currentPathStep = 0;
	}
	
	public void moveNPC(){
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

    public MasterPath requestMasterPath(){return new MasterPath();}

	public Path requestPath(){return new Path();}

	public void setPath(Path p){
		//Reset path counter?
        currentPathStep  = 0;
		path = p;
	}

	public void setMasterPath(MasterPath mp){
		masterPath = mp;

	}
	public String getCharacterName(){
		return npc.getName();
	}
	
	public int getCharacterId(){
		return npc.getId();
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
	
	public Character getNpc(){
		return this.npc;
	}
	
	public void turn(Direction dir){
		npc.setDirection(dir);
	}
	
	public Direction getCharacterDirection(){
		return npc.getDirection();
	}
	
	public boolean isTalking() {
		return npc.isTalking();
	}

	public void setTalking(boolean talking) {
		npc.setTalking(talking);
	}
	
	//ugly test code
	public void update(World world, int deltaTime){
		/*Task t = new TaskTurn(Direction.EAST);
		t.execute(this);'
		*/
		statebasedAI.think(this);
	}
	//end

	public void setBlocked(boolean blocked) {
		npc.setBlocked(blocked);
	}
	
	public boolean isBlocked() {
		return npc.isBlocked();
	}
}
