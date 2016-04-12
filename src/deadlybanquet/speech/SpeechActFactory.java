package deadlybanquet.speech;


import deadlybanquet.Talkable;
import deadlybanquet.ai.IPerceiver;
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
        
        if(intendedcontent.isEmpty()) return null;
        System.out.print(speaker + " says: ");
        for (IThought i: intendedcontent){
            System.out.println(i);
        }
        return null;
    }

    IPerceiver A;
    IPerceiver B;

    SpeechAct lastThingSaid;

    IPerceiver talkingRightNow;

    public SpeechActFactory(IPerceiver A, IPerceiver B){
        this.A=A;
        this.B=B;
    }

    public SpeechAct2 generateSpeechAct(IPerceiver talker, ArrayList<IThought> listOfIThought){
        this.talkingRightNow=talker;
        SpeechAct2 temp;
        //blabla generate a speechAct her
        temp = new SpeechAct2("hello there #name");
        //now parse the SpeechAct
        String line=temp.getLine();
        if(wordsToBeParsed(line)!=null){
            line=lineParser(line,wordsToBeParsed(line));
        }
        temp.setLine(line);
        return temp;
    }


    private String lineParser(String line,String word){
        String temp="ERROR VALUE";
        switch(word){
            case "#name":   temp=caseName(line);
                            break;
            case "#person": temp=casePerson(line);
                            break;
            case "#location":temp=caseLocation(line);
                            break;
            default:        System.err.println("Error in SpeechActFactory, could not recognize the placeholder String");
                            break;
        }
        return temp;
    }

    private String caseName(String line){
        //talkingRightNow
        return "NameNotImplemented";
    }

    private String caseLocation(String line){
        return "LocationNotImplemented";
    }

    private String casePerson(String line){
        return "PersonNameNotImplemented";
    }


    /*
    Returns the word that are needed to be parsed, it there is no word that are needed
    return NULL OBS!!! probably change this later.
     */
    private String wordsToBeParsed(String line){
        String temp = "";
        if(line.contains("#")){
            String[] list = line.split(" ");
            for(int i=0;i<list.length;i++){
                if(list[i].contains("#")){
                    temp=list[i];
                }
            }
            return temp;
        }else{
            return null;
        }
    }


}
