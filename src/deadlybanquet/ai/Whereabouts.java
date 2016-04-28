package deadlybanquet.ai;

import deadlybanquet.model.Debug;
import deadlybanquet.model.TimeStamp;

/**
 * Represents that somebody or something is/was somewhere (in a room)
 * @author omega
 */
public class Whereabouts implements IThought{
    private String room;
    private String character;
    private PAD opinion;
    private double certainty;
    private TimeStamp time;


    private Whereabouts previous;
    
    /**
     * 
     * @param character
     * @param room 
     */
    public Whereabouts (String character, String room){
        this(character,room,null,1.0,null, null);
    }
    
    public Whereabouts(String character, String room, double cert,  TimeStamp time){
        this(character,room,null,cert,time, null);
    }
    
    public Whereabouts(String character, String room, double cert,  TimeStamp time, Whereabouts p){
        this(character,room,null,cert,time, p);
    }
    
    public Whereabouts(String character, String room, PAD o, double cert,  TimeStamp time, Whereabouts previous){
        this.character=character;
        this.room=room;
        opinion=o;
        certainty=cert;
        this.time=time;
        this.previous=previous;
    }
    
    @Override
	public String debugMessage() {
		return "Whereoubouts:\n"
    			+ "Room: " + room + "\n"
    			+ "Character: " + character + "\n"
				+ "Opinion: " + opinion.toString() + "\n"
				+ "Certainty:" + certainty + "\n"
				+ "Time: " + time.getDay() + ":" + time.getHour() + ":" + time.getMinute() + "\n";
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

    public TimeStamp getTimeStamp() {
        return time;
    }

    public Whereabouts getPrevious() {
        return previous;
    }
    
    public void setPrevious(Whereabouts p){
        previous=p;
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
        TimeStamp t=time;
        if(t!=null)
            t=t.copy();
        PAD p =opinion;
        if(p!=null)
            p=p.copy();
        return new Whereabouts(character, room, p, certainty, t,null);
    }
    
    @Override
    public void setCertainty(double i){
        certainty=i;
    }

    @Override
    public Boolean dontKnow(){
        return room==null || character==null;
    }
    
}
