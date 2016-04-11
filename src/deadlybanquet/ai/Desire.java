package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Representation of a desire or goal.
 * @author omega
 */
public class Desire implements IThought{

    @Override
    public double getCertainty() {
        return strength;
    }

    @Override
    public boolean isQuestion() {
        return desireorgoal==dg.DGPLACEHOLDER || what.isQuestion();
    }

    @Override
    public int compareTo(IThought i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public enum dg {
        DESIRE, GOAL, DGPLACEHOLDER;
    }
    /**
     * Placeholder is null. There is no null value as that would render the whole
     * object useless.
     */
    public IThought what;
    
    /**
     * Null means always current. No placeholder (no way to ask when)
     */
    public Time time;
    
    /** This should lie between -1 and 1.
     *  Placeholder is a value larger than 1. Null is a value smaller than -1
     * A negative value implies that NPC wants this not to be the case.
     */
    public double strength;
    //TODO order by strength;
    
    /**
     * Placeholder is DGPLACEHOLDER. Null is null.
     */
    public dg desireorgoal;
    
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

    @Override
    public void setPlaceHolderToNull() {
        if (what==null)
            throw new RuntimeException("You are trying to set Desire.what to null");
        if (strength>1) strength = -strength;
        if (desireorgoal==dg.DGPLACEHOLDER) desireorgoal=null;
    }
    
    
    
    @Override
    public String toString(){
        return desireorgoal + ": " + what + " with strength " + strength;
    }
}
