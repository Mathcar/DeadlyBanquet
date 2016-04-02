package deadlybanquet.speech;

import deadlybanquet.ai.IThought;
import java.util.ArrayList;

/**
 *
 * @author omega
 */
public class SpeechActFactory {
    /**
     * 
     * @param intendedcontent the intended content.
     * @return a speech act which conveys as much as possible of the intended content,
     * using multiple sentences where necessary. If content could not be conveyed, return error.
     */
    public static SpeechAct makeSpeechAct(ArrayList<IThought> intendedcontent, String speaker){
        System.out.println(speaker + " says:");
        for (IThought i: intendedcontent){
            System.out.println(i);
        }
        return null;
    }
}
