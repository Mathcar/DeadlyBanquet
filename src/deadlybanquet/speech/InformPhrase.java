package deadlybanquet.speech;

import deadlybanquet.ai.IThought;

import java.util.ArrayList;

/**
 * Created by Tom on 2016-03-23.
 */
public class InformPhrase implements SpeechAct {
    private String text;
    private TextPropertyEnum property;
    private ArrayList<IThought> content;

    public InformPhrase(){
        property=null;
        content=null;
        text="";
    }

    public InformPhrase(String text,TextPropertyEnum property,ArrayList<IThought> contentList){
        this.text=text;
        this.property=property;
        this.content=contentList;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text=text;
    }

    @Override
    public ArrayList<IThought> getContent() {
        return this.content;
    }

    @Override
    public void setContent(ArrayList<IThought> thoughts) {
        this.content=thoughts;
    }

    @Override
    public void setTextProperty(TextPropertyEnum property) {
        this.property=property;
    }

    @Override
    public TextPropertyEnum getTextProperty(){
        return this.property;
    }

    @Override
    public String getSpeaker() {
        return null;
    }
}
