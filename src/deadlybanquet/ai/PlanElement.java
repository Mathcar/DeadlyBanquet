package deadlybanquet.ai;

/**
 * 
 * The main purpose of this class is in formulating plans inside the AI.
 * In a speech act, this may be interpreted as "Ask something" (if the value of action is ASK)
 * give information etc. The intended information to be requested or given is found 
 * in the results list. The prerequsites act as a because clause 
 * I want to know X, because I don't know it
 * can be represented as
 * prerequisite = don't know X
 * action = ASK
 * results = know X
 * @author omega
 */
public class PlanElement implements IThought {
    public IThought[] prerequisites;
    public Action action;
    public IThought[] results;
}
