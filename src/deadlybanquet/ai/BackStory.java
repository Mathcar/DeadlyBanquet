package deadlybanquet.ai;

/**
 * A place for facts which do not fit in any other IThought implementation
 * and which do not need extra data.
 * If you want to represent information which needs data, make a new implementation of IThought
 * Feel free to add as much as you like to this enum!
 * @author omega
 */
public enum BackStory implements IThought{
    SNOWEDIN, NOMOBILESIGNAL;
}
