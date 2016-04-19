package deadlybanquet.states;

import org.newdawn.slick.Color;

import deadlybanquet.ConversationModel;
import deadlybanquet.RenderSet;
import deadlybanquet.model.*;
import deadlybanquet.model.Character;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class Talk extends BasicGameState {

	private int answer;
	private TextField text1, text2;
	private World model;
	private ConversationModel conv;
	
	public Talk(World world) {
		model = world;

	}

	/*
	Some comments and questions about this Class // Tom

	Could this class have two characters? so we know whom are talking?
	Cuz if we can do that, we can quite easily send SpeechActs from each Character.
	This would mean that instead of switching to the "Talk" State, we much switch
	to a new Talk, Don't know if that is posible. The other alternative would be
	to have a dialogue class that the "Talk" state are "talking" to.
	 */


	public void init(GameContainer gc, StateBasedGame s) throws SlickException {
		text1 = new TextField(gc, gc.getDefaultFont(), 100, 30 ,510,140);
		text2 = new TextField(gc, gc.getDefaultFont(), 30, 310 ,510,140);
		text1.setAcceptingInput(false);
		text2.setAcceptingInput(false);

	}

	public void render(GameContainer gc, StateBasedGame s, Graphics g) throws SlickException {
		
		model.getPlayerConv().getPlayerImage().draw(540,330,3);
		model.getPlayerConv().getNpcImage().draw(0,50,3);
		g.setBackground(Color.white);
		g.setColor(Color.black);
		
		g.drawRect(100, 30, 510, 140);
		
		text1.setBorderColor(Color.black);
		text2.setBorderColor(Color.black);	
		text1.setTextColor(Color.blue);
		
		//g.drawString(model.getPlayerConv().getIPerceiverPlayer().getName(), 555, 430 );
		//g.drawString(model.getPlayerConv().getIperceiverNpc().getName(), 25, 150 );
		
		if(answer == 1){

			text1.setText("\n I'm great, but I'm late for a meeting.");

			text2.setText(" \n 2. Ok! Good bye! ");
	
		}else if(answer == 2){
			text1.setText("\n Talk to you later!");
			text2.setText("\n Press 'e' to leave");
		}else{
			text1.setText("\n Hi!");	
			text2.setText("\n 1. Hello! How are you? \n 2. Good bye! ");
		}
		
		text1.render(gc,g);
		text2.render(gc, g);
		
	}

	public void update(GameContainer gc, StateBasedGame s, int arg2) throws SlickException {
	
		if(gc.getInput().isKeyPressed(Input.KEY_E)){
			gc.getInput().clearKeyPressedRecord();
			s.enterState(States.game);
		}

		if(gc.getInput().isKeyPressed(Input.KEY_1)){
			answer = 1;
		}else if(gc.getInput().isKeyPressed(Input.KEY_2)){
			answer = 2;
		}
		
		gc.getInput().clearKeyPressedRecord();
		
	}

	public int getID() {
		return States.talk;
	}

}

