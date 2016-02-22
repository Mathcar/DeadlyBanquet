package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Pause extends BasicGameState{

	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		g.drawString("Pause",100,100);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_P)){
			gc.getInput().clearKeyPressedRecord();
			s.enterState(States.game);
		}else if(gc.getInput().isKeyPressed(Input.KEY_ESCAPE)){
			gc.getInput().clearKeyPressedRecord();
			s.enterState(States.menu);
		}
		
	}

	@Override
	public int getID() {
		return States.pause;
	}

}
