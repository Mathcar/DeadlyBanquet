package deadlybanquet.ai;

import static deadlybanquet.ai.BackStory.*;
import static deadlybanquet.ai.BrainFactory.makeBrain;
import static deadlybanquet.ai.Do.What.MURDER;
import static deadlybanquet.model.World.getTime;
import deadlybanquet.speech.GreetingPhrase;
import deadlybanquet.speech.SpeechAct;
import static deadlybanquet.speech.SpeechActFactory.makeSpeechAct;
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
    NPCBrain Jane;
    NPCBrain Bill;
    public HearExample() {
    }

    @Before
    public void setUp(){
        input = new GreetingPhrase();
        content = new ArrayList<>();
        input.setContent(content);
        input.setSpeaker("Bill");
        Jane = makeBrain("Hell", "Jane");
        Bill = makeBrain("Hell", "Bill");
    }
    @Test
    public void testOpinion() {
        content.add(new Opinion("Charley", new PAD(1,1,1)));
        Jane.hear(input);
        Jane.hear(input);
    }
    
    @Test
    public void testBackStory(){
        content.add(SNOWEDIN);
        Jane.hear(input);
        Jane.hear(input);
    }
    
    @Test
    public void testWhereabouts(){
        content.add(new Whereabouts("Jane", ""));
        Jane.hear(input);
        Jane.hear(input);
    }
    
    @Test
    public void testConversation(){
        Opinion starter;
        //starter = new Whereabouts("Jane", "");
        //starter = new Whereabouts("Alice", "Kitchen");
        starter = new Opinion("Alice", new PAD (1,1,1));
        //starter = new Opinion ("Alice", PAD.placeholderPAD());
        //starter = NOMOBILESIGNAL;
        //starter=new Do(MURDER,"Alice", "Bob", getTime());
        /*starter = new SomebodyElse(
                    new SomebodyElse(
                            new Do(MURDER,"Alice", "James", null), 
                            "Jane", null, 1.0), 
                    "Derek", null, 1.0);*/
        //We are maniplating Bill into saying this
        content.add(starter);
        makeSpeechAct(content, "Bill");
        //Bill.plantFalseOpinion((Opinion)starter);
        Bill.plantFalseMemory(starter);
        //Jane.plantFalseMemory(starter);
        while (true){
            //Jane hears and puts her response into debugInfo.
            Jane.hear(input);
            content.clear();
            content.addAll(Jane.debugInfo);
            if (content.isEmpty()) return;
            //It is now Jane's turn to speak
            input.setSpeaker("Jane");
            Bill.hear(input);
            content.clear();
            content.addAll(Bill.debugInfo);
            if (content.isEmpty()) return;
            input.setSpeaker("Bill");
            //And now we are in the same state as we were before the loop starts for the first time.
            
        }
    }
}
