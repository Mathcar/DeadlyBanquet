package deadlybanquet.ai;

import static deadlybanquet.ai.BackStory.*;
import static deadlybanquet.ai.BrainFactory.makeBrain;
import deadlybanquet.speech.GreetingPhrase;
import deadlybanquet.speech.SpeechAct;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author omega
 */
public class HearExample {
    SpeechAct input;
    ArrayList<IThought> content;
    NPCBrain npc;
    public HearExample() {
    }

    @Before
    public void setUp(){
        input = new GreetingPhrase();
        content = new ArrayList<>();
        input.setContent(content);
        input.setSpeaker("Bill");
        npc = makeBrain("Hell", "Jane");
    }
    @Test
    public void testOpinion() {
        content.add(new Opinion("Charley", new PAD(1,1,1)));
        npc.hear(input);
        npc.hear(input);
    }
    
    @Test
    public void testBackStory(){
        content.add(SNOWEDIN);
        npc.hear(input);
        npc.hear(input);
    }
    
    @Test
    public void testWhereabouts(){
        content.add(new Whereabouts("Jane", ""));
        npc.hear(input);
        npc.hear(input);
    }
}
