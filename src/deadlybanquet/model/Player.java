package deadlybanquet.model;

import org.newdawn.slick.Input;

public class Player {
	
	private Character character;
	
	private final static int MOVEMNET_DELAY = 500;
	private int movmentTimer = 0;
	
	public Player(){
		this.character = new Character("Gandalf", 5, 5);
	}
	
	public Player(Character c){
		this.character = c;
	}
	
	public void uppdate(Input in, int dt){
		if(this.movmentTimer < 1){
			if(in.isKeyPressed(Input.KEY_UP)){
				character.moveN();
				movmentTimer = MOVEMNET_DELAY;
			}else if(in.isKeyPressed(Input.KEY_DOWN)){
				character.moveS();
			}else if(in.isKeyPressed(Input.KEY_LEFT)){
				character.moveW();
			}else if(in.isKeyPressed(Input.KEY_RIGHT)){
				character.moveE();
			}
		}
	}
	
}
