package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Representation of an emotion or temperament
 * @author omega
 */
public class EmotionThought implements IThought{

    @Override
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isQuestion() {
        return emotionortemperament==et.ETPLACEHOLDER || pad.isPlaceholder() || time.isPlaceHolder();
    }

    @Override
    public int compareTo(IThought i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
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
}
