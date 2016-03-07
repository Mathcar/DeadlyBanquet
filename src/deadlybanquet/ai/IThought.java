package deadlybanquet.ai;

/**
 * Marker interface for thoughts. It is meant to be empty.
 * A thought may for example be transmitted via SpeechActs, used as prerequisite or postcondition
 * or used to store data inside the AI if there is no more useful data structure available.
 * This is the basic unit of information. All information known to an AI should be 
 * possible to express using an IThought.
 * @author omega
 */
public interface IThought {
    
}
