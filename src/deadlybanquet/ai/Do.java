package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 * Describes things with format somebody does something to something/somebody/somewhere
 * @author omega
 */
public class Do implements IThought{
    
    public enum What{
        MOVETO,
        PICKUP,
        PUTDOWN,
        MURDER;
    }


    private What what;
    //Placeholder is empty string, null is null.
    private String doer;
    //who is murdered, what is picked up...
    private String withWhat;
    private Time when;
    //Describes whether somebody actually does/did item or just intended to do so
    public Do(What what, String doer, String to, Time time) {
        this.what=what;
        this.doer=doer;
        this.when=time;
        this.withWhat=to;
    }
    
    @Override
    public boolean contains(IThought i) {
        if(i==null) return true;
        if(!this.getClass().equals(i.getClass())) return false;
        Do d = (Do) i;
        if (d.doer!="" && d.doer!=this.doer) return false;
        if (d.withWhat!="" && d.withWhat!=this.withWhat) return false;
        return true;
    }

    @Override
    public void setPlaceHolderToNull() {
        if (doer=="") doer=null;
        if (withWhat=="") withWhat=null;
        if (when.isPlaceHolder()) when=null;
    }

    @Override
    public double getCertainty() {
        return 1;
    }

    @Override
    public boolean isQuestion() {
        return doer=="" || withWhat=="" || when.getDay()<0;
    }

    @Override
    public int compareTo(IThought o) {
        if (getCertainty()<o.getCertainty()) return -1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return 1;
    }
    
    @Override
    public String toString(){
        String t = "";
        if (when!=null)
            t=when.toString();
        return doer + " " + what + " " + withWhat + " at " + t;
    }
    
    @Override
    public Do copy(){
        Time t=when;
        if(t!=null)
            t=t.copy();
        return new Do(what,doer,withWhat, t);
    }
    
    @Override
    public void setCertainty(double i){
        
    }

    public What getWhat() {
        return what;
    }

    public void setWhat(What what) {
        this.what = what;
    }

    public String getDoer() {
        return doer;
    }

    public void setDoer(String doer) {
        this.doer = doer;
    }

    public String getWithWhat() {
        return withWhat;
    }

    public void setWithWhat(String withWhat) {
        this.withWhat = withWhat;
    }

    public Time getWhen() {
        return when;
    }

    public void setWhen(Time when) {
        this.when = when;
    }
}
