package deadlybanquet.ai;

import deadlybanquet.model.TimeStamp;

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
    public TimeStamp time;
    public SomebodyElse previous;
    
    public SomebodyElse(IThought w, String a, PAD o, double h, TimeStamp t, SomebodyElse p){
        what=w;
        aboutPerson =a;
        opinion = o;
        howsure=h;
        time=t;
        previous=p;
    }
    
    public SomebodyElse(IThought w, String a, PAD o, double h){
        this(w,a,o,h,null,null);
    }
    
    
    @Override
    public String toString(){
        return ("think with " + howsure + " certainty and " + opinion + "opinion that " + aboutPerson + " thinks that " + what);
    }
    
    @Override
    public boolean contains(IThought i) {
        if(i==null) return true;
        if(!this.getClass().equals(i.getClass())) return false;
        SomebodyElse d = (SomebodyElse) i;
        if (d.aboutPerson!=""&&d.aboutPerson!=aboutPerson) return false;
        if(what==null) return true;
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
    
    //Calculates a certainty for X in A said that B said that C said that X
    public double getModifiedCertainty(){
        return howsure*what.getCertainty();
    }
    
    @Override
    public int compareTo(IThought o) {
        if (getModifiedCertainty()<o.getCertainty()) return -1;
        else if (getModifiedCertainty()==o.getCertainty()) return 0;
        return 1;
    }

    @Override
    public boolean isQuestion() {
        return what.isQuestion() || aboutPerson=="" || opinion.isPlaceholder();
    }
    
    @Override
    public SomebodyElse copy(){
        TimeStamp t=time;
        if(t!=null)
            t=t.copy();
        PAD p =opinion;
        if(p!=null)
            p=p.copy();
        SomebodyElse s = previous;
        if(s!=null)
            s=s.copy();
        return new SomebodyElse(what, aboutPerson, p, howsure, t, s);
    }
    
    @Override
    public void setCertainty(double i){
        howsure=i;
    }

    @Override
    public Boolean dontKnow(){
        System.err.println("dont know method in this SomebodeElse class are NOT done");
        return false;
    }
}
