package deadlybanquet.model;

import deadlybanquet.ai.IPerceiver;
import deadlybanquet.speech.SpeechAct;

import java.util.ArrayList;

/**
 * Created by Hampus on 2016-04-13.
 */
public abstract class Perceiver implements IPerceiver {
    @Override
    public void hear(SpeechAct act) {

    }

    @Override
    public void observeInteraction(String who, String with) {

    }

    @Override
    public void observePickUp(String who, String what) {

    }

    @Override
    public void observePutDown(String who, String what) {

    }

    @Override
    public void observeRoomChange(String character, String origin, String destination) {

    }

    @Override
    public void seePeople(ArrayList<String> people) {

    }

    @Override
    public String getName() {
        return null;
    }
}
