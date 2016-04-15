package deadlybanquet.model;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import deadlybanquet.Talkable;
import deadlybanquet.ai.PlayerBrain;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import deadlybanquet.states.States;

public class Player implements Talkable {

	
	private Character character;
	private PlayerBrain playerBrain;
	
	private final static int MOVEMENT_DELAY = 32;
	private int movementTimer = MOVEMENT_DELAY;
	
	public Player(ActionListener al, PlayerBrain brain){
		this.character = new Character(al, "Gandalf", 5, 5);
		playerBrain = brain;
	}
	
	public Player(Character c, PlayerBrain brain){
		this.character = c;
		playerBrain = brain;
	}
	
	public Character getCharacter(){
		return this.character;
	}

	//Changed parameters so that ALL update methods are of the same structure


	public void update(World world, GameContainer container, StateBasedGame s, int dt){

		Input in = container.getInput();
		if(this.movementTimer < 1){
			if(in.isKeyDown(Input.KEY_UP) || in.isKeyDown(Input.KEY_W)){
				world.attemptMove(character, Direction.NORTH);
				movementTimer = MOVEMENT_DELAY;
			}else if(in.isKeyDown(Input.KEY_DOWN) || in.isKeyDown(Input.KEY_S)){
				world.attemptMove(character, Direction.SOUTH);
				movementTimer = MOVEMENT_DELAY;
			}else if(in.isKeyDown(Input.KEY_LEFT) || in.isKeyDown(Input.KEY_A)){
				world.attemptMove(character, Direction.WEST);
				movementTimer = MOVEMENT_DELAY;
			}else if(in.isKeyDown(Input.KEY_RIGHT) || in.isKeyDown(Input.KEY_D)){
				world.attemptMove(character, Direction.EAST);
				movementTimer = MOVEMENT_DELAY;
			}
			if(in.isKeyPressed(Input.KEY_E)){
				character.attemptRoomChange();
				world.attemptTalk(this.getCharacter());
				//character.attemptRoomChange();
				//character.tryTalk();

			}
		}
		movementTimer--;
	}

	public PlayerBrain getBrain(){
		return playerBrain;
	}

	//Called on every person in origin and destination rooms on room change.
	public void observeRoomChange(String who, String origin, String destination){
		//TODO IMPLEMENT
		playerBrain.observeRoomChange(who,origin,destination);
	}

	//called on entering a room
	public void seePeople (ArrayList<String> people){
		playerBrain.seePeople(people);
		System.out.println(getName() + " sees these people upon entering the room: ");
		for(String s : people){
			System.out.println(s);
		}
		//TODO IMPLEMENT
	}

	public void observeInteraction(String who, String with){
		playerBrain.observeInteraction(who,with);
		//TODO IMPLEMENT
	}
	
	public boolean isCharacter(Character c){
		if(this.character.equals(c)){
			return true;
		}else{
			return false;
		}
	}

	public String getName(){
		return character.getName();
	}
	
}
