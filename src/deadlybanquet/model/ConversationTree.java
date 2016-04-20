package deadlybanquet.model;

import org.newdawn.slick.Input;

/**
 * Created by Hampus on 2016-04-20.
 */
public class ConversationTree {
    private enum State{
        INITIAL,
        QUESTION,
        GOODBYE,
        QUESTION_LOCATION,
        QUESTION_OPINION,
        QUESTION_EVENT
    }

    private State currentState;
    //true once sufficient option to create an IThought has been made
    private boolean choiceCompleted;

    public ConversationTree(){
        currentState = State.INITIAL;
    }

    public void parseInputForConv(Input input){
        switch (currentState){
            case INITIAL :
                parseInputInitial(input);
                break;
            case QUESTION :
                parseInputQuestion(input);
        }
    }

    private void parseInputInitial(Input input){
        String debug = "Changing state from " + currentState.toString();
        if(input.isKeyPressed(Input.KEY_1)){
            currentState = State.QUESTION;
            debug += " to " + currentState.toString();
            System.out.println(debug);
        } else if(input.isKeyPressed(Input.KEY_2)) {
            currentState = State.GOODBYE;
            debug += " to " + currentState.toString();
            System.out.println(debug);
        }
    }

    private void parseInputQuestion(Input input){
        String debug = "Changing state from " + currentState.toString();
        if(input.isKeyPressed(Input.KEY_1)){
            currentState = State.QUESTION_LOCATION;
            debug += " to " + currentState.toString();
            System.out.println(debug);
        } else if(input.isKeyPressed(Input.KEY_2)) {
            currentState = State.QUESTION_OPINION;
            debug += " to " + currentState.toString();
            System.out.println(debug);
        } else if(input.isKeyPressed(Input.KEY_3)) {
            //currentState = State.QUESTION_EVENT;      //TODO On ice for now
            debug += " to " + currentState.toString();
            System.out.println(debug);
        }
    }

    public boolean isChoiceCompleted(){
        return choiceCompleted;
    }
}
