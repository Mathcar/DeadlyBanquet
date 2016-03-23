package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 *
 * @author omega
 */
public class Principle implements IThought{
    public PlanElement what;
    public PAD opinion;
    public Time time;
    public Principle previous;
    
    @Override
    public String toString(){
        return what +" is " + opinion;
    }
    
    @Override
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        Principle d = (Principle) i;
        return what.contains(d.what);
    }
}
