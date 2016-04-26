package deadlybanquet.ai;

import deadlybanquet.model.TimeStamp;

/**
 * 
 * This IThought expresses the fact that (implied person) has opinion pad about aboutPersonRoomObject
 */
public class Opinion implements IThought{
    private String person;
    //With a placeholder pad, this becomes the question: What do you think about person?
    private PAD pad;
    //The time when this opinion first became current.
    private TimeStamp time;
    private Opinion previous;
    
    public Opinion(String about, PAD pad){
        this(about,pad,null,null);
    }
    
    public Opinion(String about, PAD opinion, TimeStamp t, Opinion p){
        person=about;
        pad=opinion;
        time=t;
        previous=p;
    }
    
    @Override
	public String debugMessage() {
    	return 	"Opinion:\n"
    			+ "Person: " + person + "\n"
				+ "PAD: " + pad.toString() + "\n"
				+ "Time: " + time.getDay() + ":" + time.getHour() + ":" + time.getMinute() + "\n" 
				+ ((previous!=null) ? previous.debugMessage() : null );
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
        if(i==null) return true;
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
    public int compareTo(IThought o) {
        if (getCertainty()<o.getCertainty()) return -1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return 1;
    }
    
    @Override
    public Opinion copy(){
        TimeStamp t=time;
        if(t!=null)
            t=t.copy();
        PAD p =pad;
        if(p!=null)
            p=p.copy();
        Opinion pr = previous;
        if(pr!=null)
           pr.copy();
        return new Opinion(person,p, t, pr);
    }
    
    @Override
    public void setCertainty(double i){
        
    }

    public String getPerson() {
        return person;
    }

    public PAD getPad() {
        return pad;
    }

    public TimeStamp getTimeStamp() {
        return time;
    }

    public Opinion getPrevious() {
        return previous;
    }

    @Override
    public Boolean dontKnow(){
        return pad==null;
    }

}