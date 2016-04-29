package deadlybanquet.model;

import deadlybanquet.ai.*;
import deadlybanquet.speech.SpeechActFactory;
import deadlybanquet.speech.SpeechType;
import deadlybanquet.speech.TextPropertyEnum;
import org.newdawn.slick.Image;

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
    private boolean conversationCompleted;
    private int iteration = 0;
    private final int maxIterations = 4;
    //Used to record the conversation
    private HashMap<IPerceiver, ArrayList<SpeechAct>> actHistory;
    private SpeechAct decidedAnswer;

    //Constructor needed when player is a part of the conversation
    public ConversationModel(IPerceiver player, IPerceiver npc, Image playerImage, Image npcImage){
        this.perceiver1=player;
        this.perceiver2=npc;
        this.playerImage=playerImage;
        this.npcImage=npcImage;
        //Images should only be sent when player is in the conversation
        initDefaults();
    }

    //Constructor for a conversation inbetween NPCs
    public ConversationModel(IPerceiver npc1, IPerceiver npc2){
        if(npc1.isPlayer() || npc2.isPlayer())
            Debug.printDebugMessage("Wrong conversationModel constructor, use the one for the player!"
                                    , Debug.Channel.CONVERSATION);
        this.perceiver1=npc1;
        this.perceiver2=npc2;
        //no images should mean that no player is in the conversation
        initDefaults();
    }

    private void initDefaults(){
        actHistory = new HashMap<>();
        actHistory.put(perceiver1, new ArrayList<SpeechAct>());
        actHistory.put(perceiver2, new ArrayList<SpeechAct>());
        conversationCompleted = false;
    }

    public void runConversation(){
        // for now assume that perceiver1 will start to speak
        if(perceiver1.isPlayer()){
            if(playersChoice!= null){
                Debug.printDebugMessage("Players choice exists!", Debug.Channel.CONVERSATION);
                SpeechAct sa = getPlayerChoice();
                Debug.printDebugMessage("Line = " + sa.getLine() + " speaker = " + sa.getSpeaker()
                                    + " listener = " +sa.getListener(), Debug.Channel.CONVERSATION);
                doOneRound(sa);
            }
        }else if(perceiver2.isPlayer()){
            if(iteration<maxIterations){
                if(iteration==0) {
                    TextPropertyEnum tpe = perceiver1.chooseProperty(perceiver2.getName());
                    SpeechAct greeting = SpeechActFactory.convertIThoughtToSpeechAct(BeingPolite.GREET,
                            tpe, perceiver1, perceiver2);
                    sendActToPerceiver(perceiver2, greeting); //TODO NPC cant handle the Yes, GREET response
                    Debug.printDebugMessage("NPCs actually greeted each other without crash, score!",
                            Debug.Channel.CONVERSATION);
                    iteration++;
                }else if(iteration==1){
                    if(playersChoice!=null){
                        sendActToPerceiver(perceiver1, getPlayerChoice());
                    }


                    SpeechAct answer = sendActToPerceiver(perceiver2, perceiver1.getIntendedPhrase());
                    decidedAnswer = sendActToPerceiver(perceiver1,answer);
                    Debug.printDebugMessage("NPC actually asked the intended question and got an answer without crash, score!",
                            Debug.Channel.CONVERSATION);
                    iteration++;
                }
            }


        }else{
            //TODO Create flow for npc-npc conversation
            //Force start by greeting
            //answer should be a greeting
            //then let perceiver1 choose the question to start the hear loop
            //keep track of the conversation by forving a goodbye after a certain amount
            //of iterations
            if(iteration<maxIterations){
                //First runthrough means only greetings should be exchanged
                if(iteration==0){
                    TextPropertyEnum tpe = perceiver1.chooseProperty(perceiver2.getName());
                    SpeechAct greeting = SpeechActFactory.convertIThoughtToSpeechAct(BeingPolite.GREET,
                                                                                    tpe, perceiver1, perceiver2);
                    sendActToPerceiver(perceiver1, sendActToPerceiver(perceiver2, greeting));
                    Debug.printDebugMessage("NPCs actually greeted each other without crash, score!",
                                                Debug.Channel.CONVERSATION);
                    iteration++;
                } else if(iteration==1){  //Start the conversation using the phrase chosen by the StateBasedAI
                    SpeechAct answer = sendActToPerceiver(perceiver2, perceiver1.getIntendedPhrase());
                    decidedAnswer = sendActToPerceiver(perceiver1,answer);
                    Debug.printDebugMessage("NPC actually asked the intended question and got an answer without crash, score!",
                                                Debug.Channel.CONVERSATION);
                    iteration++;
                } else{     //Just run the conversation using old answers and the hear function!
                    SpeechAct answer = sendActToPerceiver(perceiver2, decidedAnswer);
                    decidedAnswer = sendActToPerceiver(perceiver1, answer);
                    iteration++;
                }
            }
            else{
                //Should say goodbye here instead, but starting like this for debug
                //TODO add goodbye phrases here instead
                conversationCompleted = true;
            }
        }
    }

    private void doOneRound(SpeechAct act){
        SpeechAct answer = sendActToPerceiver(perceiver2, act); //Should return an answer or there should be a handle for one
        //answer = perceiver2.getAnswer();
        Debug.printDebugMessage("Answer = " + answer.getLine(), Debug.Channel.CONVERSATION);
        sendActToPerceiver(perceiver1, answer); //No response should be created here
        if(act.getSpeechType() == SpeechType.GOODBYE){
            conversationCompleted = true;
        }
    }

    //Handles sending a speechact to a perceiver and record and archive what is needed
    private SpeechAct sendActToPerceiver(IPerceiver p, SpeechAct act){
        Debug.printDebugMessage(act.getSpeaker() + " said   \"" + act.getLine() + "\"       to " + act.getListener(),
                                     Debug.Channel.CONVERSATION);
        if(p==perceiver1)
            actHistory.get(perceiver2).add(act);     //Add act to history
        else
            actHistory.get(perceiver1).add(act);
        return p.hear(act);
    }

    public ArrayList<SpeechAct> getAllPropertyVariations(IThought thought){
        ArrayList<SpeechAct> temp = new ArrayList<>();
        ArrayList<IThought> tempI = new ArrayList<IThought>();
        //Debug.printDebugMessage("speaker = " + perceiver1.getName());
        tempI.add(thought);
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI,
                                TextPropertyEnum.NEUTRAL, perceiver1, perceiver2));
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI, TextPropertyEnum.PROPER,
                                                            perceiver1, perceiver2));
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI, TextPropertyEnum.COLLOQUIAL,
                                                                perceiver1, perceiver2));
        /*for(SpeechAct sa : temp)
            Debug.printDebugMessage("Speaker =  " + sa.getSpeaker() + "   Listener = " + sa.getListener());
        */
        return temp;
    }

    //Used as an internal getter so that the variable is reset after reading it!
    private SpeechAct getPlayerChoice(){
        SpeechAct act = playersChoice;
        playersChoice = null;
        return act;
    }

    public boolean isConversationOver(){
        return conversationCompleted;
    }
    
    public void setConversationOver(boolean b){
    	conversationCompleted = b;
    }

    //Called from the Talk state once a speechact has been constructed using the interface
    //This is only used in conversations that contain the player
    public void recieveChoice(SpeechAct sa){
        if(sa == null){
            Debug.printDebugMessage("Speech act received was null", Debug.Channel.CONVERSATION);
        }else {
            Debug.printDebugMessage("Player choice received : " + sa.getLine(), Debug.Channel.CONVERSATION);
            playersChoice = sa;
            //TODO This should most likely be set in the doOneRound() method instead!
            /*if(sa.getSpeechType() == SpeechType.GOODBYE){
                conversationCompleted = true;
            }*/
        }
    }

    //Returns the latest string sent by the 2nd perciever.
    //Intended for use with the view and therefore only get the second perceivers answer
    public String getLatestResponse(){
        ArrayList<SpeechAct>  acts = actHistory.get(perceiver2);
        //Debug.printDebugMessage("current size of responses : " + acts.size());
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
    public IPerceiver getIPerceiver2(){
    	return perceiver2;
    }

}
