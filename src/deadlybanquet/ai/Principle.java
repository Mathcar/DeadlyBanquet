package deadlybanquet.ai;

/**
 *
 * @author omega
 */
public class Principle implements IThought{
    public PlanElement what;
    public PAD opinion;
    @Override
    public String toString(){
        return what +" is " + opinion;
    }
}
