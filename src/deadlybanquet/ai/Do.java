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

    @Override
    public void setPlaceHolderToNull() {
        if (doer=="") doer=null;
        if (withWhat=="") withWhat=null;
        if (when.getDay()<0) when=null;
    }

    @Override
    public double getCertainty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isQuestion() {
        return doer=="" || withWhat=="" || when.getDay()<0;
    }

    @Override
    public int compareTo(IThought i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public enum What{
        MOVETO,
        PICKUP,
        PUTDOWN,
        MURDER;
    }
    
    public What what;
    //Placeholder is empty string, null is null.
    public String doer;
    //who is murdered, what is picked up...
    public String withWhat;
    public Time when;
    //Describes whether somebody actually does/did item or just intended to do so
    public Do(What what, String doer, String to, Time time) {
        this.what=what;
        this.doer=doer;
        this.when=time;
        this.withWhat=to;
    }
}
