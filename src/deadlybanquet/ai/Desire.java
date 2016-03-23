package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Representation of a desire or goal.
 * @author omega
 */
public class Desire implements IThought{

    @Override
    //It is not possible to get a match on strength.
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        Desire d = (Desire) i;
        //Desire-goal mismatch
        if (d.desireorgoal!=dg.DGPLACEHOLDER&&desireorgoal!=d.desireorgoal)return false;
        //If we are looking for any object of this type regardless of content
        if (d.what==null) return true;
        return what.contains(d.what);
    }
    public enum dg {
        DESIRE, GOAL, DGPLACEHOLDER;
    }
    /**
     * Placeholder is null. There is no null value as that would render the whole
     * object useless.
     */
    public IThought what;
    
    public Time time;
    public Desire previous;
    
    /** This should lie between 0 and 1.
     *  Placeholder is a value larger than 1. Null is a negative value.
     */
    public double strength;
    //TODO order by strength;
    
    /**
     * Placeholder is DGPLACEHOLDER. Null is null.
     */
    public dg desireorgoal;
    @Override
    public String toString(){
        return desireorgoal + ": " + what + " with strength " + strength;
    }
}
