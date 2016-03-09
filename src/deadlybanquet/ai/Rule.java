package deadlybanquet.ai;

/**
 * Represents ideas such as "if there is a murder, then we call the police"
 * Essentially, if ifs is the case, then action should be added to the plan unless
 * there is a strong argument against it. Contrast this with a plan element, where
 * the action is added to the plan if the postcondition is desired. Contrast also with principle, 
 * which says that certain actions are good/bad/whatever
 * @author omega
 */
public class Rule implements IThought{
    public IThought[] ifs;
    public Action action;
}
