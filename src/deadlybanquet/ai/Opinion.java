package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * 
 * This IThought expresses the fact that (implied person) has opinion pad about aboutPersonRoomObject
 */
public class Opinion implements IThought{
    public String person;
    //With a placeholder pad, this becomes the question: What do you think about person?
    public PAD pad;
    //The time when this opinion first became current.
    public Time time;
    public Opinion previous;
    public Opinion(String about, PAD pad){
        this.pad = pad;
        person = about;
    }
    @Override
    public String toString(){
        return "feeling " + pad + " about " + person;
    }

    @Override
    /**
     * It is not possible to find opinions with a particular emotional value.
     */
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        Opinion d = (Opinion) i;
        return person==d.person;
    }

    @Override
    public void setPlaceHolderToNull() {
        if (pad.isPlaceholder()) pad=null;
    }
    
    public PAD getPAD(){
    	return this.pad;
    }

    @Override
    public double getCertainty() {
        return 1;
    }

    @Override
    public boolean isQuestion() {
        return pad.isPlaceholder();
    }

    @Override
    public int compareTo(IThought i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}