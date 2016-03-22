package deadlybanquet.ai;

/**
 * Represents that somebody or something is/was somewhere (in a room)
 * @author omega
 */
public class Whereabouts implements IThought{
    public String where;
    //TODO: If you find yourself copy-pasting this more than once, make an abstract superclass
    public String whoorwhat;
    public PAD opinion;
    public double howsure;
     //TODO time information;
    @Override
    public String toString(){
        return whoorwhat + " is with " + howsure + "certainty " + "in the " + where + "of which circumstance current person thinks" + opinion;
    }
    
    @Override
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        Whereabouts d = (Whereabouts) i;
        if (d.where!=""&&d.where!=where) return false;
        return !(d.whoorwhat!=""&&d.whoorwhat!=whoorwhat);
    }
}
