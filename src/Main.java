import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import states.*;

public class Main extends StateBasedGame{

	public Main() {
		super("Deadly Banquet");
	}

	public static void main(String[] args) {
		
		AppGameContainer app;
		try {
			app = new AppGameContainer(new Main());
			
			app.setDisplayMode(640, 480, false);
			app.start(); 

			
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		gc.setShowFPS(true);
		gc.setAlwaysRender(true);
		gc.setVSync(true);
		gc.setMaximumLogicUpdateInterval(60);
		
		this.addState(new Menu());
		this.addState(new SetUpClass());
		this.addState(new Pause());
		
		
	}


}
