package deadlybanquet.ai;

/**
 * Representation of a desire or goal.
 * @author omega
 */
public class Desire implements IThought{
    public enum dg {
        DESIRE, GOAL;
    }
    public IThought what;
    //Might want to restrict the value of this at some point
    public double strength;
    //TODO order by strength;
    public dg desireorgoal;
}
