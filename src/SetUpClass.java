
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

public class SetUpClass extends BasicGame {

	private TiledMap map1, map2;
	private int roomNum = 1;
	
	public SetUpClass(String title) {
		super(title);
	}

	public void render(GameContainer container, Graphics arg1) throws SlickException {
		if(roomNum == 1){
			map1.render(0, 0);
		}
		else if(roomNum == 2){
			map2.render(0, 0);
		}
		
	}

	public void init(GameContainer container) throws SlickException {
		map1 = new TiledMap("res/pictures/living_room2.tmx");
		map2 = new TiledMap("res/pictures/kitchen.tmx");
	}

	public void update(GameContainer container, int delta) throws SlickException {
		Input input = container.getInput();
		if(input.isKeyPressed(Input.KEY_1)){
			roomNum = 1;
		}else if (input.isKeyPressed(Input.KEY_2)){
			roomNum = 2;
		}
		
	}

}
