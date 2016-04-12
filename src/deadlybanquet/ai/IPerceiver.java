package deadlybanquet.ai;

import deadlybanquet.speech.SpeechAct;
import java.util.ArrayList;

/**
 *
 * @author omega
 */
public interface IPerceiver {

    //This method responds to a speech act, returning an appropriate reply.
    void hear(SpeechAct act);

    //Called on every person in the room when somebody strikes up a conversation with somebody
    //else or examines (but does not pick up) an object in the room. (Other than the person
    //with whom the person is talking, obviously)
    void observeInteraction(String who, String with);

    //Called on every person in the room (apart from who) when somebody picks up an object.
    void observePickUp(String who, String what);

    //Called on every person in the room (apart from who) when somebody puts down an object.
    void observePutDown(String who, String what);

    //Called on every person in origin and destination rooms on room change.
    void observeRoomChange(String character, String origin, String destination);

    //called on entering a room
    void seePeople(ArrayList<String> people);

    //Memory getMemory(); //todo please add this.

    String getName(); //todo please add this.
    
}
