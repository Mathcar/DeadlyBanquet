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

    public void setText(String text);

    //gets the information content of the speech act.
    public ArrayList<IThought> getContent(); // return the info

    public void setContent(ArrayList<IThought> thoughts);

    public void setTextProperty(TextPropertyEnum property);

    public TextPropertyEnum getTextProperty();
    
    //Gets the identifier of the speaker.
    public String getSpeaker();

}
