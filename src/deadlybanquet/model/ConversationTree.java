package deadlybanquet.model;

import deadlybanquet.ai.*;
import deadlybanquet.speech.SpeechAct;
import deadlybanquet.speech.TextPropertyEnum;
import org.newdawn.slick.Input;

import java.util.ArrayList;

/**
 * Created by Hampus on 2016-04-20.
 */
public class ConversationTree {
    private enum State{
        GREETING,
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
    private String who2, what, when;

    //Greeting variables



    public ConversationTree(World model, ArrayList<SpeechAct> greetingAlternatives){
        resetToDefaults();
        currentState = State.GREETING;
        alternatives = greetingAlternatives;
        characters = model.getAllCharacterNames();
    }

    private void resetToDefaults(){
        currentState = State.INITIAL;
        currentCounter = 0;
        who = "";
        who2 = "";
        when = "";
        what = "";
        alternatives = null;
        finalChoice = null;
        hasFinalChoiceBeenFetched = false;
        choiceCompleted = false;
        System.out.println("ConversationTree has been reset");
    }

    public void parseInputForConv(Input input, ConversationModel cm){
        switch (currentState){
            case GREETING :
                parseInputGreeting(input);
                break;
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
            case QUESTION_EVENT_WHO2 :
                parseInputQuestionEventWho2(input);
                break;
            case QUESTION_EVENT_WHEN :
                parseInputQuestionEventWhen(input);
                break;
            case QUESTION_EVENT_WHAT :
                parseInputQuestionEventWhat(input, cm);
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
            case QUESTION_EVENT_WHEN :
                text = getPrintQuestionEventWhen();
                break;
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
        } else if (input.isKeyPressed(Input.KEY_3)) {
            stateToSkip = State.QUESTION_EVENT_WHEN;
        }
        if(stateToSkip != null)
            setCurrentState(State.QUESTION_EVENT_WHO);
    }

    private String getPrintQuestionEvent(){
        String temp ="";
        temp+= "\n  1.  Ask about who did something to someone";
        temp+= "\n  2.  Ask about to whom someone did something";
        temp+= "\n  3.  Ask about when something happened";
        return temp;
    }

    private void parseInputQuestionEventWho(Input input){
        if(stateToSkip != State.QUESTION_EVENT_WHO ) {
            who = chooseAPerson(input);
            if(!who.equals(""))
                setCurrentState(State.QUESTION_EVENT_WHO2);
        }else
            setCurrentState(State.QUESTION_EVENT_WHO2);
    }

    private String getPrintQuestionEventWho(){
        String temp = "";
        temp = getPrintPersonChoice("Choose the perpetrator");
        return temp;
    }

    private void parseInputQuestionEventWho2(Input input){
        if(stateToSkip != State.QUESTION_EVENT_WHO2 ) {
            who2 = chooseAPerson(input);
            if(!who2.equals(""))
                setCurrentState(State.QUESTION_EVENT_WHEN);
        }else
            setCurrentState(State.QUESTION_EVENT_WHEN);
    }

    private String getPrintQuestionEventWho2(){
        String temp = "";
        temp = getPrintPersonChoice("Choose the victim/target?");
        return temp;
    }

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
                setCurrentState(State.QUESTION_EVENT_WHAT);
            }
        }else
            setCurrentState(State.QUESTION_EVENT_WHAT);
    }

    private String getPrintQuestionEventWhen(){
        String temp = "         When did it happen?";
        temp += "\n  1.  During the morning";
        temp += "\n  2.  During the afternoon";
        temp += "\n  3.  During the evening";
        return temp;
    }

    private void parseInputQuestionEventWhat(Input input, ConversationModel cm){
        if (stateToSkip != State.QUESTION_EVENT_WHAT){
            if (input.isKeyPressed(Input.KEY_1)) {
                what = "Dong";
            } else if (input.isKeyPressed(Input.KEY_2)) {
                what = "Natures Prophet";
            } else if (input.isKeyPressed(Input.KEY_3)) {
                what = "Spectre";
            }
            if(!what.equals("")){
                eventDebug();
               // cm.getAllPropertyVariations(new Do(what, who, who2, null)); //TODO fix time
                //setCurrentState(State.TEXT_PROPERTY_CHOICE);

            }
        }else {
            eventDebug();
            //cm.getAllPropertyVariations(new Do(what, who, who2, null)); //TODO fix time
           // setCurrentState(State.TEXT_PROPERTY_CHOICE);


        }
    }

    private String getPrintQuestionEventWhat(){
        String temp = "        Choose the utilized object";
        temp += "\n  1.  Dong";
        temp += "\n  2.  Natures Prophet";
        temp += "\n  3.  Spectre";
        return temp;
    }

    private void eventDebug(){
        String temp = "";
        temp+= "Who = " + who + "     When = " + when + "      Who2 = " + who2
                + "      What = " + what;
        System.out.println(temp);
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
            temp += "\n  " + (i+1) + ".  \"" + alternatives.get(i).getLine() + "\"";
        }
        temp += "\n  4.  abort mission";
        return temp;
    }

    public boolean isChoiceCompleted(){
        return choiceCompleted;
    }

    public SpeechAct getFinalChoice(){
        if(choiceCompleted) {
            System.out.println("Final choice fetched : " + finalChoice.getLine());
            //resetToDefaults();          //choice has been recorded, now reset the tree
            //TODO NEED A COPY FUNCTION FOR SPEECHACTS
            hasFinalChoiceBeenFetched = true;
            return finalChoice;
        }
        else
            return null;                //This should never happen, so a crash is intended
    }

    private String chooseAPerson(Input input){
        String who = "";
        if(input.isKeyPressed(Input.KEY_1)){
            System.out.println("Name chosen = " + characters.get((currentCounter)));
            who = characters.get(currentCounter);
        } else if(input.isKeyPressed(Input.KEY_2) && characters.size()>currentCounter+1) {
            System.out.println("Name chosen = " + characters.get((currentCounter + 1)));
            who = characters.get(currentCounter + 1);
        } else if(input.isKeyPressed(Input.KEY_3) && characters.size()>currentCounter+2) {
            System.out.println("Name chosen = " + characters.get((currentCounter + 2)));
            who = characters.get(currentCounter + 2);
        } else if(characters.size()>currentCounter+3) {
            if (input.isKeyPressed((Input.KEY_4))) {
                currentCounter += 3;
                System.out.println("currentCounter has been set to " + currentCounter);
            } else if (input.isKeyPressed(Input.KEY_5) && currentCounter >= 3) {
                currentCounter -= 3;
                System.out.println("currentCounter has been set to " + currentCounter);
            }
        }else if(input.isKeyPressed(Input.KEY_4) && currentCounter>=3) {
            currentCounter-=3;
            System.out.println("currentCounter has been set to " + currentCounter);
        }
        return who;
    }

    private String getPrintPersonChoice(String heading){
        String temp = "       Choose which person to ask about ";
        if(!heading.equals(""))
            temp = heading;
        int numberDisplayed = 1;
        for(int i = currentCounter; i<characters.size() && i<currentCounter+3; i++) {
            temp += "\n  " + numberDisplayed + ".  " + characters.get(i);
            numberDisplayed++;
        }
        if(characters.size()>currentCounter+3) {
            temp += "\n  " + numberDisplayed + ".  Next 3 names ";
            numberDisplayed++;
        }
        if(currentCounter >= 3) {
            temp += "\n  " + numberDisplayed + ".  Previous 3 names";
        }
        return temp;
    }

    private void setCurrentState(State newState){
        String debug = "Changing state from " + currentState.toString();
        currentState = newState;
        debug += " to " + currentState.toString();
        System.out.println(debug);
    }

    private void setFinalChoice(SpeechAct sa) {
        if (sa != null) {
            finalChoice = sa;
            System.out.println("Final choice has been set to:  " + finalChoice.getLine());
            choiceCompleted = true;
            setCurrentState(State.FINISHED);
        }
    }
}
