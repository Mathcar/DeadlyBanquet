package deadlybanquet.model;

import org.newdawn.slick.Input;

public class Player {
	
	private Character character;
	
	public Player(){
		
	}
	
	public void uppdate(Input in, int dt){
		if(in.isKeyPressed(Input.KEY_UP)){
			character.moveN();
		}else if(in.isKeyPressed(Input.KEY_DOWN)){
			character.moveS();
		}else if(in.isKeyPressed(Input.KEY_LEFT)){
			character.moveW();
		}else if(in.isKeyPressed(Input.KEY_RIGHT)){
			character.moveE();
		}
	}
	
}
