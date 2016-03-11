package deadlybanquet.ai;

/**
 * Representation of an emotion or temperament
 * @author omega
 */
public class EmotionThought implements IThought{
    public enum et {
        EMOTION, TEMPERAMENT;
    }
    public et emotionortemperament;
    public PAD pad;
    
    @Override
    public String toString(){
        return emotionortemperament + " with value " + pad;
    }
}
