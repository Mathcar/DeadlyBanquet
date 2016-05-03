package deadlybanquet.states;

import deadlybanquet.ai.BeingPolite;
import org.newdawn.slick.Color;

import deadlybanquet.model.*;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class EndGame extends BasicGameState {
	
	private World model;
	private TextField text1;
	private boolean wonGame, lostGame = false;
	
	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		text1 = new TextField(gc, gc.getDefaultFont(), 250, 200 ,96,20);
		text1.setBackgroundColor(Color.white);
		text1.setAcceptingInput(true);
		text1.setTextColor(Color.black);
		text1.setMaxLength(10);
		text1.setText("");
	}
	
	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {

		if(wonGame == true){
			g.drawString("Congratulation you were right!",100,200);
			g.drawString("Press E to exit.",100,300);
		}
		else if(lostGame == true){
			g.drawString("You were wrong, game over!",100,200);
			g.drawString("Press E to exit.",100,300);
		}
		else{
			g.drawString("Write the name of the murderer and press enter",100,60);
			text1.render(gc, g);
		}
	}
	
	public void update(GameContainer gc, StateBasedGame s, int arg2) throws SlickException {
		if(!wonGame && !lostGame){
			if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
				System.out.println(text1.getText());
				if(text1.getText().equals("BURT")){
					wonGame = true;
				}
				else{
					lostGame = true;
				}
			}
		}
		else{
			if(gc.getInput().isKeyPressed(Input.KEY_E)){
				gc.exit();
			}
		}
	}
	
	public int getID() {
		return States.endgame;
	}
	
}
