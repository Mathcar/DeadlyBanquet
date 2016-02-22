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
		this.addState(new Talk());
		
		
	}

	/*
	This main is for tests of characters and opinion,
	Just leave this commented if you dont want to use it.
	 */
	/*
	public static void main(String[] args){
		Character c1=new Character("C1");
		Character c2=new Character("C2");
		System.out.println("C1 and c2 meet...");
		c1.meetNewCharacter(c2);
		c2.meetNewCharacter(c1);
		System.out.println("Both have \"love\" set to 30 as default.");
		System.out.println("c1 about c2: "+c1.getOpinion(c2).getLove());
		System.out.println("c2 says something rude to c1...");
		c1.getOpinion(c2).changeLove(-10);
		System.out.println("c1 about c2: "+c1.getOpinion(c2).getLove());
	}
	*/


}
