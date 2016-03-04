package deadlybanquet.AI;

/**
 *
 * @author omega
 * //This is the brain of an NPC.
 */
import java.util.*;
public class Brain extends Memory {
    //Current emotion - may be changed by events.
    private PAD emotion;
    //Personality. May not change. Emotion object regresses
    //to this in the absence of new stimuli.
    private PAD temperament;
    
    //Things the NPC wishes were true
    private ArrayList<Desire> desires= new ArrayList<>();
    //The NPC's goals. This list should remain sorted.
    private ArrayList<Desire> goals = new ArrayList<>();
    private ArrayList<Action> plan = new ArrayList<>();
}
