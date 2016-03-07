package deadlybanquet.ai;

/**
 * IThought wrapper for a single plan element. "person intends to do action."
 * also "person has done action"
 * @author omega
 */
public class Plan implements IThought{
    public enum wd{
        WILL, DID;
    }
    public String person;
    public Action action;
    public wd willordid;
    //TODO time;
}
