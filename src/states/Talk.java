package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Talk extends BasicGameState {

	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
	}

	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("Hej",100,60);
		g.drawString("Hejhej",100,100);
		
	}

	public void update(GameContainer gc, StateBasedGame s, int arg2) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_T)){
			gc.getInput().clearKeyPressedRecord();
			s.enterState(States.talk);
			
		}
		
	}

	public int getID() {
		return States.talk;
	}

}

