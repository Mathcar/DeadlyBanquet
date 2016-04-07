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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public TextPropertyEnum getTextProperty() {
        return textProperty;
    }

    public void setTextProperty(TextPropertyEnum textProperty) {
        this.textProperty = textProperty;
    }

    public SpeechType getSpeechType() {
        return speechType;
    }

    public void setSpeechType(SpeechType speechType) {
        this.speechType = speechType;
    }

}
