package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Describes things with format somebody does something to something/somebody/somewhere
 * @author omega
 */
public class Do implements IThought{

    @Override
    public boolean contains(IThought i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public enum What{
        MOVETO,
        PICKUP,
        PUTDOWN,
        MURDER;
    }
    
    public enum Wd{
        WILL, INTENDS;
    }
    public What what;
    public String doer;
    //who is murdered, what is picked up...
    public String withWhat;
    public Time when;
    //Describes whether somebody actually does/did item or just intended to do so
    public Wd willormight;
    public Do(What what, String doer, String to, Time time, Wd w) {
        this.what=what;
        this.doer=doer;
        this.when=time;
        this.withWhat=to;
        willormight = w;
    }
}
