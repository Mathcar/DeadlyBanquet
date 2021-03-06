package deadlybanquet.ai;

import static deadlybanquet.ai.BackStory.*;
import static deadlybanquet.ai.BrainFactory.makeBrain;
import static deadlybanquet.ai.Do.What.MURDER;
import deadlybanquet.model.World;
import static deadlybanquet.model.World.getTime;
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
    NPCBrain Jane;
    NPCBrain Bill;
    World world;
    public HearExample() {
    }

    @Before
    public void setUp(){
        input = new SpeechAct();
        content = new ArrayList<>();
        input.setContent(content);
        input.setSpeaker("Bill");
        Jane = makeBrain("Hell", "Jane");
        Bill = makeBrain("Hell", "Bill");
        world=new World();
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
        IThought starter;
        //starter = new Whereabouts("Jane", "");
        starter = new Whereabouts("Alice", "");
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
        //makeSpeechAct(content, "Bill");
        //Bill.plantFalseOpinion((Opinion)starter);
        //Bill.plantFalseMemory(starter);
        Jane.observeRoomChange("Alice", "Bedroom", "kitchen");
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
    
    @Test
    public void testLongConversation(){
        ArrayList<IThought> input = new ArrayList<>();
        input.add(SNOWEDIN);
        input.add(NOMOBILESIGNAL);
        input.add(new Opinion("Alice", new PAD (1,1,1)));
        runConversation(Bill, Jane, input);
    }
    
    private void runConversation(NPCBrain first, NPCBrain second, ArrayList<IThought> ideas){
        content.add(ideas.get(0));
        //makeSpeechAct(content, "Bill");
        Bill.plantFalseMemory(ideas.get(0));
        ideas.remove(0);
        while (true){
            //Jane hears and puts her response into debugInfo.
            Jane.hear(input);
            content.clear();
            content.addAll(Jane.debugInfo);
            if (content.isEmpty()) {
                if (ideas.isEmpty())
                    return;
                Jane.plantFalseMemory(ideas.get(0));
                content.add(ideas.get(0));
                ideas.remove(0);
                //makeSpeechAct(content, "Jane");
            }
            //It is now Jane's turn to speak
            input.setSpeaker("Jane");
            Bill.hear(input);
            content.clear();
            content.addAll(Bill.debugInfo);
            if (content.isEmpty()) {
                if (ideas.isEmpty())
                    return;
                Bill.plantFalseMemory(ideas.get(0));
                content.add(ideas.get(0));
                ideas.remove(0);
                //makeSpeechAct(content, "Bill");
            }
            input.setSpeaker("Bill");
            //And now we are in the same state as we were before the loop starts for the first time.
            
        }
    }
}
