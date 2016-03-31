package deadlybanquet;

import deadlybanquet.speech.SpeechAct;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by Hampus on 2016-03-04.
 * 
 * Things this class should be able to handle:
 * 
 * - AI wants to move to a location
 * - AI wants to interact with other character
 * - AI wants to send and receive SpeachActs
 * - Model wants AI to interpret SpeachActs
 * - AI wants to know how is in the same room
 * - AI wants to move to a character in the same room
 * 
 */
public class AI {

    public static void update(GameContainer container, StateBasedGame s, int deltaTime){
        //All update calls for AIs needs to be directed from here, like calls to the brain and such
    }
//Result indicates success
    //It might be simplest for these functions to just move one step in the right direction.
    //Although I made three stubs, it might work just fine with one function.
    public static boolean moveToCharacter(String name){
        return true;
    }

    public static boolean moveToRoom(String name){
return true;
    }
    
    public static boolean moveToObject(String name){
        return true;
    }
    
    //Tom says that in order for his code to work, hear (in Brain) must guarantee
    //to call this function, with an empty SpeechAct if necessary.
    //It might therefore be more convenient for hear to return the 
    //SpeechAct directly, instead of sending it here; the only thing is that
    //in that case one can no longer use the same type signature for player and NPC objects.
    public static boolean speak (SpeechAct speech){
  return true;
    }
    
    //pick up and put down an object, provided you are standing next to it.
    public static boolean pickUp(String object){
        return true;
    }
    
    public static boolean putDown(String object){
        return true;
    }
    
    public static boolean kill (String person){
        return true;
    }
    
}
