package deadlybanquet.speech;


import deadlybanquet.Talkable;
import deadlybanquet.ai.*;
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

    IPerceiver talker;
    public SpeechActFactory(IPerceiver A, IPerceiver B){
        this.A=A;
        this.B=B;
        talker=this.A;
    }

    private void changeTalker(){
        if(talker.equals(A)){
            talker=B;
        }else{
            talker=A;
        }
    }

    private IPerceiver getListener(){
        if(talker.equals(A)){
            return B;
        }else{
            return A;
        }
    }

    public SpeechAct2 generateSpeechAct(IPerceiver talker, ArrayList<IThought> listOfIThought){
        this.talker=talker;
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

    public SpeechAct2 convertIThoughtToSpeechAct(IThought i, TextPropertyEnum prop){
        SpeechAct2 temp = new SpeechAct2();
        SpeechActHolder holder = SpeechActHolder.getInstance();
        if(i instanceof BeingPolite){
            if(i.equals(BeingPolite.GREET)){
                ArrayList<SpeechInfo> list = holder.getGreetingFrase();
                String text = i.toString(); // In case something dose not exist, give it the toString value
                for(int k=0;k<list.size();k++){
                    if(list.get(k).getTextProperty().equals(prop)&&list.get(k).getSpeechType().equals(SpeechType.GREET)){
                        text=list.get(k).getText();
                    }
                }
                temp = new SpeechAct2(text,talker.getName(),getListener().getName(),SpeechType.GREET,prop);
            }
            else if(i.equals(BeingPolite.GOODBYE)) {
                ArrayList<SpeechInfo> list = holder.getGreetingFrase();
                String text = i.toString(); // In case something dose not exist, give it the toString value
                for (int k = 0; k < list.size(); k++) {
                    if (list.get(k).getTextProperty().equals(prop) && list.get(k).getSpeechType().equals(SpeechType.GOODBYE)) {
                        text = list.get(k).getText();
                    }
                }
                temp = new SpeechAct2(text, talker.getName(), getListener().getName(), SpeechType.GOODBYE, prop);
            }
            else{
                System.err.println("SpeechActFactory: What i want to say is not yet implemented");
            }
        }else if(i instanceof Whereabouts){
            temp = new SpeechAct2();
            if(i.isQuestion()){
                ArrayList<SpeechInfo> list = holder.getQuestionFrase();
                String text = i.toString();
                if(((Whereabouts) i).getCharacter().equals("")){
                    // who is in getRoom()
                    for(int k=0;k<list.size();k++){
                        SpeechInfo si=list.get(k);
                        if(si.getSpeechType().equals(SpeechType.WHERE_LOCATION)&&si.getTextProperty().equals(prop)){
                            text=si.getText();
                            break;
                        }
                    }
                    temp = new SpeechAct2(text,talker.getName(),getListener().getName(),SpeechType.WHERE_LOCATION,prop);
                }else if(((Whereabouts) i).getRoom().equals("")){
                    // where is getCharacter()
                    for(int k=0;k<list.size();k++){
                        SpeechInfo si = list.get(k);
                        if(si.getSpeechType().equals(SpeechType.WHERE_PERSON)&&si.getTextProperty().equals(prop)){
                            text=si.getText();
                            break;
                        }
                    }
                    temp = new SpeechAct2(text,talker.getName(),getListener().getName(),SpeechType.WHERE_PERSON,prop);
                }
            }else{ // not a question!
                //Some statement about the location of someone
            }
        }else if(i instanceof Opinion){
            if(i.isQuestion()){
                ;
            }
        }


        if(temp.isDead()){
            //Something terrible has happend.
            System.err.println("Could not create SpeeechAct from IThought [SpeechActFactory]");
            System.exit(0);
        }
        return temp;

    }


}
