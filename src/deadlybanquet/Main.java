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
/*
		// TOMS TEST BELOW
		NPCBrain a = new NPCBrain(null,null,null,null,null,null,"Kitchen","A",null);
		NPCBrain b = new NPCBrain(null,null,null,null,null,null,"Kitchen","B",null);
		SpeechActFactory factory = new SpeechActFactory(a,b);
		//SpeechAct s =factory.convertIThoughtToSpeechAct(new Whereabouts("Hampus",""), TextPropertyEnum.NEUTRAL);
		//Opinion o = new Opinion("Tom",PAD.placeholderPAD());
		Opinion o = new Opinion("Tom",new PAD(0.0,1.0,-1.0));

		SpeechAct s =factory.convertIThoughtToSpeechAct(new Do(Do.What.MURDER,"Tom","Mathias",new Time()), TextPropertyEnum.PROPER);
		//SpeechAct s =factory.convertIThoughtToSpeechAct(new Whereabouts("Tom","Kitchen",null,1.0,null), TextPropertyEnum.COLLOQUIAL);
		//SpeechAct s = factory.convertIThoughtToSpeechAct(BeingPolite.GREET,TextPropertyEnum.PROPER);
		Debug.printDebugMessage("line: "+s.getLine());

*/

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
		
		this.addState(new Menu(model));
		this.addState(new Pause());
		this.addState(new Talk(model));
		this.addState(new GameWindow(model));
		

		
	}


}
