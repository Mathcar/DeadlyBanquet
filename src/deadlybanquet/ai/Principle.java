package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * This is possible too advanced? We'll wait with this, maybe not do at all.
 * @author omega
 
public class Principle implements IThought{
    public IThought what;
    public PAD opinion;
    public Time time;
    public Principle previous;
    
    public Principle(PlanElement w, PAD o, Time t){
        what=w;
        opinion=o;
        time=t;
    }
    
    
    
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
*/