package deadlybanquet.ai;

/**
 * Enum for actions which do not need any extra information,
 * in this case politeness phrases.
 * @author omega
 */
public enum BeingPolite implements IThought{
    GREET, GOODBYE, THANKS, THANKSANYWAY;

    @Override
    public boolean contains(IThought i) {
        if(i==null) return true;
        if(!this.getClass().equals(i.getClass())) return false;
        return this==i;
    }

    @Override
    public void setPlaceHolderToNull() {
        //No placeholders here
    }

    @Override
    public double getCertainty() {
        return 1;
    }

    @Override
    public boolean isQuestion() {
        return false;
    }

    @Override
    public int compareTo(IThought o) {
        if (getCertainty()<o.getCertainty()) return -1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return 1;
    }
    
    @Override
    public BeingPolite copy(){
        return this;
    }

    @Override
    public void setCertainty(double i){
        
    }

    public Boolean dontKnow(){
        return false; // always know hoe to greet someone.
    }
}
