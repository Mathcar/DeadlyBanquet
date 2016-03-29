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
    public static boolean moveToCharacter(String name){
        return true;
    }

    public static boolean moveToRoom(String name){
return true;
    }
    
    public static boolean moveToObject(String name){
        return true;
    }
    
    public static boolean speak (SpeechAct speech){
  return true;
    }
    
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
