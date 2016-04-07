package deadlybanquet.ai;

/**
 * This class is the memory of the NPC or player. 
 * Contains everything which the NPC and player have in common.
 * @author omega
 */
import deadlybanquet.speech.SpeechAct;
import java.util.*;
public class Memory {
    //This contains all information which the player has got, sorted in order of how sertenty of the thought.
    public SortedSet<IThought> information = new TreeSet<>();
    
    /**
     * 
     * @param information sorted set of initial information.
     * @param room
     */
    public Memory(SortedSet<IThought> information){
        //if a null object is supplied, object will be initialized with empty TreeSet.
        if (information!=null)
            this.information=information;
    }
    /**
     * 
     * @param thought - what to find. Describes what it expects to find, including placeholders.
     * Null values are taken to mean "any value" TODO or is it the other way around?
     * This function, and the contain functions, are probably full of bugs.
     * @return 
     */
    public SortedSet<IThought> find(IThought thought){
        if(thought==null){
//        	return new TreeSet<IThought>();
        	throw new NullPointerException();
        }
        SortedSet<IThought> results = new TreeSet<IThought>();
        for (IThought i: this.information){
            if (i.contains(thought)) results.add(i);
        }
        return results;
    }
    
    
    //this method accepts a speech act and adds the information to its memory.
    //This method should not evaluate memory as that is the (human) player's or brains job.
    public void hear(SpeechAct act){
    	for(IThought i: act.getContent()){
    		Opinion compOpinion = new Opinion(act.getSpeaker(), new PAD(0,0,0));
    		SortedSet<IThought> tempSet = this.find(compOpinion);
    		
    		if(tempSet.size()==0){//if the characters hven't met before
    			information.add(compOpinion);
    			tempSet.add(compOpinion);
    		}
    		Opinion speeker = (Opinion)tempSet.first(); //find should only return Opinions
    		
    		
    		this.information.add(new SomebodyElse(i, act.getSpeaker(), speeker.getPAD(), 1));
    	}
    }
    
    //Called on every person in origin and destination rooms on room change.
    public void observeRoomChange(String character, String origin, String destination){
        //how does Wherabouts work?
    }
    
    //Called on every person in the room when somebody strikes up a conversation with somebody
    //else or examines (but does not pick up) an object in the room. (Other than the person
    //with whom the person is talking, obviously)
    public void observeInteraction(String who, String with){
        //witch IThought should be generated?
    }
    
    //Called on every person in the room (apart from who) when somebody picks up an object.
    public void observePickUp(String who, String what){
        //It is not possible to pick up things
    }
    
    //Called on every person in the room (apart from who) when somebody puts down an object.
    public void observePutDown(String who, String what){
    	//It is not possible to put down things
    }
   
}
