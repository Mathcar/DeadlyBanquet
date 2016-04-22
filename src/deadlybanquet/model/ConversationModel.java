package deadlybanquet.model;

import deadlybanquet.ai.IThought;
import deadlybanquet.speech.SpeechActFactory;
import deadlybanquet.speech.SpeechType;
import deadlybanquet.speech.TextPropertyEnum;
import org.newdawn.slick.Image;

import deadlybanquet.ai.BeingPolite;
import deadlybanquet.ai.IPerceiver;
import deadlybanquet.model.Character;
import deadlybanquet.model.NPC;
import deadlybanquet.model.Player;
import deadlybanquet.speech.SpeechAct;

import java.lang.reflect.Array;
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
    private SpeechAct playersChoice;
    private boolean hasPlayer;
    private boolean conversationCompleted;
    private int iteration = 0;
    private final int maxIterations = 4;
    //Used to record the conversation
    private HashMap<IPerceiver, ArrayList<SpeechAct>> actHistory;

    //Constructor needed when player is a part of the conversation
    public ConversationModel(IPerceiver player, IPerceiver npc, Image playerImage, Image npcImage){
        this.perceiver1=player;
        this.perceiver2=npc;
        this.playerImage=playerImage;
        this.npcImage=npcImage;
        //Images should only be sent when player is in the conversation
        hasPlayer = true;
        initDefaults();
    }

    //Constructor for a conversation inbetween NPCs
    public ConversationModel(IPerceiver npc1, IPerceiver npc2){
        this.perceiver1=npc1;
        this.perceiver2=npc2;
        //no images should mean that no player is in the conversation
        hasPlayer = false;
        initDefaults();
    }

    private void initDefaults(){
        //TODO NO PERCEIVERS IN THE FACTORY!
        actHistory = new HashMap<>();
        actHistory.put(perceiver1, new ArrayList<SpeechAct>());
        actHistory.put(perceiver2, new ArrayList<SpeechAct>());
        conversationCompleted = false;
        System.out.println("Defaults initialized");
    }

    private void conversation(){
        // for now assume that player will start to speak

        boolean end=false;


        if(hasPlayer){
            if(playersChoice!= null){
                SpeechAct sa = getPlayerChoice();
                doOneRound(sa);
            }
        }else{
            //TODO Create flow for npc-npc conversation
        }
        /*if(iteration == 0){
            perceiver1.selectPhrase(saFactory.getDialogueOptions(true));
        }
        saFactory.getDialogueOptions(true);
        /*

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
        if(act.getSpeechType() == SpeechType.GOODBYE){
            conversationCompleted = true;
        }
    }

    //Handles sending a speechact to a perceiver and record and archive what is needed
    private void sendActToPerceiver(IPerceiver p, SpeechAct act){
        actHistory.get(p).add(act);     //Add act to history
        p.hear(act);
    }

    public ArrayList<SpeechAct> getAllPropertyVariations(IThought thought){
        ArrayList<SpeechAct> temp = new ArrayList<>();
        ArrayList<IThought> tempI = new ArrayList<IThought>();
        tempI.add(thought);
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI,
                                TextPropertyEnum.NEUTRAL, perceiver1, perceiver2));
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI, TextPropertyEnum.PROPER,
                                                            perceiver1, perceiver2));
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI, TextPropertyEnum.COLLOQUIAL,
                                                                perceiver1, perceiver2));
        return temp;
    }

    //Used as an internal getter so that the variable is reset after reading it!
    private SpeechAct getPlayerChoice(){
        SpeechAct act = playersChoice;
        System.out.println(act.getLine() + "     is the line before reset of playerChoice");
        playersChoice = null;
        System.out.println(act.getLine() + "     is the line after reset of playerChoice");
        return act;
    }

    public boolean isConversationOver(){
        return conversationCompleted;
    }

    //Called from the Talk state once a speechact has been constructed using the interface
    //This is only used in conversations that contain the player
    public void recieveChoice(SpeechAct sa){
        if(sa == null){
            System.out.println("Speech act received was null :(");
        }else {
            System.out.println("Player choice received : " + sa.getLine());
            playersChoice = sa;
            //TODO This should most likely be set in the doOneRound() method instead!
            if(sa.getSpeechType() == SpeechType.GOODBYE){
                conversationCompleted = true;
            }
        }
    }

    //Returns the latest string sent by the 2nd perciever.
    //Intended for use with the view and therefore only get the second perceivers answer
    public String getLatestResponse(){
        ArrayList<SpeechAct>  acts = actHistory.get(perceiver2);
        //System.out.println("current size of responses : " + acts.size());
        if(acts.size() == 0)
            return "";      //No act has been sent yet so there is nothing to display
        else
            return acts.get(acts.size()-1).getLine();   //Return latest line of act
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
