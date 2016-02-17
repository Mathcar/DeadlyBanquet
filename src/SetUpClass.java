
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SetUpClass extends BasicGame {

	private TiledMap map;
	
	public SetUpClass(String title) {
		super(title);
	}


	public void render(GameContainer container, Graphics arg1) throws SlickException {
		map.render(0,0);
		
	}

	public void init(GameContainer container) throws SlickException {
		map = new TiledMap("maps/living_room2.tmx"); 
	}

	public void update(GameContainer container, int delta) throws SlickException {

		
	}
	
	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new SetUpClass("SetUpTest"));
		
		app.setDisplayMode(640, 480, false);
		app.setShowFPS(true);
		app.setAlwaysRender(true);
		app.setVSync(true);
		app.setMaximumLogicUpdateInterval(60);
		app.start(); 
	}

}
