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
public class GreetingFrase implements SpeechAct {
    private String text;
    private boolean endConversation;

    private String property;

    private ArrayList<IThought> listOfproperties;

    public GreetingFrase(String property){
        this.property=property;
        listOfproperties=new ArrayList<IThought>();

    }

    @Override
    public boolean isEndConversation() {
        return this.endConversation;
    }

    @Override
    public Map<String, Integer> getOpinionChanges() {
        return null;
    }

    public ArrayList<IThought> getProperties(){
        return this.listOfproperties;
    }

    @Override
    public String getSpeaker() {
        return null;
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public ArrayList<IThought> getContent() {
        return null;
    }
}
