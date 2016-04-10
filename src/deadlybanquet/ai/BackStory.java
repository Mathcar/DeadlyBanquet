package deadlybanquet.ai;

/**
 * A place for facts which do not fit in any other IThought implementation
 * and which do not need extra data.
 * If you want to represent information which needs data, make a new implementation of IThought
 * Feel free to add as much as you like to this enum!
 * Stuff in this IThought is just for flavour and should not change during the course of the game;
 * everything in here is considered true and it is just a matter of knowing it!
 * @author omega
 */
public enum BackStory implements IThought{
    SNOWEDIN, NOMOBILESIGNAL;
    //NOTE: DOTDOTDOT is gone; it just struck me that a SpeechAct with an empty content 
    //list will do exactly the same thing.
    @Override
    public boolean contains(IThought i) {
        if(!this.getClass().equals(i.getClass())) return false;
        return this==i;
    }

    @Override
    public void setPlaceHolderToNull() {
        //No placeholders here
    }

    @Override
    public double getCertainty() {
        return 1;
    }
}
