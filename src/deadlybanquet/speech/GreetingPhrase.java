package deadlybanquet.speech;

import deadlybanquet.ai.IThought;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 2016-03-04.
 */
public class GreetingPhrase implements SpeechAct {
    private String text;

    //private SomeCharatcterEnumBothNpcAndPlayerAre speaker;

    private boolean endConversation;

    private TextPropertyEnum property;

    private ArrayList<IThought> content;


    public GreetingPhrase(){
        endConversation=false;
        property=null;
        content=null;
        text="";
    }

    public GreetingPhrase(String text,TextPropertyEnum property,ArrayList<IThought> contentList){
        endConversation=false;
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

    @Override
    public boolean isEndConversation() {
        return endConversation;
    }
}
