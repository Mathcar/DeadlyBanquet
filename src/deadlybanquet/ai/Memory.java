package deadlybanquet.ai;

import deadlybanquet.model.Debug;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author omega
 */
public class Memory {
    //This contains all information which the player has got, sorted in order of how sertenty of the thought.
    public SortedList information = new SortedList();
    
    /**
     * 
     * @param information sorted set of initial information.
     * @param //room
     */
    public Memory(SortedList information){
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
    public SortedList find(IThought thought){
        if(thought==null){
//        	return new TreeSet<IThought>();
        	throw new NullPointerException();
        }
        SortedList results = new SortedList();
        for (IThought i: this.information){
            if (i.contains(thought)) results.add(i);
        }
        return results;
    }

    //
    public ArrayList<Whereabouts> getAllWhereabouts(){
        ArrayList<Whereabouts> temp=new ArrayList<>();
        for (IThought i : information) {
            if(i instanceof Whereabouts){
                temp.add((Whereabouts) i);
            }
        }
        return temp;
    }
    
    public void add(IThought i){
        information.add(i);
    }
    
    public void replace(IThought old, IThought replacement){
        information.remove(old);
        information.add(replacement);
    }
    
    public void debugPrintMemory(){
        String temp = "========= DEBUG ===========";
    	for(IThought i:information){
    		temp +="\n" +  i.debugMessage();
    	}
    	temp+= "\n========== END ============";
        Debug.printDebugMessage(temp, Debug.Channel.BRAIN);
    }
}
