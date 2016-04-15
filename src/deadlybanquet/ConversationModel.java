package deadlybanquet;

import deadlybanquet.ai.BeingPolite;
import deadlybanquet.model.Character;
import deadlybanquet.model.NPC;
import deadlybanquet.model.Player;
import deadlybanquet.speech.SpeechAct;

/**
 * Created by Tom on 2016-03-30.
 */
public class ConversationModel {

    // for now only works with players and NPC
    Player player;
    Character npc;
    public ConversationModel(Player player, Character npc){
        this.player=player;
        this.npc=npc;
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
    
    public Player getPlayer(){
    	return player;
    }
    public Character getNpc(){
    	return npc;
    }

}
