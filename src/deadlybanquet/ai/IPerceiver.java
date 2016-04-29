package deadlybanquet.ai;

import deadlybanquet.speech.SpeechAct;
import java.util.ArrayList;

import deadlybanquet.speech.TextPropertyEnum;
import org.newdawn.slick.Image;
import org.w3c.dom.Text;

/**
 *
 * @author omega
 */
public interface IPerceiver {

    //This method responds to a speech act, returning an appropriate reply.
    SpeechAct hear(SpeechAct act);

    //Selects a phrase out of a list of possible ones. Used instead of hear when the perciever
    //has the initiative in the conversation
    SpeechAct selectPhrase(ArrayList<SpeechAct> acts);

    //Called on every person in the room when somebody strikes up a conversation with somebody
    //else or examines (but does not pick up) an object in the room. (Other than the person
    //with whom the person is talking, obviously)
    void observeInteraction(String who, String with);

    //Returns the TextProperty that should be used in conversation with person
    TextPropertyEnum chooseProperty(String person);

    //Called on every person in the room (apart from who) when somebody picks up an object.
    void observePickUp(String who, String what);

    //Called on every person in the room (apart from who) when somebody puts down an object.
    void observePutDown(String who, String what);

    //Called on every person in origin and destination rooms on room change.
    void observeRoomChange(String character, String origin, String destination);

    //called on entering a room
    void seePeople(ArrayList<String> people);

    boolean isPlayer();

    //Should return the speechact intended to be said when initiating a conversation
    //Only used on the ai
    SpeechAct getIntendedPhrase();
    
    Memory getMemory(); //todo please add this.

    String getName(); //todo please add this.
    
    public ArrayList<Whereabouts> getWhereabouts();
    public ArrayList<Opinion> getOpinions();
}
