package deadlybanquet.ai;

import deadlybanquet.model.TimeStamp;

/**
 * Representation of a desire or goal.
 * @author omega
 */
public class Desire implements IThought{

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
    public TimeStamp time;
    
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
    
    public Desire(Desire.dg dg, IThought w, TimeStamp t, double s){
        desireorgoal=dg;
        what=w;
        time=t;
        strength=s;
    }
    
    @Override
    //It is not possible to get a match on strength.
    public boolean contains(IThought i) {
        if(i==null) return true;
        //The following line already throws a NullPointerException; no need to throw one explicitly.
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
    
    @Override
    public double getCertainty() {
        return strength;
    }

    @Override
    public boolean isQuestion() {
        return desireorgoal==dg.DGPLACEHOLDER || what.isQuestion();
    }

    @Override
    public int compareTo(IThought o) {
        if (getCertainty()<o.getCertainty()) return -1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return 1;
    }
    
    @Override
    public Desire copy(){
        TimeStamp t=time;
        if(t!=null)
            t=t.copy();
        
        return new Desire(desireorgoal, what.copy(), t, strength);
    }
    
    @Override
    public void setCertainty(double i){
        strength=i;
    }
}
