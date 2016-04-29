package deadlybanquet.ai;

import java.util.ArrayList;

/**
 *
 * @author omega
 */
public class BrainInitInformation {
    public SortedList information;
    public PAD emotion; 
    public PAD temperament; 
    public ArrayList<Desire>desires;
    public ArrayList<Desire>goals;
    public ArrayList<IThought>plan;
    public String room;
    public String name;
    public AIControler aic;
    public ArrayList<Whereabouts>whereabouts = new ArrayList<>();
}
