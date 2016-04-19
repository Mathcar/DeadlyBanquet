package deadlybanquet;

import org.newdawn.slick.Image;

import deadlybanquet.ai.BeingPolite;
import deadlybanquet.ai.IPerceiver;
import deadlybanquet.model.Character;
import deadlybanquet.model.NPC;
import deadlybanquet.model.Player;
import deadlybanquet.speech.SpeechAct;

/**
 * Created by Tom on 2016-03-30.
 */
public class ConversationModel {

    // for now only works with players and NPC
    IPerceiver p1, p2;
    Image p,n;
    public ConversationModel(IPerceiver player, IPerceiver npc, Image playerImage, Image npcImage){
        this.p1=player;
        this.p2=npc;
        this.p=playerImage;
        this.n=npcImage;
    }
    public ConversationModel(IPerceiver player, IPerceiver npc){
        this.p1=player;
        this.p2=npc;
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
    
    public Image getPlayerImage(){
    	return p;
    }
    public Image getNpcImage(){
    	return n;
    }
    public IPerceiver getIPerceiverPlayer(){
    	return p1;
    }
    public IPerceiver getIperceiverNpc(){
    	return p2;
    }

}
