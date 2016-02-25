package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Talk extends BasicGameState {


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
		g.drawString("Hej",100,60);
		g.drawString("Hejhej",100,100);
		
	}

	public void update(GameContainer gc, StateBasedGame s, int arg2) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_E)){
			gc.getInput().clearKeyPressedRecord();
			s.enterState(States.game);
			
		}
		
	}

	public int getID() {
		return States.talk;
	}

}

