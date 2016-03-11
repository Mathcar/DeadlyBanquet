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
    public PlanElement action;
    public wd willordid;
    //TODO time;
    @Override
    public String toString(){
        return person + willordid + action;
    }
}
