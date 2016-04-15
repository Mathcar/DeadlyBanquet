package deadlybanquet.ai;

/**
 * This class is the memory of the NPC or player. 
 * Contains everything which the NPC and player have in common.
 * @author omega
 */
import deadlybanquet.speech.SpeechAct;
import java.util.*;
public class PlayerBrain implements IPerceiver {
    Memory information;
    //TODO CONSTRUCTOR ONLY TEMPORARILY ADDED, NOT SURE IF EMPTY MEMORY IS SUPPPOSED TO BE MADE
    public PlayerBrain(){information = new Memory(new SortedList());}
    public PlayerBrain(SortedList s){
        information = new Memory(s);
    }
    
    //this method accepts a speech act and adds the information to its memory.
    //This method should not evaluate memory as that is the (human) player's or brains job.
    @Override
    public void hear(SpeechAct act){
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
		// TODO Auto-generated method stub
		return null;
	}
   
}
