package deadlybanquet.ai;

import deadlybanquet.model.Time;

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
    
    public Time time;
    public EmotionThought previous;
    
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

    public EmotionThought(et e, PAD p){
        emotionortemperament=e;
        pad=p;
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
}
