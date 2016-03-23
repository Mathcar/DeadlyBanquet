package deadlybanquet.ai;

import static deadlybanquet.ai.Action.ACTIONPLACEHOLDER;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * The main purpose of this class is in formulating plans inside the AI.
 * In a speech act, this may be interpreted as "Ask something" (if the value of action is ASK)
 * give information etc. The intended information to be requested or given is found 
 * in the results list. The prerequisites act as a because clause 
 * I want to know X, because I don't know it
 * can be represented as
 * prerequisite = don't know X
 * action = ASK
 * results = know X
 * @author omega
 */
public class PlanElement implements IThought {
    public ArrayList<IThought> prerequisites;
    //Placeholder is ACTIONPLACEHOLDER. Null is null.
    public Action action;
    public ArrayList<IThought> results;
    public pe type;
    
    public PlanElement(ArrayList<IThought> pre, Action a, ArrayList<IThought> r, pe type){
        prerequisites=pre;
        action = a;
        results = r;
        this.type = type;
    }
    public enum pe{
        RULE,
        PLAN,
        PEPLACEHOLDER; //TODO there must be more to this
    }
    @Override
    public String toString(){
        return ("I want to do " + action.toString() + "in order that " + results.toString() + "because " + prerequisites.toString());
    }

    @Override
    public boolean contains(IThought i) {
        //Wrong type of information
        if(i==null) throw new NullPointerException();
        if(!this.getClass().equals(i.getClass())) return false;
        PlanElement d = (PlanElement) i;
        if (d.type!=pe.PEPLACEHOLDER && d.type!=type) return false;
        if (d.action!=ACTIONPLACEHOLDER && d.action!=action) return false;
        ArrayList<IThought> temp = new ArrayList<>();
        //Any given prerequisites must be matched
        for (IThought t: d.prerequisites){
            for (IThought q: prerequisites){
                if (q.contains(t)) temp.add(q);
            }
            if (temp.isEmpty()) return false;
            temp.clear();
        }
        //So must the results
        for (IThought t: d.results){
            for (IThought q: results){
                if (q.contains(t)) temp.add(q);
            }
            if (temp.isEmpty()) return false;
            temp.clear();
        }
        return true;
    }
}
