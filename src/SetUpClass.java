
import org.newdawn.slick.AppGameContainer;
import states.*;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;


public class SetUpClass extends BasicGameState {

	private TiledMap map1, map2;
	private int roomNum = 1;
	// A*pathfinding testing variables-------------
	private AStarPathFinder pathFinder;
	private LayerBasedTileMap pathfindingMap;
	private int currentX = 0;
	private int currentY = 0;
	private int currentLayer = 0;
	private NPCMover testNPC;
	
	private Image player;
	private int playerx = 100;
	private int playery = 100;
	private Boolean playerDown;
	private Boolean playerUp;
	private Boolean playerLeft;
	private Boolean playerRight;
	private Boolean leftStop;
	private Boolean rightStop;
	private Boolean upStop;
	private Boolean downStop;
	//-------------------------------------------

	public SetUpClass() {
		super();
	}

//<<<<<<< HEAD
//	public void render(GameContainer container, Graphics arg1) throws SlickException {
//=======

	public void render(GameContainer container, StateBasedGame s,Graphics arg1) throws SlickException {
//>>>>>>> fa02dda0394f532fc64d37fc0e0ce0477c95c778
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
		
		
		player.getScaledCopy(32,32).draw(playerx,playery);
		
		

        //----------PATHFINDING TEST RENDERS------------------------
        //drawGrid(arg1);
		//arg1.drawString("X=" + currentX + "   Y=" + currentY + "    Layer=" + currentLayer + "     ID =" + map1.getTileId(currentX,currentY,currentLayer), 100,100);
        arg1.drawOval(testNPC.x, testNPC.y, 20,20);
        //---------------------------------------------------------
	}

	public void init(GameContainer container, StateBasedGame s) throws SlickException {
		map1 = new TiledMap("res/pictures/living_room2.tmx");
		map2 = new TiledMap("res/pictures/kitchen.tmx");
		player = new Image("res/pictures/images.jpg");
        //Pathfinding test inits-----------------------------
		pathfindingMap = new LayerBasedTileMap(map1);
		pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		testNPC = new NPCMover(64,64);
        //----------------------------------------------------
		
		

	}

	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		testNPC.update();

		Input input = container.getInput();

		if (playerx / 32 == 5 && playery / 32 == 2 || (input.isKeyPressed(Input.KEY_1))) {
			playerx = 9 * 32;
			playery = 3 * 32;
			roomNum = 2;
		} else if (playerx / 32 == 9 && playery / 32 == 2 || input.isKeyPressed(Input.KEY_2)) {
			playerx = 5 * 32;
			playery = 3 * 32;
			roomNum = 1;
		} else if (input.isKeyPressed(Input.KEY_P) || input.isKeyPressed(Input.KEY_ESCAPE)) {
			input.clearKeyPressedRecord();
			s.enterState(States.pause);
		} else if(input.isKeyPressed(Input.KEY_T)) {
			//input.clearKeyPressedRecord();
			s.enterState(States.talk);
		}
    	
    	//Code for moving a small image on the map
        playerDown = input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN);
        playerUp = input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP);
        playerLeft = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
        playerRight = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);
        
        leftStop = pathfindingMap.isBlocked((playerx-1)/32,playery/32);
    	rightStop = pathfindingMap.isBlocked((playerx+33)/32,playery/32);
    	upStop = pathfindingMap.isBlocked((playerx+16)/32,(playery-1)/32);
    	downStop = pathfindingMap.isBlocked((playerx+16)/32,(playery+33)/32);
        
		if (playerRight && playerLeft){
			playerx += 0;
		}else if (playerUp && playerDown){
			playery += 0;
		}else if (playerDown && playerLeft && !downStop && !leftStop){
			playery += 1;
			playerx -= 1;
		}
		else if (playerDown && playerRight && !downStop && !rightStop){
			playery += 1;
			playerx += 1;
		}
		else if (playerUp && playerLeft && !upStop && !leftStop){
			playery -= 1;
			playerx -= 1;
		}
		else if (playerUp && playerRight && !upStop && !rightStop){
			playery -= 1;
			playerx += 1;
		}else if (playerDown && !downStop){
			playery += 1;
		}else if (playerLeft && !leftStop){
			playerx -= 1;
		}else if (playerUp && !upStop){
			playery -= 1;
		}else if (playerRight && !rightStop){
			playerx += 1;
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


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.game;
	}


}
