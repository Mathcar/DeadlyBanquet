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
    private IThought thought;
    private ArrayList<SpeechAct> alternatives;
    private boolean hasFinalChoiceBeenFetched;

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
        thought = null;
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
            //setCurrentState(State.QUESTION_EVENT);      //TODO On ice for now
        }
    }

    private String getPrintQuestion(){
        String temp = "";
        temp += "  1. Ask about someones whereabouts";
        temp += "\n  2. Ask about the adversaries opinion of someone";
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
        return getPrintPersonChoice();
    }

    private void parseInputQuestionOpinion(Input input, ConversationModel cm){
        who = chooseAPerson(input);
        if(!who.equals("")) {
            alternatives = cm.getAllPropertyVariations(new Opinion(who, PAD.placeholderPAD()));
            setCurrentState(State.TEXT_PROPERTY_CHOICE);
        }
    }

    private String getPrintQuestionOpinion(){
        return getPrintPersonChoice();
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
            temp += "\n  " + (i+1) + ".  " + alternatives.get(i).getLine();
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

    private String getPrintPersonChoice(){
        String temp = "       Choose which person to ask about ";
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
