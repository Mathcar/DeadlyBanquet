package deadlybanquet.ai;

/**
 * Represents that somebody or something is/was somewhere (in a room)
 * @author omega
 */
public class Position implements IThought{
    public String where;
    //TODO: If you find yourself copy-pasting this more than once, make an abstract superclass
    public String whoorwhat;
    public PAD opinion;
    public double howsure;
     //TODO time information;
}
