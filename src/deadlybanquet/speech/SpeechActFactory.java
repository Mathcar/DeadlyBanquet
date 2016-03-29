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
     * @return a speech act which conveys as much as possible of the intended content.
     * The result of getContent() should be the list of things actually conveyed.
     * For example: If the input contains the information "Bob is in the living room"
     * and "I hate Bob" then the result could have the String "That horrible Bob is in the living room."
     * and getContent() contains the original input array.
     * If instead the intended content contains "Mary is cute" and "It's snowing", then the result
     * would be just "Mary is cute" and getContent() then returns only the IThought for "Mary is cute"
     * It would be convenient for this method to attempt to express as much as possible of the 
     * intended content (as many items as possible) and to prioritize the start of the list before 
     * the end, if there are several possibilities expressing the same number of IThoughts.
     * Should return null if no IThoughts were possible to express (for example, if intendedcontent is
     * "Explanation of Einstein's relativity theory" then the generator will not be able to turn this
     * into words and must return null.
     */
    public static SpeechAct makeSpeechAct(ArrayList<IThought> intendedcontent, String speaker){
        System.out.println(speaker + " says:");
        for (IThought i: intendedcontent){
            System.out.println(i);
        }
        return null;
    }
}
