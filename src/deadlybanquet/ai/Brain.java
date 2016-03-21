package deadlybanquet.ai;

/**
 *
 * @author omega
 * //This is the brain of an NPC. It contains information about the NPC which the AI needs
 * to operate on frequently in a more easily accessible form.
 * There should be nothing in this file which could not technically be expressed as a list
 * of IThought objects.
 */
import deadlybanquet.speech.SpeechAct;
import java.util.*;
public class Brain extends Memory {
    //Current emotion - may be changed by events.
    private String currentRoom;
    private PAD emotion = new PAD(0,0,0);
    //Personality. May not change. Emotion object regresses
    //to this in the absence of new stimuli.
    private PAD temperament= new PAD(0,0,0);   
    //Things the NPC wishes were true
    private ArrayList<Desire> desires = new ArrayList<>();
    //The NPC's goals. This list should remain sorted.
    private ArrayList<Desire> goals = new ArrayList<>();
    //This uses plan elements rather than actions in order to facilitate reevaluation of the plan.
    private ArrayList<PlanElement> plan = new ArrayList<>();
    private ArrayList<Opinion> opinions = new ArrayList<>(); //TODO add this to constructor
    
    //this constructor replaces any bad values by defaults.
    public Brain(   ArrayList<IThought> information, 
                    PAD emotion, 
                    PAD temperament, 
                    ArrayList<Desire>desires,
                    ArrayList<Desire>goals,
                    ArrayList<PlanElement>plan,
                    String currentRoom){
        super(information, currentRoom);
        if (emotion!=null) this.emotion=emotion;
        if (temperament!=null) this.temperament=temperament;
        if (desires !=null) this.desires = desires;
        if (goals!=null) this.goals = goals;
        if (plan!=null) this.plan = plan;
    }
    
    @Override
    //This class evaluates the content, changing both opinions and information
    //This method is also responsible for sending answers.
    public void hear(SpeechAct act){
        ArrayList<IThought> content = act.getContent();
        for (IThought t : content){
            switch(t.getClass().getSimpleName()){
                case "Opinion":
                case "SomebodyElse":
                case "BackStory": System.out.println("switch statement works.");
                case "Whereabouts":
                case "Plan":
                case "PlanElement":
                case "EmotionThought":
                case "Principle":
                case "Desire":
                case "Rule":
                default: System.out.println("You missed a case.");
            }
        }
    }
    
    //selects which statement to respond to in the case that an incoming speech act
    //contains several pieces of information
    private ArrayList<IThought> selectResponse(ArrayList<IThought>[] possibleResponses){
        return null;
    }
    
    @Override
    public void observeRoomChange(String person, String origin, String destination){
        
    }
    
    @Override
    public void observeInteraction(String who, String with){
        
    }
    
    @Override
    public void observePickUp(String who, String what){
        
    }
    //Character does some thinking, cooling down, executing plan etc.
    public void update(){
        
    }
    //this method creates plans for any goals and puts
    private void plan(){
        
    }
}
