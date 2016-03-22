package deadlybanquet.ai;

/**
 * Represents the idea that I think with howsure certainty that aboutPerson thinks... 
 * Opinion is my opinion, not that of aboutPerson
 * @author omega
 */
public class SomebodyElse implements IThought{
    public IThought what;
    //TODO: If you find yourself copy-pasting this more than once, make an abstract superclass
    public String aboutPerson;
    public PAD opinion;
    //TODO restrict to smaller range
    public double howsure;
    //TODO time information;
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
}
