package deadlybanquet.states;

import deadlybanquet.*;
import deadlybanquet.model.World;

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
	private View view;
	private World model;

	public GameWindow(World world) {
		model = world;
	}

	public void render(GameContainer container, StateBasedGame s,Graphics g) throws SlickException {
		view.drawRenderObjects(model.getRenderSet(),g);
	}

	public void init(GameContainer container, StateBasedGame s) throws SlickException {		
		view = new View(container.getHeight(), container.getWidth());
		
	}

	public void update(GameContainer container, StateBasedGame s, int delta) throws SlickException {
		/*testNPC.update();
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
        	
            Debug.printDebugMessage("mouse is at " + input.getMouseX() / 32 + "   " + input.getMouseY() / 32);
            testNPC.addPath(pathFinder.findPath(testNPC, testNPC.x / 32, testNPC.y / 32, input.getMouseX() / 32, input.getMouseY() / 32));
            Debug.printDebugMessage("Path made ");
        }
        
        //-----------------------------------------------------------------

        if(!pathfindingMap.isBlocked(testNPC.x/32, testNPC.y/32)){
        	pathfindingMap.updateBlockStatus(testNPC.x/32, testNPC.y/32, true);
        	pathfindingMap.updateBlockStatus(testNPC.prevTilex,testNPC.prevTiley , false);
        }
        input.clearKeyPressedRecord();
        --------------------------OLD CODE ABOVE----------------------------------*/

		//Update the model
		model.update(container, s, delta);
	}

	@Override
	public int getID() {
		// TODO Auto-generated method stub
		return States.game;
	}
}
