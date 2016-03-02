package AI;

/**
 * This class is the memory of the NPC or player. 
 * Contains everything which the NPC and player have in common.
 * @author omega
 */
import java.util.*;
public class Memory {
    //The current subject's thoughts about others
    HashMap<String, ModelOfWorld> otherPeople = new HashMap<>();
    //Key is object identifier, Value is person or room identifier
    HashMap<String, String> objectPosition = new HashMap<>();
}
