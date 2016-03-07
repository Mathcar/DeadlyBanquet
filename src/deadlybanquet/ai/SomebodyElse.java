package deadlybanquet.ai;

/**
 * Represents the idea that I think with howsure certainty that aboutPerson thinks... 
 * Opinion is my opinion, not that of aboutPerson
 * @author omega
 */
public class SomebodyElse implements IThought{
    public IThought what;
    //TODO: If you find yourself copy-pasting this more than once, make an abstract superclass
    public String aboutPerson;
    public PAD opinion;
    //TODO restrict to smaller range
    public double howsure;
    //TODO time information;
}
