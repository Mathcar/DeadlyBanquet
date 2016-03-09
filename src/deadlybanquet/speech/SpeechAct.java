package deadlybanquet.speech;

import java.util.List;
import java.util.Map;
import deadlybanquet.ai.IThought;
import java.util.ArrayList;

/**
 * Created by Tom on 2016-02-25.
 */
public interface SpeechAct {

    public String getText();
    
    //gets the information content of the speech act.
    public ArrayList<IThought> getContent();
    
    //Gets the identifier of the speaker.
    public String getSpeaker();

    public boolean isEndConversation();

    public Map<String,Integer> getOpinionChanges();

    public List<String> getProperties();

}
