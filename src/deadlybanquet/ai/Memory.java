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
    public TreeSet<IThought> information = new TreeSet<>();
    
    public String currentRoom;
    public Memory(ArrayList<IThought> information, String room){
        //if a null object is supplied, object will be initialized with empty list.
        if (information!=null)
            this.information=information;
        if(room==null) throw new IllegalArgumentException("No room supplied for Memory constructor");
        currentRoom=room;
    }
    /**
     * 
     * @param what - what to find. Describes what it expects to find, including placeholders.
     * Null values are taken to mean "any value" TODO or is it the other way around?
     * This function, and the contain functions, are probably full of bugs.
     * @return 
     */
    public ArrayList<IThought> find (IThought what, ArrayList<IThought> where){
        if(what==null) throw new NullPointerException();
        ArrayList<IThought> results = new ArrayList<>();
        for (IThought i: where){
            if (i.contains(what)) results.add(i);
        }
        return results;
    }
    
    
    //this method accepts a speech act and adds the information to its memory.
    //This method should not evaluate memory as that is the (human) player's job.
    public void hear(SpeechAct act){
        
    }
    
    //Called on every person in origin and destination rooms on room change.
    public void observeRoomChange(String who, String origin, String destination){
        
    }
    
    //Called on every person in the room when somebody strikes up a conversation with somebody
    //else or examines (but does not pick up) an object in the room. (Other than the person
    //with whom the person is talking, obviously)
    public void observeInteraction(String who, String with){
        
    }
    
    //Called on every person in the room (apart from who) when somebody picks up an object.
    public void observePickUp(String who, String what){
        
    }
    
    //Called on every person in the room (apart from who) when somebody puts down an object.
    public void observePutDown(String who, String what){
        
    }
   
}
