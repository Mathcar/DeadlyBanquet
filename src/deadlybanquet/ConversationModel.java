package deadlybanquet;

import deadlybanquet.speech.SpeechActFactory;
import org.newdawn.slick.Image;

import deadlybanquet.ai.BeingPolite;
import deadlybanquet.ai.IPerceiver;
import deadlybanquet.model.Character;
import deadlybanquet.model.NPC;
import deadlybanquet.model.Player;
import deadlybanquet.speech.SpeechAct;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tom on 2016-03-30.
 */
public class ConversationModel {

    // for now only works with players and NPC
    private IPerceiver perceiver1, perceiver2;
    private Image playerImage,npcImage;
    private SpeechActFactory saFactory;
    //Used to record the conversation
    private HashMap<IPerceiver, ArrayList<SpeechAct>> actHistory;

    //Constructor needed when player is a part of the conversation
    public ConversationModel(IPerceiver player, IPerceiver npc, Image playerImage, Image npcImage){
        this.perceiver1=player;
        this.perceiver2=npc;
        this.playerImage=playerImage;
        this.npcImage=npcImage;
    }

    //Constructor for a conversation inbetween NPCs
    public ConversationModel(IPerceiver player, IPerceiver npc){
        this.perceiver1=player;
        this.perceiver2=npc;
    }

    private void initDefaults(){
        //saFactory = new SpeechActFactory();
        actHistory = new HashMap<>();
        actHistory.put(perceiver1, new ArrayList<SpeechAct>());
        actHistory.put(perceiver2, new ArrayList<SpeechAct>());
    }

    private void conversation(){
        // for now assume that player will start to speak
        boolean end=false;
        /*while(!end){
            SpeechAct p = player.saySomeThing();
            p.setSpeaker(player.getName());
            npc.getBrain.hear(p);
            npc.think();
            SpeechAct n= npc.getAi.createSpeechAct();
            player.getBrain.hear(n);
            if(p.getContent().contains(BeingPolite.GOODBYE) || n.getContent().contains(BeingPolite.GOODBYE)){
                end=true;
            }
        }*/
    }

    private void doOneRound(SpeechAct act){
        sendActToPerceiver(perceiver2, act); //Should return an answer or there should be a handle for one
        SpeechAct answer = null;
        //answer = perceiver2.getAnswer();
        sendActToPerceiver(perceiver1, act); //No response should be created here
    }

    //Handles sending a speechact to a perceiver and record and archive what is needed
    private void sendActToPerceiver(IPerceiver p, SpeechAct act){
        actHistory.get(p).add(act);     //Add act to history
        p.hear(act);
    }
    
    public Image getPlayerImage(){
    	return playerImage;
    }
    public Image getNpcImage(){
    	return npcImage;
    }
    public IPerceiver getIPerceiver1(){
    	return perceiver1;
    }
    public IPerceiver getIperceiver2(){
    	return perceiver2;
    }

}
