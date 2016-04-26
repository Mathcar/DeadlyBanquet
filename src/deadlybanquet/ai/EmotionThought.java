package deadlybanquet.ai;

import deadlybanquet.model.TimeStamp;

/**
 * Representation of an emotion or temperament
 * @author omega
 */
public class EmotionThought implements IThought{
    
    public enum et {
        EMOTION, TEMPERAMENT, ETPLACEHOLDER;
    }
    public et emotionortemperament;
    
    public PAD pad;
    
    public TimeStamp time;
    public EmotionThought previous;

    public EmotionThought(et e, PAD p){
        this(e,p,null,null);
    }
    
    public EmotionThought(et e, PAD p, TimeStamp t, EmotionThought em){
        emotionortemperament=e;
        pad=p;
        time=t;
        previous=em;
    }
    @Override
    public void setPlaceHolderToNull() {
        if (pad.getP()<-1) pad=null;
    }

    @Override
    public double getCertainty() {
        return 1;
    }

    @Override
    public boolean isQuestion() {
        return emotionortemperament==et.ETPLACEHOLDER || pad.isPlaceholder() || time.isPlaceHolder();
    }

    @Override
    public int compareTo(IThought o) {
        if (getCertainty()<o.getCertainty()) return -1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return 1;
    }
    
    @Override
    public String toString(){
        return emotionortemperament + " with value " + pad;
    }
    
    @Override
    public boolean contains(IThought i) {
        if(i==null) return true;
        if(!this.getClass().equals(i.getClass())) return false;
        EmotionThought d = (EmotionThought) i;
        return (d.emotionortemperament==et.ETPLACEHOLDER||d.emotionortemperament==emotionortemperament);
    }
    
    @Override
    public EmotionThought copy(){
        TimeStamp t=time;
        if(t!=null)
            t=t.copy();
        PAD p =pad;
        if(p!=null)
            p=p.copy();
        return new EmotionThought (emotionortemperament,p,t,previous);
    }
    
    @Override
    public void setCertainty(double i){
        
    }

    @Override
    public Boolean dontKnow(){
        return emotionortemperament==null||pad==null||time==null;
    }
}
