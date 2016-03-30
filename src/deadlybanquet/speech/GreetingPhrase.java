package deadlybanquet.speech;

import deadlybanquet.ai.IThought;

import java.util.ArrayList;

/**
 * Created by Tom on 2016-03-04.
 */

public class GreetingPhrase implements SpeechAct {
    private String text;

    //private SomeCharatcterEnumBothNpcAndPlayerAre speaker;

    private TextPropertyEnum property;

    private ArrayList<IThought> content;

    private String speaker;


    public GreetingPhrase(){
        property=null;
        content=null;
        text="";
    }

    public GreetingPhrase(String text,TextPropertyEnum property,ArrayList<IThought> contentList){
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
        return this.speaker;
    }

    @Override
    public void setSpeaker(String name){
        this.speaker=name;
    }
}
