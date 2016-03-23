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
