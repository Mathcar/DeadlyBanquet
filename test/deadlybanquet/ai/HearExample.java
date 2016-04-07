package deadlybanquet.ai;

import static deadlybanquet.ai.BackStory.*;
import static deadlybanquet.ai.BrainFactory.makeBrain;
import deadlybanquet.speech.GreetingPhrase;
import deadlybanquet.speech.SpeechAct;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author omega
 */
public class HearExample {
    
    public HearExample() {
    }

    @Test
    public void hello() {
        SpeechAct input = new GreetingPhrase();
        
        ArrayList<IThought> content = new ArrayList<>();
        content.add(new Opinion("Bill", new PAD(1,1,1)));
        input.setContent(content);
        Brain npc = makeBrain(null,null,null, null, null, null, "Hell", "Jane");
        npc.hear(input);
        npc.hear(input);
        content.clear();
        content.add(SNOWEDIN);
        npc.hear(input);
        npc.hear(input);
    }
}
