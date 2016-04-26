package deadlybanquet.ai;

import deadlybanquet.model.World;
/**
 * This class is the memory of the NPC or player. 
 * Contains everything which the NPC and player have in common.
 * @author omega
 */
import deadlybanquet.speech.SpeechAct;
import java.util.*;

import deadlybanquet.speech.TextPropertyEnum;
import org.newdawn.slick.Image;
public class PlayerBrain implements IPerceiver {
    private Memory information;
    private World world;
    private String name;
    //TODO CONSTRUCTOR ONLY TEMPORARILY ADDED, NOT SURE IF EMPTY MEMORY IS SUPPPOSED TO BE MADE
    public PlayerBrain(String name){
        information = new Memory(new SortedList());
        this.name = name;
    }
    public PlayerBrain(SortedList s, World world){
        information = new Memory(s);
        this.world = world;
    }
    
    //this method accepts a speech act and adds the information to its memory.
    //This method should not evaluate memory as that is the (human) player's or brains job.
    @Override
    public SpeechAct hear(SpeechAct act){
    	for(IThought i: act.getContent()){
    		Opinion compOpinion = new Opinion(act.getSpeaker(), new PAD(0,0,0));
    		SortedList tempSet = information.find(compOpinion);
    		
    		if(tempSet.size()==0){//if the characters hven't met before
    			information.add(compOpinion);
    			tempSet.add(compOpinion);
    		}
    		Opinion speeker = (Opinion)tempSet.first(); //find should only return Opinions
    		
    		
    		this.information.add(new SomebodyElse(i, act.getSpeaker(), speeker.getPAD(), 1));
    	}
        //TODO Return something sensible
        return null;
    }
    
    //Called on every person in origin and destination rooms on room change.
    @Override
    public void observeRoomChange(String character, String origin, String destination){
        //how does Wherabouts work?
    }
    
    //Called on every person in the room when somebody strikes up a conversation with somebody
    //else or examines (but does not pick up) an object in the room. (Other than the person
    //with whom the person is talking, obviously)
    @Override
    public void observeInteraction(String who, String with){
        //witch IThought should be generated?
    }

    @Override
    public TextPropertyEnum chooseProperty(String person) {
        return TextPropertyEnum.PROPER;
    }

    //Called on every person in the room (apart from who) when somebody picks up an object.
    @Override
    public void observePickUp(String who, String what){
        //It is not possible to pick up things
    }
    
    //Called on every person in the room (apart from who) when somebody puts down an object.
    @Override
    public void observePutDown(String who, String what){
    	//It is not possible to put down things
    }
    
    //called on entering a room
    @Override
    public void seePeople (ArrayList<String> people){
        
    }

	@Override
	public String getName() {
		return name;
	}
    
    @Override
    public Memory getMemory(){
        return this.information;
    }
	@Override
	public SpeechAct selectPhrase(ArrayList<SpeechAct> acts) {
		// TODO Auto-generated method stub
		return null;
	}
   
}
