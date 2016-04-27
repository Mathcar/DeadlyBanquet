package deadlybanquet.states;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import deadlybanquet.model.World;


public class Menu extends BasicGameState{

	private World model;
	private Image image;
	private TextField text1;
	
	public Menu(World world) {
		model = world;
		}
	
	@Override
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		image = new Image("res/pictures/lookingdown.png");
		text1 = new TextField(gc, gc.getDefaultFont(), 250, 300 ,96,20);
		text1.setBackgroundColor(Color.white);
		text1.setAcceptingInput(true);
		text1.setTextColor(Color.black);
		text1.setMaxLength(10);
		text1.setText("ChooseName");
	}

	@Override
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		g.drawString("1. Start Game",100,60);
		g.drawString("2. Quit Game",100,100);
		image.draw(250,200,3);
		text1.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame s, int delta) throws SlickException {
		if(text1.hasFocus() && gc.getInput().isKeyPressed(Input.KEY_ENTER)){
			text1.setFocus(false);
		}
		
		if(!text1.hasFocus()){
			if(gc.getInput().isKeyPressed(Input.KEY_1)){
				gc.getInput().clearKeyPressedRecord();
				model.getPlayer().getCharacter().setName(text1.getText());
				model.getPlayerBrain().setName(text1.getText());
				s.enterState(States.game);
			}else if(gc.getInput().isKeyPressed(Input.KEY_2)){
				gc.exit();
				
			}
		}
		gc.getInput().clearKeyPressedRecord();
		
	}

	@Override
	public int getID() {
		return States.menu;
	}

}
