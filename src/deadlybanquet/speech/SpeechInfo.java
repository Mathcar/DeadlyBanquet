package deadlybanquet.speech;

import deadlybanquet.ai.IThought;

import java.util.ArrayList;

/**
 * Created by Tom on 2016-04-06.
 */
public class SpeechInfo {
    private String text;
    private TextPropertyEnum textProperty;
    private SpeechType speechType;

    public SpeechInfo(String t,TextPropertyEnum prop,SpeechType st){
        this.text=t;
        this.textProperty=prop;
        this.speechType=st;
    }

}
