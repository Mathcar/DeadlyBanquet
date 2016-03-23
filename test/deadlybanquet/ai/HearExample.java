package deadlybanquet.ai;

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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    @Test
    public void hello() {
        SpeechAct input = new GreetingPhrase();
        ArrayList<IThought> content = new ArrayList<>();
        content.add(new Opinion("Bill", new PAD(1,1,1)));
        input.setContent(content);
        Brain npc = makeBrain(null,null,null, null, null, null, "Hell");
        npc.hear(input);
    }
}
