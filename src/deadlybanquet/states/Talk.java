package deadlybanquet.states;

import deadlybanquet.ai.BeingPolite;
import org.newdawn.slick.Color;

import deadlybanquet.model.ConversationModel;
import deadlybanquet.model.*;

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
	private ConversationTree convTree;

	private boolean isExitingState;
	
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
		if(isExitingState)
			return;				//State is being changed, so data no longer exists
		ConversationModel playerConv = model.getPlayerConv();
		playerConv.getPlayerImage().draw(540,330,3);
		playerConv.getNpcImage().draw(0,50,3);
		g.setBackground(Color.white);
		g.setColor(Color.black);
		
		g.drawRect(100, 30, 510, 140);
		
		text1.setBorderColor(Color.black);
		text2.setBorderColor(Color.black);	
		text1.setTextColor(Color.blue);
		
		//g.drawString(model.getPlayerConv().getIPerceiverPlayer().getName(), 555, 430 );
		//g.drawString(model.getPlayerConv().getIperceiverNpc().getName(), 25, 150 );
		text1.setText("\n " +conv.getLatestResponse());
		text2.setText("\n" + convTree.getPrint());

		text1.render(gc,g);
		text2.render(gc, g);
		
	}

	//Called upon entering this state
	@Override
	public void enter(GameContainer gc, StateBasedGame  game){
		conv = model.getPlayerConv();
		convTree = new ConversationTree(model, conv.getAllPropertyVariations(BeingPolite.GREET));
		isExitingState = false;
	}

	private void resetRunningData(){
		conv = null;
		convTree = null;
		isExitingState = true;
	}

	public void update(GameContainer gc, StateBasedGame s, int arg2) throws SlickException {

		if(gc.getInput().isKeyPressed(Input.KEY_E) || conv.isConversationOver()){
			gc.getInput().clearKeyPressedRecord();
			resetRunningData();
			s.enterState(States.game);
		}else {
			convTree.parseInputForConv(gc.getInput(), conv);
			if (convTree.isChoiceCompleted()) {
				conv.recieveChoice(convTree.getFinalChoice());
			}
			conv.runConversation();

			/*
			if(gc.getInput().isKeyPressed(Input.KEY_1)){
				answer = 1;
			}else if(gc.getInput().isKeyPressed(Input.KEY_2)){
				answer = 2;
			}
			*/
		}
		gc.getInput().clearKeyPressedRecord();
		
	}

	public int getID() {
		return States.talk;
	}

}

