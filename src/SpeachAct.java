/**
 * Created by Tom on 2016-02-25.
 */
public class SpeachAct {
    String text;

    // loveChange is probably wrong, but don't know what to do here.
    int loveChange;

    // if this is true, that means this is an speachAct that dose NOT gonna be replied.
    boolean endConversation;

    public SpeachAct(String s,int change){
        this.loveChange=change;
        this.text=s;
    }

    public SpeachAct(String s,int change,boolean end){
        this.loveChange=change;
        this.text=s;
        this.endConversation=end;
    }

    public static SpeachAct[] getAproprietAnswers(SpeachAct act){
        SpeachAct[] answers = {new SpeachAct("something nice",10),new SpeachAct("something rude",-10),
                new SpeachAct("Bye",0,true)};
        return answers;
    }



    public String getText(){
        return this.text;
    }

    public int getLoveChange() {
        return loveChange;
    }

    public boolean isEndConversation() {
        return endConversation;
    }


}
