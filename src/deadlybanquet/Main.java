package deadlybanquet;

import deadlybanquet.ai.*;
import deadlybanquet.model.Time;
import deadlybanquet.model.World;
import deadlybanquet.speech.*;
import deadlybanquet.states.Menu;
import deadlybanquet.states.Pause;
import deadlybanquet.states.Talk;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import deadlybanquet.states.*;

public class Main extends StateBasedGame{

	public Main() {
		super("Deadly Banquet");
	}

	World model;
	public static void main(String[] args) {

		//create a speechActHolder class,that reads all the texts files and has a lot of lists
		//SpeechActHolder speechActHolder = SpeechActHolder.getInstance();
		//speechActHolder.createListFromFiles();
		/*
		Reading the text files here, maybe these should be moved
		 */
		SpeechActHolder.getInstance().readAllFile();

		// TOMS TEST BELOW
		/*NPCBrain a = new NPCBrain(null,null,null,null,null,null,"Kitchen","A");
		NPCBrain b = new NPCBrain(null,null,null,null,null,null,"Kitchen","B");
		SpeechActFactory factory = new SpeechActFactory(a,b);
		//SpeechAct s =factory.convertIThoughtToSpeechAct(new Whereabouts("Tom","Kitchen"), TextPropertyEnum.NEUTRAL);
		//SpeechAct s =factory.convertIThoughtToSpeechAct(new Opinion("Tom",new PAD(10.0,10.0,10.0),new Time(),new Opinion("Tom",new PAD(20,20,20))), TextPropertyEnum.PROPER);
		SpeechAct s =factory.convertIThoughtToSpeechAct(new Whereabouts("Tom","Kitchen"), TextPropertyEnum.COLLOQUIAL);
		System.out.println("line: "+s.getLine());*/


		AppGameContainer app;
		try {
			app = new AppGameContainer(new Main());
			
			app.setDisplayMode(640, 480, false);
			app.start(); 

			
		} catch (SlickException e) {
			e.printStackTrace();
			System.exit(0);
		}

		

	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		gc.setShowFPS(true);
		gc.setAlwaysRender(true);
		gc.setVSync(true);
		gc.setMaximumLogicUpdateInterval(60);
		
		model = new World();		
		
		this.addState(new Menu());
		this.addState(new Pause());
		this.addState(new Talk(model));
		this.addState(new GameWindow(model));
		

		
	}


}
