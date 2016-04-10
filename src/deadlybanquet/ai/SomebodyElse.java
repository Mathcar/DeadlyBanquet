package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Represents the idea that I think with howsure certainty that aboutPerson thinks... 
 * Opinion is my opinion, not that of aboutPerson
 * @author omega
 */
public class SomebodyElse implements IThought, Comparable<IThought>{
    public IThought what;
    //TODO: If you find yourself copy-pasting this more than once, make an abstract superclass
    public String aboutPerson;
    //Remember that opinion is the opinion of whoever owns the IThought, not that of aboutPerson!
    //AboutPerson's own opinion can be found inside the what.
    public PAD opinion;
    //TODO restrict to smaller range
    public double howsure;
    public Time time;
    public SomebodyElse previous;
    public SomebodyElse(IThought w, String a, PAD o, double h){
        what=w;
        aboutPerson =a;
        opinion = o;
        howsure=h;
    }
    
    
    @Override
    public String toString(){
        return ("think with " + howsure + " certainty and " + opinion + "opinion that " + aboutPerson + " thinks that " + what);
    }
    
    @Override
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        SomebodyElse d = (SomebodyElse) i;
        if (d.aboutPerson!=""&&d.aboutPerson!=aboutPerson) return false;
        return what.contains(d.what);
    }

    @Override
    public void setPlaceHolderToNull() {
        what.setPlaceHolderToNull();
        if (aboutPerson=="") aboutPerson=null;
        if (opinion.isPlaceholder()) opinion=null;
    }

    @Override
    public double getCertainty() {
        return howsure;
    }
    
    public int compareTo(IThought i){
        return 1;
    }
}
