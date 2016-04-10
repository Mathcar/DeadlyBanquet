package deadlybanquet.ai;

/**
 * Enum for actions which do not need any extra information,
 * in this case politeness phrases.
 * @author omega
 */
public enum BeingPolite implements IThought{
    GREET, GOODBYE, THANKS, THANKSANYWAY;

    @Override
    public boolean contains(IThought i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setPlaceHolderToNull() {
        //No placeholders here
    }

    @Override
    public double getCertainty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isQuestion() {
        return false;
    }
}
