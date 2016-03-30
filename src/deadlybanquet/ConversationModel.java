package deadlybanquet;

import deadlybanquet.ai.Action;
import deadlybanquet.model.NPC;
import deadlybanquet.model.Player;
import deadlybanquet.speech.SpeechAct;

/**
 * Created by Tom on 2016-03-30.
 */
public class ConversationModel {

    // for now only works with players and NPC
    Player player;
    NPC npc;
    public ConversationModel(Player player, NPC npc){
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
            if(p.getContent().contains(Action.GOODBYE) || n.getContent().contains(Action.GOODBYE)){
                end=true;
            }
        }*/
    }

}
