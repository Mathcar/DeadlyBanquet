package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Represents that somebody or something is/was somewhere (in a room)
 * @author omega
 */
public class Whereabouts implements IThought, Comparable<IThought>{
    public String room;
    //TODO: If you find yourself copy-pasting this more than once, make an abstract superclass
    public String character;
    public PAD opinion;
    public double certainty;
    public Time time;
    public Whereabouts previous;
    
    public Whereabouts (String character, String room){
        this(character,room,null,1.0,null);
    }
    
    
    
    public Whereabouts(String character, String room, PAD o, double cert,  Time time){
        this.character=character;
        this.room=room;
        opinion=o;
        certainty=cert;
        this.time=time;
    }
    
    @Override
    public String toString(){
        return character + " is with " + certainty + "certainty " + "in the " + room + "of which circumstance current person thinks" + opinion;
    }
    
    @Override
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        Whereabouts d = (Whereabouts) i;
        if (d.room!=""&&d.room!=room) return false;
        return !(d.character!=""&&d.character!=character);
    }

    @Override
    public void setPlaceHolderToNull() {
        if (room=="") room = null;
        if (character=="") character=null;
    }

    @Override
    public double getCertainty() {
        return certainty;
    }

    @Override
    public boolean isQuestion() {
        return room=="" || character=="";
    }

    @Override
    public int compareTo(IThought o) {
        if (getCertainty()<o.getCertainty()) return 1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return -1;
    }
}
