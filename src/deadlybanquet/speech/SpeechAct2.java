package deadlybanquet.speech;


/**
 * Created by Tom on 2016-04-08.
 */
public class SpeechAct2 {
    private String line;
    private String talker;
    private String listener;
    private SpeechType speechType;
    private TextPropertyEnum property;

    private Boolean dead = false; // just for easier programing

    public SpeechAct2(String line,String talker,String listener,SpeechType st,TextPropertyEnum tpe){ //todo add IThought
        this.line=line;
        this.talker=talker;
        this.listener=listener;
        this.speechType=st;
        this.property=tpe;
    }//todo add someway to seee that it is a goodbye speechAct or leave speechAct.
    public SpeechAct2(){
        dead=true;
    }

    public boolean isDead(){
        return dead;
    }

    public SpeechAct2(String line){ // this is for test
        this.line=line;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getTalker() {
        return talker;
    }

    public void setTalker(String talker) {
        this.talker = talker;
    }

    public String getListener() {
        return listener;
    }

    public void setListener(String listener) {
        this.listener = listener;
    }

    public SpeechType getSpeechType() {
        return speechType;
    }

    public void setSpeechType(SpeechType speechType) {
        this.speechType = speechType;
    }

    public TextPropertyEnum getProperty() {
        return property;
    }

    public void setProperty(TextPropertyEnum property) {
        this.property = property;
    }
}
