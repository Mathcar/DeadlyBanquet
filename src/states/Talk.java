package states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Talk extends BasicGameState {

	private int answer;

	/*
	Some comments and questions about this Class // Tom

	Could this class have two characters? so we know whom are talking?
	Cuz if we can do that, we can quite easily send SpeechActs from each Character.
	This would mean that instead of switching to the "Talk" State, we much switch
	to a new Talk, Don't know if that is posible. The other alternative would be
	to have a dialogue class that the "Talk" state are "talking" to.
	 */

	public void init(GameContainer gc, StateBasedGame s) throws SlickException {

	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		g.setBackground(Color.white);
		g.setColor(Color.black);
		
		if(answer == 1){
			g.drawString("NPC: I'm great, but I'm late for a meeting.",100,60);
			g.drawString("Player: \n 2. Ok! Good bye! ",100,100);
		}else if(answer == 2){
			g.drawString("NPC: Talk to you later!",100,60);
			g.drawString("Press 'e' to leave",100,100);
		}else{
			g.drawString("NPC: Hi!",100,60);	
			g.drawString("Player: \n 1. Hello! How are you? \n 2. Good bye! ",100,100);
		}
		
		
		
		
	}

	public void update(GameContainer gc, StateBasedGame s, int arg2) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_E)){
			gc.getInput().clearKeyPressedRecord();
			s.enterState(States.game);
		}

		if(gc.getInput().isKeyPressed(Input.KEY_1)){
			answer = 1;
		}else if(gc.getInput().isKeyPressed(Input.KEY_2)){
			answer = 2;
		}
		
		gc.getInput().clearKeyPressedRecord();
		
	}

	public int getID() {
		return States.talk;
	}

}

