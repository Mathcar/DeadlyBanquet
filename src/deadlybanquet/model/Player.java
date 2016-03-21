package deadlybanquet.model;

import java.awt.event.ActionListener;

import org.newdawn.slick.Input;

public class Player {
	
	private Character character;
	
	
	private final static int MOVEMNET_DELAY = 500;
	private int movmentTimer = 0;
	
	public Player(ActionListener al){
		this.character = new Character(al, "Gandalf", 5, 5);
	}
	
	public Player(Character c){
		this.character = c;
	}
	
	public Character getCharacter(){
		return this.character;
	}
	
	public void uppdate(Input in, int dt){
		if(this.movmentTimer < 1){
			if(in.isKeyPressed(Input.KEY_UP)){
				character.moveN();
				movmentTimer = MOVEMNET_DELAY;
			}else if(in.isKeyPressed(Input.KEY_DOWN)){
				character.moveS();
				movmentTimer = MOVEMNET_DELAY;
			}else if(in.isKeyPressed(Input.KEY_LEFT)){
				character.moveW();
				movmentTimer = MOVEMNET_DELAY;
			}else if(in.isKeyPressed(Input.KEY_RIGHT)){
				character.moveE();
				movmentTimer = MOVEMNET_DELAY;
			}
		}
		movmentTimer--;
	}
	
}
