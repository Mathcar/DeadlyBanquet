package deadlybanquet.speech;


import deadlybanquet.ai.IThought;

import java.util.ArrayList;

/**
 * Created by Tom on 2016-04-08.
 */
public class SpeechAct {
    private String line;
    private String speaker;
    private String listener;
    private SpeechType speechType;
    private TextPropertyEnum property;
    private ArrayList<IThought> content;

    private Boolean dead = false; // just for easier programing

    public SpeechAct(String line, String talker, String listener, SpeechType st,
                     TextPropertyEnum tpe,ArrayList<IThought> con){ //todo add IThought
        this.line=line;
        this.speaker=talker;
        this.listener=listener;
        this.speechType=st;
        this.property=tpe;
        this.content=con;
    }//todo add someway to seee that it is a goodbye speechAct or leave speechAct.
    public SpeechAct(){
        dead=true;
    }

    public boolean isDead(){
        return dead;
    }

    public SpeechAct(String line){ // this is for test
        this.line=line;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpesker(String talker) {
        this.speaker = talker;
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

    public ArrayList<IThought> getContent(){
        return this.content;
    }

    public SpeechAct copy(){
        SpeechAct s = this;
        return new SpeechAct(s.getLine(),s.getSpeaker(),s.getListener(),s.getSpeechType(),s.getProperty(),s.getContent());
    }
}
