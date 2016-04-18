package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Represents that somebody or something is/was somewhere (in a room)
 * @author omega
 */
public class Whereabouts implements IThought{
    private String room;
    private String character;
    private PAD opinion;
    private double certainty;
    private Time time;


    private Whereabouts previous;
    
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
        if(i==null) return true;
        if(!this.getClass().equals(i.getClass())) return false;
        Whereabouts d = (Whereabouts) i;
        if (d.room!=""&&d.room!=room) return false;
        return (d.character==""||d.character==character);
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
        if (getCertainty()<o.getCertainty()) return -1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return 1;
    }

    public Time getTime() {
        return time;
    }

    public Whereabouts getPrevious() {
        return previous;
    }

    public PAD getOpinion() {
        return opinion;
    }

    public String getCharacter() {
        return character;
    }

    public String getRoom() {
        return room;
    }
    
    @Override
    public Whereabouts copy(){
        Time t=time;
        if(t!=null)
            t=t.copy();
        PAD p =opinion;
        if(p!=null)
            p=p.copy();
        return new Whereabouts(room, character, p, certainty, t);
    }
    
    @Override
    public void setCertainty(double i){
        certainty=i;
    }
    
    
}
