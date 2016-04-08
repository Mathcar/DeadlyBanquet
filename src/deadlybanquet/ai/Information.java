package deadlybanquet.ai;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 *
 * @author omega
 */
public class Information {
    //This contains all information which the player has got, sorted in order of how sertenty of the thought.
    public SortedSet<IThought> information = new TreeSet<>();
    
    /**
     * 
     * @param information sorted set of initial information.
     * @param room
     */
    public Information(SortedSet<IThought> information){
        //if a null object is supplied, object will be initialized with empty TreeSet.
        if (information!=null)
            this.information=information;
    }
    /**
     * 
     * @param thought - what to find. Describes what it expects to find, including placeholders.
     * Null values are taken to mean "any value" TODO or is it the other way around?
     * This function, and the contain functions, are probably full of bugs.
     * @return 
     */
    public SortedSet<IThought> find(IThought thought){
        if(thought==null){
//        	return new TreeSet<IThought>();
        	throw new NullPointerException();
        }
        SortedSet<IThought> results = new TreeSet<IThought>();
        for (IThought i: this.information){
            if (i.contains(thought)) results.add(i);
        }
        return results;
    }
    
    public void add(IThought i){
        information.add(i);
    }
}
