package deadlybanquet.ai;

import deadlybanquet.model.Time;

/**
 *
 * @author omega
 */
public class Say implements IThought{
    
    public enum How {
        SAY, //with placeholder:ask
        AGREE, //information being agreed with gets sent along
        DISAGREE, //one's own opinion gets sent along
        YESNO, //this is really just for polite responses: Oh, is it?
        REQUEST, // With Do or Say object
        REASON; //With placeholder: why. Without placeholder: because.
    }
    String speaker;
    String hearer;
    IThought content;
    How type;
    //This is null if one is currently doing that speech act
    //i.e. speaker is the person sending this IThought and
    //hearer is the receiver.
    //When when is not null, this indicates something which
    //has been said (or will be said).
    Time when;
    
    public Say (String s, String h, IThought c, How t, Time time){
        speaker=s;
        hearer=h;
        content=c;
        type=t;
        this.when = time;
    }
    
    @Override
    public String toString(){
        return speaker + " " + type + " " + hearer + " " + content;
    }
    @Override
    public boolean contains(IThought i) {
        if(i==null) return true;
        if(!this.getClass().equals(i.getClass())) return false;
        Say s = (Say) i;
        if(this.speaker!="" && this.speaker!=s.speaker) return false;
        if(this.hearer!="" && this.hearer!=s.hearer) return false;
        if (this.content==null) return true;
        return content.contains(s.content);
    }
    
    @Override
    public void setPlaceHolderToNull() {
        if (speaker=="") speaker=null;
        if (hearer =="") hearer = null;
        content.setPlaceHolderToNull();
        if (when.getDay()<0) when=null;
    }

    @Override
    public double getCertainty() {
        return 1;
    }

    @Override
    public boolean isQuestion() {
        return speaker=="" || hearer=="" || content.isQuestion() || when.isPlaceHolder();
    }

    @Override
    public int compareTo(IThought o) {
        if (getCertainty()<o.getCertainty()) return -1;
        else if (getCertainty()==o.getCertainty()) return 0;
        return 1;
    }
    
}
