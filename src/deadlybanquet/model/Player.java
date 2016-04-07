package deadlybanquet.model;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import deadlybanquet.states.States;

public class Player {
	
	private Character character;
	
	
	private final static int MOVEMENT_DELAY = 32;
	private int movementTimer = MOVEMENT_DELAY;
	
	public Player(ActionListener al){
		this.character = new Character(al, "Gandalf", 5, 5);
	}
	
	public Player(Character c){
		this.character = c;
	}
	
	public Character getCharacter(){
		return this.character;
	}

	//Changed parameters so that ALL update methods are of the same structure
	public void update(GameContainer container, StateBasedGame s, int dt){
		Input in = container.getInput();
		if(this.movementTimer < 1){
			if(in.isKeyDown(Input.KEY_UP) || in.isKeyDown(Input.KEY_W)){
				character.moveN();
				movementTimer = MOVEMENT_DELAY;
			}else if(in.isKeyDown(Input.KEY_DOWN) || in.isKeyDown(Input.KEY_S)){
				character.moveS();
				movementTimer = MOVEMENT_DELAY;
			}else if(in.isKeyDown(Input.KEY_LEFT) || in.isKeyDown(Input.KEY_A)){
				character.moveW();
				movementTimer = MOVEMENT_DELAY;
			}else if(in.isKeyDown(Input.KEY_RIGHT) || in.isKeyDown(Input.KEY_D)){
				character.moveE();
				movementTimer = MOVEMENT_DELAY;
			}
			if(in.isKeyPressed(Input.KEY_E)){
				character.attemptRoomChange();
				character.tryTalk();
			}
		}
		movementTimer--;
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
	
	public boolean isCharacter(Character c){
		if(this.character.equals(c)){
			return true;
		}else{
			return false;
		}
	}
	
}
