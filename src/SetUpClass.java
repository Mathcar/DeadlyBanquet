
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;

public class SetUpClass extends BasicGame {

	private TiledMap map1, map2;
	private int roomNum = 1;
	// A*pathfinding testing variables-------------
	private AStarPathFinder pathFinder;
	private LayerBasedTileMap pathfindingMap;
	private int currentX = 0;
	private int currentY = 0;
	private int currentLayer = 0;
	private NPCMover testNPC;
	//-------------------------------------------

	public SetUpClass(String title) {
		super(title);
	}


	public void render(GameContainer container, Graphics arg1) throws SlickException {
		if(roomNum == 1){
			map1.render(0, 0);
            pathfindingMap = new LayerBasedTileMap(map1);
            pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		}
		else if(roomNum == 2){
			map2.render(0, 0);
            pathfindingMap = new LayerBasedTileMap(map2);
            pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		}

        //----------PATHFINDING TEST RENDERS------------------------
        //drawGrid(arg1);
		//arg1.drawString("X=" + currentX + "   Y=" + currentY + "    Layer=" + currentLayer + "     ID =" + map1.getTileId(currentX,currentY,currentLayer), 100,100);
        arg1.drawOval(testNPC.x, testNPC.y, 20,20);
        //---------------------------------------------------------
	}

	public void init(GameContainer container) throws SlickException {
		map1 = new TiledMap("res/pictures/living_room2.tmx");
		map2 = new TiledMap("res/pictures/kitchen.tmx");

        //Pathfinding test inits-----------------------------
		pathfindingMap = new LayerBasedTileMap(map1);
		pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		testNPC = new NPCMover(64,64);
        //----------------------------------------------------

	}

	public void update(GameContainer container, int delta) throws SlickException {
		testNPC.update();

        Input input = container.getInput();
		if(input.isKeyPressed(Input.KEY_1)){
			roomNum = 1;
		}else if (input.isKeyPressed(Input.KEY_2)){
			roomNum = 2;
		}
        //PATHFINDING TEST INPUTS-----------------------------------------
        if(input.isMousePressed(0)) {
            System.out.println("mouse is at " + input.getMouseX() / 32 + "   " + input.getMouseY() / 32);
            testNPC.addPath(pathFinder.findPath(testNPC, testNPC.x / 32, testNPC.y / 32, input.getMouseX() / 32, input.getMouseY() / 32));
            System.out.println("Path made");
        }
        //-----------------------------------------------------------------
	}


    //Draws a grid over the background TiledMap, currently requires origin of (0,0)
    public void drawGrid(Graphics g){
        for(int i = 0; i<pathfindingMap.getWidthInTiles(); i++){
            for(int j=0; j<pathfindingMap.getHeightInTiles();j++){
                g.drawRect(i*32,j*32,32,32);
            }

        }

    }

}
