package deadlybanquet.speech;

import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 2016-02-25.
 */
public interface SpeechAct {

    public String getText();

    public boolean isEndConversation();

    public Map<String,Integer> getOpinionChanges();

    public List<String> getProperties();

}
