package deadlybanquet.ai;

/**
 * IThought wrapper for a single plan element. "person intends to do action."
 * also "person has done action", "person intended to do action but chose not to"
 * @author omega
 */
public class Plan implements IThought{

    @Override
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        Plan d = (Plan) i;
        if (d.person!="" && person!=d.person) return false;
        if (d.willordid!=wd.WDPLACEHOLDER&&willordid!=d.willordid) return false;
        if (d.action==null) return true;
        return action.contains(d.action);
    }
    public enum wd{
        WILL, DID, DISCARDED, WDPLACEHOLDER;
    }
    //Placeholder is the empty string. Null value is null.
    public String person;
    //This has placeholder null and no null value.
    public PlanElement action;
    public wd willordid;
    //TODO time;
    @Override
    public String toString(){
        return person + willordid + action;
    }
}
