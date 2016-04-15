package deadlybanquet.ai;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.model.Character;
import deadlybanquet.model.Direction;
import deadlybanquet.model.MasterPath;

import deadlybanquet.model.World;

import org.newdawn.slick.util.pathfinding.Path;

import deadlybanquet.model.NPC;

public class AIControler {
	private NPC npc;
	
	
	private final static int MOVEMNET_DELAY = 32;
	private int movmentTimer = 0;
	private MasterPath masterPath;
	private Path path;
	
	public AIControler(ActionListener al){
		this.npc = new NPC(al, "Frido", 3, 3);
	}
	
	public AIControler(ActionListener al, deadlybanquet.model.Character c){
		this.npc = new NPC(al, c);
		
	}
	
	public void moveNPC(){
		if(this.checkBlocked()){
			//notify AI to make a decision
			//and discard path
		}else{
			int xMovement = npc.getPos().getX()-path.getX(0);
			int yMovement = npc.getPos().getY()-path.getY(0);
			if(xMovement==0){
				if(yMovement<0){
					npc.moveN();
				}else if(yMovement>0){
					npc.moveS();
				}
			}else{
				if(xMovement<0){
					npc.moveW();
				}else if(xMovement>0){
					npc.moveE();
				}
			}
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

	public void setPath(Path p){
		//Reset path counter?
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
	
	private boolean checkBlocked(){
		if(npc.isBlocked()){
			npc.unblock();
			return true;
		}else{
			return false;
		}
	}
	
	public NPC getNpc(){
		return this.npc;
	}
	
	public void turn(Direction dir){
		npc.setDirection(dir);
	}
	
	public Direction getCharacterDirection(){
		return npc.getDirection();
	}
	
	//ugly test code
	public void update(){
		Task t = new TaskTurn(Direction.EAST);
		t.execute(this);
	}
	//end
}
