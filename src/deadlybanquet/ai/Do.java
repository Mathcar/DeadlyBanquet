package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Describes things with format somebody does something to something/somebody/somewhere
 * @author omega
 */
public class Do {
    public enum What{
        MOVETO,
        PICKUP,
        PUTDOWN,
        MURDER;
    }
    public What what;
    public String doer;
    //who is murdered, what is picked up...
    public String withWhat;
    public Time when;
    public Do(What what, String doer, String to, Time time) {
        this.what=what;
        this.doer=doer;
        this.when=time;
        this.withWhat=to;
    }
}
