package deadlybanquet.states;

import deadlybanquet.*;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;


public class GameWindow extends BasicGameState {

	private TiledMap map1, map2, map3;
	private int roomNum = 1;
	// A*pathfinding testing variables-------------
	private AStarPathFinder pathFinder;
	private LayerBasedTileMap pathfindingMap;
	private int currentX = 0;
	private int currentY = 0;
	private int currentLayer = 0;
	private NPCMover testNPC;
	private Image npcImage;
	private OldPlayer thePlayer;

	public GameWindow() {
		super();
	}

	public void render(GameContainer container, StateBasedGame s,Graphics g) throws SlickException {
		

		if(roomNum == 1){
			map1.render(0, 0);
			npcImage.draw(testNPC.x, testNPC.y);
		}
		else if(roomNum == 2){
			map2.render(0, 0);
		}else if(roomNum == 3){
			map3.render(0, 0);
		}
		thePlayer.render();			//play the player animation
	}

	public void init(GameContainer container, StateBasedGame s) throws SlickException {
		map1 = new TiledMap("res/pictures/living_room2.tmx");
		map2 = new TiledMap("res/pictures/kitchen.tmx");
		map3 = new TiledMap("res/pictures/bedroom.tmx");
		thePlayer = new OldPlayer(); //create the player object
		
        //Pathfinding test inits-----------------------------
		npcImage = new Image("res/pictures/testNPC.png");    //ALSO MOVED
		pathfindingMap = new LayerBasedTileMap(map1);
		pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		testNPC = new NPCMover(9*32,9*32);
        //----------------------------------------------------
	}

	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		testNPC.update();
		Input input = container.getInput();
		thePlayer.update(input,delta,this);			//run the players own update method
		if (input.isKeyPressed(Input.KEY_P) || input.isKeyPressed(Input.KEY_ESCAPE)) {
			input.clearKeyPressedRecord();
			s.enterState(States.pause);
		} else if(input.isKeyPressed(Input.KEY_E) && thePlayer.nextToNPC(testNPC)) {
			input.clearKeyPressedRecord();
			s.enterState(States.talk);
		}


        //PATHFINDING TEST INPUTS-----------------------------------------
        if(input.isMousePressed(0)) {
        	
            System.out.println("mouse is at " + input.getMouseX() / 32 + "   " + input.getMouseY() / 32);
            testNPC.addPath(pathFinder.findPath(testNPC, testNPC.x / 32, testNPC.y / 32, input.getMouseX() / 32, input.getMouseY() / 32));
            System.out.println("Path made ");
        }
        
        //-----------------------------------------------------------------

        if(!pathfindingMap.isBlocked(testNPC.x/32, testNPC.y/32)){
        	pathfindingMap.updateBlockStatus(testNPC.x/32, testNPC.y/32, true);
        	pathfindingMap.updateBlockStatus(testNPC.prevTilex,testNPC.prevTiley , false);
        }
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


	//Needs to be updated when rooms are fully defined!!!
	public void swapRooms(int roomID){

		if(roomID == 2) {
			roomNum = 2;
			pathfindingMap = new LayerBasedTileMap(map2);
			pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		}else if(roomID == 1){
			roomNum=1;
			pathfindingMap = new LayerBasedTileMap(map1);
			pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		}
		else{
			roomNum=3;
			pathfindingMap = new LayerBasedTileMap(map3);
			pathFinder = new AStarPathFinder(pathfindingMap, 100, false);
		}
	}

	public LayerBasedTileMap getMap(){
		return pathfindingMap;
	}
	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.game;
	}


}
