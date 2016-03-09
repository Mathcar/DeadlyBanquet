package deadlybanquet.ai;

/**
 * This may or may not be a useful instance of IThought.
 * The main purpose of this class is in formulating plans inside the AI.
 * @author omega
 */
public class PlanElement implements IThought {
    public IThought[] prerequisites;
    public Action action;
    public IThought[] results;
}
