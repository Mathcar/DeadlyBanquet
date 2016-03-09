package deadlybanquet.ai;

/**
 * This class is the memory of the NPC or player. 
 * Contains everything which the NPC and player have in common.
 * @author omega
 */
import deadlybanquet.speech.SpeechAct;
import java.util.*;
public class Memory {
    //This contains all information which the player has got, in random order.
    //If performance becomes too bad, may have to invent better data structure.
    ArrayList<IThought> information = new ArrayList<>();
    public Memory(ArrayList<IThought> information){
        //if a null object is supplied, object will be initialized with empty list.
        if (information!=null)
            this.information=information;
    }
    //this method accepts a speech act and adds the information to its memory.
    //This method should not evaluate memory as that is the (human) player's job.
    public void hear(SpeechAct act){
        
    }
   
}
