package states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Menu extends BasicGameState{

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame arg1, Graphics g) throws SlickException {
		g.drawString("1. Start Game",100,60);
		g.drawString("2. Quit Game",100,100);
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int arg2) throws SlickException {
		if(gc.getInput().isKeyPressed(Input.KEY_1)){
			gc.getInput().clearKeyPressedRecord();
			s.enterState(States.game);
		}else if(gc.getInput().isKeyPressed(Input.KEY_2)){
			gc.exit();
			
		}
		
	}

	@Override
	public int getID() {
		return States.menu;
	}

}
