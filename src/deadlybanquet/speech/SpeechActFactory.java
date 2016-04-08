package deadlybanquet.speech;

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

    private String addWords(String line){
        if(line.contains("#")){
            String temp = line;
            //ArrayList<String> list = new ArrayList<>();
            String list[]=temp.split(" ");
            for(int i =0;i<list.length;i++){
                if(list[i].contains("#")){
                    if(list[i].equals("#epitet")){

                    }else if(list[i].equals("#person")){

                    }else if(list[i].equals("#location")){

                    }else{
                        System.err.println("did not recognise the placeholder: "+list[i]);
                    }
                }
            }
        String ret="";
        for(int k = 0;k<list.length-1;k++){
            ret=ret+list[k]+" ";
        }
        ret=ret+list[list.length-1];
        return ret;

        }else{
            // the line is good to go.
            return line;
        }
    }


}
