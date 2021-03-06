package deadlybanquet.model;

import deadlybanquet.ai.*;
import deadlybanquet.speech.SpeechAct;
import deadlybanquet.speech.SpeechActFactory;
import deadlybanquet.speech.TextPropertyEnum;
import org.newdawn.slick.Input;

import java.util.ArrayList;

/**
 * Created by Hampus on 2016-04-20.
 */
public class ConversationTree {
    private enum State{
        GREETING,
        ANSWER,
        INITIAL,
        QUESTION,
        GOODBYE,
        QUESTION_LOCATION,
        QUESTION_OPINION,
        QUESTION_EVENT,
        QUESTION_EVENT_WHO,
        QUESTION_EVENT_WHO2,
        QUESTION_EVENT_WHAT,
        QUESTION_EVENT_WHEN,
        QUESTION_EVENT_ITEM,
        TEXT_PROPERTY_CHOICE,
        FINISHED
    }

    private State currentState;
    //true once teh finalchoice has been set
    private boolean choiceCompleted;
    private SpeechAct finalChoice;

    //Question Location variables neccessary
    private ArrayList<String> characters;
    private int currentCounter;
    private String who;     //current person selected


    private ArrayList<SpeechAct> alternatives;
    private boolean hasFinalChoiceBeenFetched;

    //Event variables
    private State stateToSkip;
    private String who2, item;
    private Do.What what;

    //Greeting variables

    //DEBUG VARIABLES
    private ArrayList<String> items;



    public ConversationTree(World model, ArrayList<SpeechAct> greetingAlternatives){
        resetToDefaults();
        currentState = State.GREETING;
        alternatives = greetingAlternatives;
        characters = model.getAllCharacterNames();
        items = new ArrayList<>();
        items.add("Knife");
        items.add("Machine Gun");
        items.add("M4A1");
        items.add("Lawnmower");
    }

    private void resetToDefaults(){
        currentState = State.INITIAL;
        currentCounter = 0;
        who = "";
        who2 = "";
        stateToSkip = null;
        //when = "";
        what = null;
        alternatives = null;
        finalChoice = null;
        hasFinalChoiceBeenFetched = false;
        choiceCompleted = false;
        Debug.printDebugMessage("ConversationTree has been reset", Debug.Channel.CONVERSATION);
    }

    public void parseInputForConv(Input input, ConversationModel cm){
        switch (currentState){
            case GREETING :
                parseInputGreeting(input);
                break;
            case ANSWER :
                parseInputAnswer(input,cm);
            case INITIAL :
                parseInputInitial(input, cm);
                break;
            case QUESTION :
                parseInputQuestion(input);
                break;
            case QUESTION_LOCATION :
                parseInputQuestionLocation(input, cm);
                break;
            case QUESTION_OPINION :
                parseInputQuestionOpinion(input, cm);
                break;
            case QUESTION_EVENT :
                parseInputQuestionEvent(input);
                break;
            case QUESTION_EVENT_WHO :
                parseInputQuestionEventWho(input);
                break;
            case QUESTION_EVENT_ITEM :
                parseInputQuestionEventItem(input, cm);
                break;
            case QUESTION_EVENT_WHO2 :
                parseInputQuestionEventWho2(input, cm);
                break;
            /*
            case QUESTION_EVENT_WHEN :
                parseInputQuestionEventWhen(input);
                break;
                */
            case QUESTION_EVENT_WHAT :
                parseInputQuestionEventWhat(input);
                break;
            case GOODBYE :
                parseInputGoodbye(input);
                break;
            case TEXT_PROPERTY_CHOICE :
                parseInputTextProperty(input);
                break;
            case FINISHED :
                if(hasFinalChoiceBeenFetched){
                    resetToDefaults();          //Would be nicer to do this during the fetch,
                                                //TODO Need a copy for SpeechAct to fix this
                    if(cm.isPlayerAnswering()){
                        setCurrentState(State.ANSWER);
                    }
                }
                break;
        }
    }

    //Might need to be changed into a render function of sorts instead
    public String getPrint(){
        //TODO Add switch case over the states
        String text = "No state print function found :/";
        switch (currentState){
            case GREETING :
                text = getPrintGreeting();
                break;
            case ANSWER :
                text = getPrintAnswer();
                break;
            case INITIAL :
                text = getPrintInitial();
                break;
            case QUESTION :
                text = getPrintQuestion();
                break;
            case QUESTION_LOCATION :
                text = getPrintQuestionLocation();
                break;
            case QUESTION_OPINION :
                text = getPrintQuestionOpinion();
                break;
            case QUESTION_EVENT :
                text = getPrintQuestionEvent();
                break;
            case QUESTION_EVENT_WHO :
                text = getPrintQuestionEventWho();
                break;
            case QUESTION_EVENT_WHO2 :
                text = getPrintQuestionEventWho2();
                break;
            case QUESTION_EVENT_ITEM :
                text = getPrintQuestionEventItem();
                break;
           /* case QUESTION_EVENT_WHEN :
                text = getPrintQuestionEventWhen();
                break;*/
            case QUESTION_EVENT_WHAT :
                text = getPrintQuestionEventWhat();
                break;
            case GOODBYE :
                text = getPrintGoodbye();
                break;
            case TEXT_PROPERTY_CHOICE :
                text = getPrintTextProperty();
                break;
        }
        return text;
    }

    private void parseInputGreeting(Input input){
        if(input.isKeyPressed(Input.KEY_1)){
            setFinalChoice(alternatives.get(0));
        }
        else if(input.isKeyPressed(Input.KEY_2)){
            setFinalChoice(alternatives.get(1));
        }
        else if(input.isKeyPressed(Input.KEY_3)){
            setFinalChoice(alternatives.get(2));
        }
    }

    private String getPrintGreeting(){
        String temp = "";
        for(int i = 0; i<3; i++){
            temp += "  " + (i+1) + ".  " + alternatives.get(i).getLine() + " \n";
        }
        return temp;
    }

    private void parseInputAnswer(Input input, ConversationModel cm){
        if(alternatives== null){
            alternatives = new ArrayList<>();
            Debug.printDebugMessage("Answer alternative list size was 0, so now creating answers!", Debug.Channel.CONVERSATION);
            alternatives = formAnswers(cm.getQuestionToAnswer(), cm);
        }else{
            SpeechAct act = chooseSpeechActFromList(input, alternatives, "Answer chosen = ");
            if(act != null){
                alternatives = cm.getAllPropertyVariations(act.getContent().get(0));
                setCurrentState(State.TEXT_PROPERTY_CHOICE);

            }
        }
    }

    private ArrayList<SpeechAct> formAnswers(SpeechAct question, ConversationModel cm){
        ArrayList<SpeechAct> acts = new ArrayList<SpeechAct>();
        //Add I Don't know
        Whereabouts w = new Whereabouts("","",-1,new TimeStamp(0,0,0));
        acts.add(SpeechActFactory.convertIThoughtToSpeechAct(w, TextPropertyEnum.NEUTRAL, cm.getIPerceiver2(), cm.getIPerceiver1()));
        //Add I don't care
            //TODO No idea how to represent this in text

        //Add Real asnwer
        if(cm.getIntendedAnswerFromPlayer()!=null)
            acts.add(cm.getIntendedAnswerFromPlayer());
        //Add Lie?

        return acts;
    }


    private String getPrintAnswer(){
        String temp = "ANSWER ALTERNATIVES OUGHTA BE PRINTED HERE";
        if( alternatives != null)
            temp = getPrintChooseSpeechAct("\tChoose your answer: ", alternatives);
        return temp;
    }

    private void parseInputInitial(Input input, ConversationModel cm){
        if(input.isKeyPressed(Input.KEY_1)){
            setCurrentState(State.QUESTION);

        } else if(input.isKeyPressed(Input.KEY_2)) {
            setCurrentState(State.GOODBYE);
            alternatives = cm.getAllPropertyVariations(BeingPolite.GOODBYE);
        }
    }

    private String getPrintInitial(){
        String temp = "";
        temp+= "  1.  Ask a question ";
        temp += "\n  2.  Say Goodbye ";
        return temp;
    }

    private void parseInputGoodbye(Input input){
        parseInputGreeting(input);      //These two are identical no need for copy-paste
    }

    private String getPrintGoodbye(){
        return getPrintGreeting();
    }

    private void parseInputQuestion(Input input){
        if(input.isKeyPressed(Input.KEY_1)){
            setCurrentState(State.QUESTION_LOCATION);
        } else if(input.isKeyPressed(Input.KEY_2)) {
            setCurrentState(State.QUESTION_OPINION);
        } else if(input.isKeyPressed(Input.KEY_3)) {
            setCurrentState(State.QUESTION_EVENT);      //TODO On ice for now
        }
    }

    private String getPrintQuestion(){
        String temp = "";
        temp += "  1. Ask about someones whereabouts";
        temp += "\n  2. Ask about the adversaries opinion of someone";
        temp += "\n  3. Ask about an event.";
        return temp;
    }

    private void parseInputQuestionLocation(Input input, ConversationModel cm){
        who = chooseAPerson(input);
        //A character has been chosen
        if(!who.equals("")){
            setCurrentState(State.TEXT_PROPERTY_CHOICE);
            //Create an IThought of Whereabouts type with a null location and then get
            //the resulting speechacts for NEUTRAL, PROPER and COLLOQUIAL
            alternatives = cm.getAllPropertyVariations(new Whereabouts(who, ""));
        }
    }



    private String getPrintQuestionLocation(){
        return getPrintPersonChoice("");
    }

    private void parseInputQuestionOpinion(Input input, ConversationModel cm){
        who = chooseAPerson(input);
        if(!who.equals("")) {
            alternatives = cm.getAllPropertyVariations(new Opinion(who, PAD.placeholderPAD()));
            setCurrentState(State.TEXT_PROPERTY_CHOICE);
        }
    }

    private String getPrintQuestionOpinion(){
        return getPrintPersonChoice("");
    }

    private void parseInputQuestionEvent(Input input) {
        if (input.isKeyPressed(Input.KEY_1)) {
            stateToSkip = State.QUESTION_EVENT_WHO;
        } else if (input.isKeyPressed(Input.KEY_2)) {
            stateToSkip = State.QUESTION_EVENT_WHO2;
        } /*else if (input.isKeyPressed(Input.KEY_3)) {
            stateToSkip = State.QUESTION_EVENT_WHEN;
        }*/
        if(stateToSkip != null)
            setCurrentState(State.QUESTION_EVENT_WHAT);
    }

    private String getPrintQuestionEvent(){
        String temp ="";
        temp+= "\n  1.  Ask about who did something to someone";
        temp+= "\n  2.  Ask about to whom someone did something";
        //temp+= "\n  3.  Ask about when something happened";
        return temp;
    }

    private void parseInputQuestionEventWho(Input input){
        if(stateToSkip != State.QUESTION_EVENT_WHO ) {
            who = chooseAPerson(input);
            if(!who.equals("")) {
                if(what == Do.What.PICKUP)
                    setCurrentState(State.QUESTION_EVENT_ITEM);
                else
                    setCurrentState(State.QUESTION_EVENT_WHO2);
            }
        }else {
            if(what == Do.What.PICKUP)
                setCurrentState(State.QUESTION_EVENT_ITEM);
            else
                setCurrentState(State.QUESTION_EVENT_WHO2);
        }
    }

    private String getPrintQuestionEventWho(){
        String temp = "";
        temp = getPrintPersonChoice("Choose the perpetrator");
        return temp;
    }

    private void parseInputQuestionEventWho2(Input input, ConversationModel cm){
        if(stateToSkip != State.QUESTION_EVENT_WHO2 ) {
            who2 = chooseAPerson(input);
            if(!who2.equals("")) {
                alternatives = cm.getAllPropertyVariations(new Do(what, who, who2, null)); //TODO fix time
                setCurrentState(State.TEXT_PROPERTY_CHOICE);
            }
        }else {
            alternatives = cm.getAllPropertyVariations(new Do(what, who, who2, null)); //TODO fix time
            setCurrentState(State.TEXT_PROPERTY_CHOICE);
        }
    }

    private String getPrintQuestionEventWho2(){
        String temp = "";
        temp = getPrintPersonChoice("Choose the victim/target?");
        return temp;
    }

    private void parseInputQuestionEventItem(Input input, ConversationModel cm){
        if(stateToSkip != State.QUESTION_EVENT_ITEM ) {
            item = chooseAnItem(input);
            if(!item.equals("")) {
                cm.getAllPropertyVariations(new Do(what, who, who2, null)); //TODO fix time
                 setCurrentState(State.TEXT_PROPERTY_CHOICE);
            }
        }else {
            cm.getAllPropertyVariations(new Do(what, who, who2, null)); //TODO fix time
            setCurrentState(State.TEXT_PROPERTY_CHOICE);
        }
    }

    private String getPrintQuestionEventItem(){
        return getPrintItemChoice("");
    }
    /*
    private void parseInputQuestionEventWhen(Input input) {
        if (stateToSkip != State.QUESTION_EVENT_WHEN){
            if (input.isKeyPressed(Input.KEY_1)) {
                when = "Morning";
            } else if (input.isKeyPressed(Input.KEY_2)) {
                when = "Afternoon";
            } else if (input.isKeyPressed(Input.KEY_3)) {
                when = "Evening";
            }
            if(!when.equals("")){
                eventDebug();
                //FINISHED HERE
            }
        }else
            eventDebug();
            //FINISHED HERE;
    }


    private String getPrintQuestionEventWhen(){
        String temp = "         When did it happen?";
        temp += "\n  1.  During the morning";
        temp += "\n  2.  During the afternoon";
        temp += "\n  3.  During the evening";
        return temp;
    }
    */

    private void parseInputQuestionEventWhat(Input input){
        if (stateToSkip != State.QUESTION_EVENT_WHAT){
            if (input.isKeyPressed(Input.KEY_1)) {
                what = Do.What.MOVETO;
            } else if (input.isKeyPressed(Input.KEY_2)) {
                what = Do.What.MURDER;
            } else if (input.isKeyPressed(Input.KEY_3)) {
                what = Do.What.TALKTO;
            }
            else if (input.isKeyPressed(Input.KEY_3)) {
                what = Do.What.PICKUP;
            }
            if(what != null){
                //TODO swap to item or person
                setCurrentState(State.QUESTION_EVENT_WHO);
                //cm.getAllPropertyVariations(new Do(what, who, who2, null)); //TODO fix time
               // setCurrentState(State.TEXT_PROPERTY_CHOICE);
            }
        }else {
            Debug.printDebugMessage("THIS SHOULD NOT HAPPEN (Comment from conversationTree)", Debug.Channel.CONVERSATION);
            setCurrentState(State.QUESTION_EVENT_WHO);
        }
    }

    private String getPrintQuestionEventWhat(){
        String temp = "        Choose the event ";
        temp += "\n  1.  Moved to ";
        temp += "\n  2.  Murdered ";
        temp += "\n  3.  Interacted with";
        return temp;
    }

    private void eventDebug(){
        String temp = "";
        temp+= "Who = " + who +  "      Who2 = " + who2
                + "      What = " + what;
        Debug.printDebugMessage(temp, Debug.Channel.CONVERSATION);
    }

    private void parseInputTextProperty(Input input){
        if(input.isKeyPressed(Input.KEY_1)){
            setFinalChoice(alternatives.get(0));
        } else if(input.isKeyPressed(Input.KEY_2)) {
            setFinalChoice(alternatives.get(1));
        } else if(input.isKeyPressed(Input.KEY_3) ) {
            setFinalChoice(alternatives.get(2));
        }else if(input.isKeyPressed(Input.KEY_4)) {
            resetToDefaults();
        }
    }

    private String getPrintTextProperty(){
        String temp = "        Choose what to say:    ";
        for(int i = 0; i<3; i++){
            temp += "\n " + (i+1) + ". \"" + alternatives.get(i).getLine() + "\"";
        }
        temp += "\n 4. cancel selection";
        return temp;
    }

    public boolean isChoiceCompleted(){
        return choiceCompleted;
    }

    public SpeechAct getFinalChoice(){
        if(choiceCompleted) {
            Debug.printDebugMessage("Final choice fetched : " + finalChoice.getLine(), Debug.Channel.CONVERSATION);
            //resetToDefaults();          //choice has been recorded, now reset the tree
            //TODO NEED A COPY FUNCTION FOR SPEECHACTS
            hasFinalChoiceBeenFetched = true;
            return finalChoice;
        }
        else
            return null;                //This should never happen, so a crash is intended
    }

    private SpeechAct chooseSpeechActFromList(Input input, ArrayList<SpeechAct> choices, String debugLine){
        SpeechAct act = null;
        if(input.isKeyPressed(Input.KEY_1)){
            Debug.printDebugMessage(debugLine + choices.get((currentCounter)).getLine(), Debug.Channel.CONVERSATION);
            act = choices.get(currentCounter);
        } else if(input.isKeyPressed(Input.KEY_2) && choices.size()>currentCounter+1) {
            Debug.printDebugMessage(debugLine + choices.get((currentCounter + 1)).getLine(), Debug.Channel.CONVERSATION);
            act = choices.get(currentCounter + 1);
        } else if(input.isKeyPressed(Input.KEY_3) && choices.size()>currentCounter+2) {
            Debug.printDebugMessage(debugLine + choices.get((currentCounter + 2)).getLine(), Debug.Channel.CONVERSATION);
            act = choices.get(currentCounter + 2);
        } else if(choices.size()>currentCounter+3) {
            if (input.isKeyPressed((Input.KEY_4))) {
                currentCounter += 3;
                Debug.printDebugMessage("currentCounter has been set to " + currentCounter, Debug.Channel.CONVERSATION);
            } else if (input.isKeyPressed(Input.KEY_5) && currentCounter >= 3) {
                currentCounter -= 3;
                Debug.printDebugMessage("currentCounter has been set to " + currentCounter, Debug.Channel.CONVERSATION);
            }
        }else if(input.isKeyPressed(Input.KEY_4) && currentCounter>=3) {
            currentCounter-=3;
            Debug.printDebugMessage("currentCounter has been set to " + currentCounter, Debug.Channel.CONVERSATION);
        }
        return act;
    }

    private String getPrintChooseSpeechAct(String heading, ArrayList<SpeechAct> choices){
        String temp = heading;
        int numberDisplayed = 1;
        for(int i = currentCounter; i<choices.size() && i<currentCounter+3; i++) {
            if(choices.get(i) == null)
                Debug.printDebugMessage("ITS NULL", Debug.Channel.CONVERSATION);
            choices.get(i).getContent();
            temp += "\n  " + numberDisplayed + ".  " + choices.get(i).getLine();
            numberDisplayed++;
        }
        if(choices.size()>currentCounter+3) {
            temp += "\n  " + numberDisplayed + ".  Next 3";
            numberDisplayed++;
        }
        if(currentCounter >= 3) {
            temp += "\n  " + numberDisplayed + ".  Previous 3";
        }
        return temp;
    }


    private String chooseStringFromList(Input input, ArrayList<String> choices, String debugLine){
        String item = "";
        if(input.isKeyPressed(Input.KEY_1)){
            Debug.printDebugMessage(debugLine + choices.get((currentCounter)), Debug.Channel.CONVERSATION);
            item = choices.get(currentCounter);
        } else if(input.isKeyPressed(Input.KEY_2) && choices.size()>currentCounter+1) {
            Debug.printDebugMessage(debugLine + choices.get((currentCounter + 1)), Debug.Channel.CONVERSATION);
            item = choices.get(currentCounter + 1);
        } else if(input.isKeyPressed(Input.KEY_3) && choices.size()>currentCounter+2) {
            Debug.printDebugMessage(debugLine + choices.get((currentCounter + 2)), Debug.Channel.CONVERSATION);
            item = choices.get(currentCounter + 2);
        } else if(choices.size()>currentCounter+3) {
            if (input.isKeyPressed((Input.KEY_4))) {
                currentCounter += 3;
                Debug.printDebugMessage("currentCounter has been set to " + currentCounter, Debug.Channel.CONVERSATION);
            } else if (input.isKeyPressed(Input.KEY_5) && currentCounter >= 3) {
                currentCounter -= 3;
                Debug.printDebugMessage("currentCounter has been set to " + currentCounter, Debug.Channel.CONVERSATION);
            }
        }else if(input.isKeyPressed(Input.KEY_4) && currentCounter>=3) {
            currentCounter-=3;
            Debug.printDebugMessage("currentCounter has been set to " + currentCounter, Debug.Channel.CONVERSATION);
        }
        return item;

    }

    private String getPrintChooseStringFromList(String heading, ArrayList<String> choices){
        String temp = heading;
        int numberDisplayed = 1;
        for(int i = currentCounter; i<choices.size() && i<currentCounter+3; i++) {
            temp += "\n  " + numberDisplayed + ".  " + choices.get(i);
            numberDisplayed++;
        }
        if(choices.size()>currentCounter+3) {
            temp += "\n  " + numberDisplayed + ".  Next 3";
            numberDisplayed++;
        }
        if(currentCounter >= 3) {
            temp += "\n  " + numberDisplayed + ".  Previous 3";
        }
        return temp;
    }

    private String chooseAnItem(Input input){
        return chooseStringFromList(input, items, "item chosen = ");
    }

    private String getPrintItemChoice(String heading){
        String temp = "       Choose which item to ask about ";
        if(!heading.equals(""))
            temp = heading;
        return getPrintChooseStringFromList(temp, items);
    }


    private String chooseAPerson(Input input){
        return chooseStringFromList(input, characters, "Name chosen = ");
    }

    private String getPrintPersonChoice(String heading){
        String temp = "       Choose which person to ask about ";
        if(!heading.equals(""))
            temp = heading;
        return getPrintChooseStringFromList(temp, characters);
    }

    private void setCurrentState(State newState){
        String debug = "Changing state from " + currentState.toString();
        currentState = newState;
        debug += " to " + currentState.toString();
        Debug.printDebugMessage(debug, Debug.Channel.CONVERSATION);
    }

    private void setFinalChoice(SpeechAct sa) {
        if (sa != null) {
            finalChoice = sa;
            Debug.printDebugMessage("Final choice has been set to:  " + finalChoice.getLine(), Debug.Channel.CONVERSATION);
            choiceCompleted = true;
            setCurrentState(State.FINISHED);
        }
    }
}
