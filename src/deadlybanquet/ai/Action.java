package deadlybanquet.ai;

/**
 * This represents an action such as walking somewhere,
 * picking something up or getting or giving information.
 * @author omega
 */
public enum Action {
    MOVETO, 
    PICKUP,
    PUTDOWN, //object somewhere
    ASK,
    INFORM,
    GREET,
    GOODBYE,
    AGREE,
    DISAGREE,
    MURDER,
    GETCONFIRMATION, //Yes-no question
    CONFIRM,
    REQUEST,
    TALKTO, //This one is really just for internal representation of history
    ACTIONPLACEHOLDER;
}
