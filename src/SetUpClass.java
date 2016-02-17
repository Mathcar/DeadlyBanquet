
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
		map = new TiledMap("res/pictures/living_room2.tmx");
	}

	public void update(GameContainer container, int delta) throws SlickException {

		
	}

}
