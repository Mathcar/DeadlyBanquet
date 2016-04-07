package deadlybanquet.ai;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.model.Character;
import deadlybanquet.model.MasterPath;
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
		//TODO IMPLEMENT
	}

	//called on entering a room
	public void seePeople (ArrayList<String> people){

		//TODO IMPLEMENT
	}

	public void observeInteraction(String who, String with){

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
}
