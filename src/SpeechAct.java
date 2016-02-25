/**
 * Created by Tom on 2016-02-25.
 */
public class SpeechAct {
    String text;

    // loveChange is probably wrong, but don't know what to do here.
    int loveChange;

    // if this is true, that means this is an speachAct that dose NOT gonna be replied.
    boolean endConversation;

    public SpeechAct(String s, int change){
        this.loveChange=change;
        this.text=s;
    }

    public SpeechAct(String s, int change, boolean end){
        this.loveChange=change;
        this.text=s;
        this.endConversation=end;
    }

    public static SpeechAct[] getAproprietAnswers(SpeechAct act){
        SpeechAct[] answers = {new SpeechAct("something nice",10),new SpeechAct("something rude",-10),
                new SpeechAct("Bye",0,true)};
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
