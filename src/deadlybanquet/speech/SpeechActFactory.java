package deadlybanquet.speech;


import deadlybanquet.Talkable;
import deadlybanquet.ai.NPCBrain;
import deadlybanquet.ai.IThought;
import deadlybanquet.model.Player;

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


    /*
    Returns all the words/names/whatever, that needs to be changed or parsed.
    If the list is empty, no words needs to be parsed and can be directly
    transmitted to the screen.
     */
    private ArrayList<String> wordsToBeParsed(String line){
        ArrayList<String> temp = new ArrayList<>();
        if(line.contains("#")){
            String[] list = line.split(" ");
            for(int i=0;i<list.length;i++){
                if(list[i].contains("#")){
                    temp.add(list[i]);
                }
            }
            return temp;
        }else{
            return temp;
        }
    }


}
