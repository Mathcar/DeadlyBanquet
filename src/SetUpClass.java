
import org.newdawn.slick.Animation;
import org.newdawn.slick.AppGameContainer;
import states.*;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
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
	
	private Image playerdown,playerup,playerleft,playerright,playerup2,playerup3;
	private int playerx = 100;
	private int playery = 100;
	private Boolean playerDown;
	private Boolean playerUp;
	private Boolean playerLeft;
	private Boolean playerRight;
	private Boolean playerLookingDown = true;
	private Boolean playerLookingUp = false;
	private Boolean playerLookingLeft = false;
	private Boolean playerLookingRight = false;
	private Boolean leftStop;
	private Boolean rightStop;
	private Boolean upStop;
	private Boolean downStop;
	private SpriteSheet moveUpSheet;
	private Animation moveUpAni;
	private Boolean playerMovingUp;
	private SpriteSheet moveDownSheet;
	private Animation moveDownAni;
	private Boolean playerMovingDown;
	private SpriteSheet moveLeftSheet;
	private Animation moveLeftAni;
	private Boolean playerMovingLeft;
	private SpriteSheet moveRightSheet;
	private Animation moveRightAni;
	private Boolean playerMovingRight;
	
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
		
		
		
		
		if (playerLookingDown){
			if(playerMovingDown){
 	 			moveDownAni.draw(playerx,playery);
 	 			moveDownAni.setPingPong(true);
 			}else{
 				playerdown.draw(playerx,playery);
 			}
 		}else if (playerLookingLeft){
 			if(playerMovingLeft){
 	 			moveLeftAni.draw(playerx,playery);
 	 			moveLeftAni.setPingPong(true);
 			}else{
 				playerleft.draw(playerx,playery);
 			}
 		}else if (playerLookingUp){
 			if(playerMovingUp){
 	 			moveUpAni.draw(playerx,playery);
 	 			moveUpAni.setPingPong(true);
 			}else{
 				playerup.draw(playerx,playery);
 			}
 			
 		}else if (playerLookingRight){
 			if(playerMovingRight){
 	 			moveRightAni.draw(playerx,playery);
 	 			moveRightAni.setPingPong(true);
 			}else{
 				playerright.draw(playerx,playery);
 			}
 		}
		
	

        //----------PATHFINDING TEST RENDERS------------------------
        //drawGrid(arg1);
		//arg1.drawString("X=" + currentX + "   Y=" + currentY + "    Layer=" + currentLayer + "     ID =" + map1.getTileId(currentX,currentY,currentLayer), 100,100);
        arg1.drawOval(testNPC.x, testNPC.y, 20,20);
        //---------------------------------------------------------
	}

	public void init(GameContainer container, StateBasedGame s) throws SlickException {
		map1 = new TiledMap("res/pictures/living_room2.tmx");
		map2 = new TiledMap("res/pictures/kitchen.tmx");
		moveUpSheet = new SpriteSheet("res/pictures/upanimation.png",32,32);
		moveUpAni = new Animation(moveUpSheet,300);
		moveDownSheet = new SpriteSheet("res/pictures/downanimation.png",32,32);
		moveDownAni = new Animation(moveDownSheet,300);
		moveLeftSheet = new SpriteSheet("res/pictures/leftanimation.png",32,32);
		moveLeftAni = new Animation(moveLeftSheet,300);
		moveRightSheet = new SpriteSheet("res/pictures/rightanimation.png",32,32);
		moveRightAni = new Animation(moveRightSheet,300);
		
		playerdown = new Image("res/pictures/lookingdown.png");
		playerup = new Image("res/pictures/lookingup.png");
		playerleft = new Image("res/pictures/lookingleft.png");
		playerright = new Image("res/pictures/lookingright.png");
        //Pathfinding test inits-----------------------------
		pathfindingMap = new LayerBasedTileMap(map1);
		pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		testNPC = new NPCMover(64,64);
        //----------------------------------------------------
		
		

	}

	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		testNPC.update();


		Input input = container.getInput();
		if (playerx / 32 == 5 && playery / 32 == 2 && playerLookingUp && input.isKeyPressed(Input.KEY_E)|| (input.isKeyPressed(Input.KEY_1))) {
			playerx = 9 * 32;
			playery = 2 * 32;
			roomNum = 2;
			playerLookingDown = true;
			playerLookingUp = false;
		} else if (playerx / 32 == 9 && playery / 32 == 2 && playerLookingUp && input.isKeyPressed(Input.KEY_E)|| input.isKeyPressed(Input.KEY_2)) {
			playerx = 5 * 32;
			playery = 2 * 32;
			roomNum = 1;
			playerLookingDown = true;
			playerLookingUp = false;
		} else if (input.isKeyPressed(Input.KEY_P) || input.isKeyPressed(Input.KEY_ESCAPE)) {
			input.clearKeyPressedRecord();
			s.enterState(States.pause);
		} else if(input.isKeyPressed(Input.KEY_E)) {
			input.clearKeyPressedRecord();
			s.enterState(States.talk);
		}
		
		playerMovement(input, delta);
		
        //PATHFINDING TEST INPUTS-----------------------------------------
        if(input.isMousePressed(0)) {
            System.out.println("mouse is at " + input.getMouseX() / 32 + "   " + input.getMouseY() / 32);
            testNPC.addPath(pathFinder.findPath(testNPC, testNPC.x / 32, testNPC.y / 32, input.getMouseX() / 32, input.getMouseY() / 32));
            System.out.println("Path made");
        }
        //-----------------------------------------------------------------
        
        input.clearKeyPressedRecord();
	}


    //Draws a grid over the background TiledMap, currently requires origin of (0,0)
    public void drawGrid(Graphics g){
        for(int i = 0; i<pathfindingMap.getWidthInTiles(); i++){
            for(int j=0; j<pathfindingMap.getHeightInTiles();j++){
                g.drawRect(i*32,j*32,32,32);
            }
        }

    }
    
    public void playerMovement(Input input,int delta){
    	
    	playerDown = input.isKeyDown(Input.KEY_S) || input.isKeyDown(Input.KEY_DOWN);
        playerUp = input.isKeyDown(Input.KEY_W) || input.isKeyDown(Input.KEY_UP);
        playerLeft = input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT);
        playerRight = input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT);
        
        playerMovingUp = false;
        playerMovingDown = false;
        playerMovingLeft = false;
        playerMovingRight = false;
        
        leftStop = pathfindingMap.isBlocked((playerx-1)/32,playery/32);
     	rightStop = pathfindingMap.isBlocked((playerx+33)/32,playery/32);
     	upStop = pathfindingMap.isBlocked((playerx+16)/32,(playery-1)/32);
     	downStop = pathfindingMap.isBlocked((playerx+16)/32,(playery+33)/32);
     	

 		if (playerDown){
 			playerLookingDown = true;
 			playerLookingUp = false;
 			playerLookingLeft = false;
 			playerLookingRight = false;
 			playerMovingDown = true;
 			moveDownAni.update(delta);
 			if(!downStop){
 				playery += 1;
 			}
 		}else if (playerLeft){
 			playerLookingLeft = true;
 			playerLookingUp = false;
 			playerLookingDown = false;
 			playerLookingRight = false;
 			playerMovingLeft = true;
 			moveUpAni.update(delta);
 			if(!leftStop){
 				playerx -= 1;
 			}
 		}else if (playerUp){
 			playerLookingUp = true;
 			playerLookingDown = false;
 			playerLookingLeft = false;
 			playerLookingRight = false;
 			playerMovingUp = true;
 			moveUpAni.update(delta);
 			if(!upStop){
 				playery -= 1;
 			}
 		}else if (playerRight){
 			playerLookingRight = true;
 			playerLookingUp = false;
 			playerLookingDown = false;
 			playerLookingLeft = false;
 			playerMovingRight = true;
 			moveUpAni.update(delta);
 			
 			if(!rightStop){
 				playerx += 1;
 			}
 		}
    	
    }


	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.game;
	}


}
