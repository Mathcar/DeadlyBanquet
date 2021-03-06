package deadlybanquet.ai;

/**
 * Marker interface for thoughts. Remember this is a marker interface, even though it has methods.
 * You are expected to use methods on objects which do not appear here.
 * A thought may for example be transmitted via SpeechActs, used as prerequisite or postcondition
 * or used to store data inside the AI if there is no more useful data structure available.
 * This is the basic unit of information. All information known to an AI should be 
 * possible to express using an IThought.
 * @author omega
 */
public interface IThought{
    /**
     * 
     * @param i - IThought with the information to match on. Contains placeholder in places
     * where one is looking for any value. Null values are matched exactly. The exception are things
     * of type IThought where the placeholder is currently null. It is not possible to match on
     * values of type double, since there would be no particular point to this.
     * This also implies that it is not possible to search for particular PADs.
     * @return whether the IThought contains the desired information
     */
    public boolean contains(IThought i);
    
    /**
     * Sets the any placeholder to null.
     */
    public void setPlaceHolderToNull();
    
    public double getCertainty();
    
    public void setCertainty(double i);
    
    public boolean isQuestion();
    
    public IThought copy();

    public Boolean dontKnow();
    
    /**
     * 
     * Compares this object with the specified object for order. 
     * @Return a negative integer, zero, or a positive integer as this object is less than, 
     * equal to, or greater than the specified object.
     * Note that this implementation has no relationship with the equals method.
     */
    public int compareTo(IThought i);
    
    public String debugMessage();
}
