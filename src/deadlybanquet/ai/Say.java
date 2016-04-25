package deadlybanquet.ai;

import static deadlybanquet.ai.Say.How.*;
import deadlybanquet.model.TimeStamp;

/**
 *
 * @author omega
 */
public class Say implements IThought{
    
    public enum How {
        SAY, 
        AGREE, //information being agreed with gets sent along
        DISAGREE, //one's own opinion gets sent along
        YESNO, //this is really just for polite responses: Oh, is it?
        REQUEST, // With Do or Say object
       // REASON; //With placeholder: why. Without placeholder: because.
        //We are leaving out reason as well unless we get really bored.
    }
    String speaker;
    String hearer;
    //Null is placeholder: What did a say to b?
    //To answer I don't know, just answer do with talkto: To be interpreted as
    //I saw them speak.
    IThought content;
    How type;
    //This is null if one is currently doing that speech act
    //i.e. speaker is the person sending this IThought and
    //hearer is the receiver.
    //When when is not null, this indicates something which
    //has been said (or will be said).
    TimeStamp when;
    
    public Say(String s, String h, IThought c, How t){
        this(s,h,c,t,null);
    }
    public Say (String s, String h, IThought c, How t, TimeStamp time){
        speaker=s;
        hearer=h;
        content=c;
        type=t;
        this.when = time;
    }
    
    
    @Override
    public String toString(){
        String y = "Yes, ";
        String e = "";
        if (type==DISAGREE)
            y="No, ";
        if (type==YESNO){
            y= "Is ";
            e = "?";
        }
        if(type==REQUEST){
            y="Please";
        }
            
        return y + content + e;
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
    
    @Override
    public Say copy(){
        TimeStamp t=when;
        if(t!=null)
            t=t.copy();
        return new Say(speaker,hearer,content,type, t);
    }
    
    @Override
    public void setCertainty(double i){
        
    }
    
}
