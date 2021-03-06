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
    private SpeechAct questionToAnswer;
    private SpeechAct playerAnwer;

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
        questionToAnswer = null;
        playerAnwer = null;
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
            if(iteration<maxIterations+1){
                if(iteration==0) {
                    TextPropertyEnum tpe = perceiver1.chooseProperty(perceiver2.getName());
                    SpeechAct greeting = SpeechActFactory.convertIThoughtToSpeechAct(BeingPolite.GREET,
                            tpe, perceiver1, perceiver2);
                    playerAnwer = sendActToPerceiver(perceiver2, greeting);
                    setQuestionToAnswer(greeting);
                    Debug.printDebugMessage("NPC greeted the player",
                            Debug.Channel.CONVERSATION);
                    iteration++;
                }else if(iteration==1){
                    if(playersChoice!=null) {
                        //send the selected getting back to the npc
                        sendActToPerceiver(perceiver1, getPlayerChoice());
                        // Send the intended question and save the cache the answer
                        playerAnwer = sendActToPerceiver(perceiver2, perceiver1.getIntendedPhrase());
                        setQuestionToAnswer(perceiver1.getIntendedPhrase());
                        Debug.printDebugMessage("Player greeted and NPC asked its question",
                                Debug.Channel.CONVERSATION);
                        iteration++;
                    }
                }else{
                    if(playersChoice!=null){
                        //Send answer to former question and cache npc response
                        SpeechAct response = sendActToPerceiver(perceiver1, getPlayerChoice());
                        iteration++;
                        if(iteration<maxIterations+1) {
                            //Send cached response to the playerBrain
                            playerAnwer = sendActToPerceiver(perceiver2, response);
                            //Set the questionToAnswer that the conversationTree looks at to the response
                            setQuestionToAnswer(response);
                        }


                    }
                }
            }else{
                Debug.printDebugMessage("Conversation reached max iterations", Debug.Channel.CONVERSATION);
                conversationCompleted = true;
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
                    Debug.printDebugMessage("One round of conversation is completed!", Debug.Channel.CONVERSATION);
                    if(answer.getSpeechType()== SpeechType.GOODBYE ||
                            decidedAnswer.getSpeechType() == SpeechType.GOODBYE) {
                        conversationCompleted = true;
                        Debug.printDebugMessage("Goodbye has been said!", Debug.Channel.CONVERSATION);
                    }else{
                        Debug.printDebugMessage(answer.getSpeechType().toString(), Debug.Channel.CONVERSATION);
                    }
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
        IPerceiver speaker = perceiver1;
        IPerceiver listener = perceiver2;
        if(perceiver2.isPlayer()){
            speaker = perceiver2;
            listener = perceiver1;
        }
        tempI.add(thought);
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI,
                                TextPropertyEnum.NEUTRAL, speaker, listener));
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI, TextPropertyEnum.PROPER,
                                             speaker, listener));
        temp.add(SpeechActFactory.convertIThoughtToSpeechAct(tempI, TextPropertyEnum.COLLOQUIAL,
                                                    speaker, listener));
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
        ArrayList<SpeechAct> acts;
        //Get the response of the perceiver that is NOT the player
        if(perceiver1.isPlayer())
            acts = actHistory.get(perceiver2);
        else
            acts = actHistory.get(perceiver1);
        //Debug.printDebugMessage("current size of responses : " + acts.size());
        if(acts.size() == 0)
            return "";      //No act has been sent yet so there is nothing to display
        else
            return acts.get(acts.size()-1).getLine();   //Return latest line of act
    }

    public String printConversation(){
        String temp = "   Printing completed conversation... \n";
        ArrayList<SpeechAct> p1strings = actHistory.get(perceiver1);
        ArrayList<SpeechAct> p2strings = actHistory.get(perceiver2);
        for(int i = 0; i<p1strings.size();i++){
            SpeechAct saP1  = p1strings.get(i);
            temp+= saP1.getSpeaker() + " said: " + saP1.getLine() + "\n";
            if(p2strings.size()>i) {
                SpeechAct saP2 = p2strings.get(i);
                temp += saP2.getSpeaker() + " said: " + saP2.getLine() + "\n";
            }
        }
        return temp;
    }

    private void setQuestionToAnswer(SpeechAct question){
        questionToAnswer =question;
    }

    public SpeechAct getQuestionToAnswer(){
        return questionToAnswer;
    }

    public boolean isPlayerAnswering(){
        return perceiver2.isPlayer();
    }

    public SpeechAct getIntendedAnswerFromPlayer(){
        return playerAnwer;
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
