package deadlybanquet.speech;


import deadlybanquet.ai.*;

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

    public SpeechAct generateSpeechAct(IPerceiver talker, ArrayList<IThought> listOfIThought){
        this.talker=talker;
        SpeechAct temp;
        //blabla generate a speechAct her
        temp = new SpeechAct("hello there #name");
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

    public SpeechAct convertIThoughtToSpeechAct(IThought i, TextPropertyEnum prop){
        ArrayList<IThought> IThoughtList = new ArrayList<>();
        IThoughtList.add(i);
        SpeechAct temp = new SpeechAct();
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
                temp = new SpeechAct(text,talker.getName(),getListener().getName(),SpeechType.GREET,prop,IThoughtList);
            }
            else if(i.equals(BeingPolite.GOODBYE)) {
                ArrayList<SpeechInfo> list = holder.getGreetingFrase();
                String text = i.toString(); // In case something dose not exist, give it the toString value
                for (int k = 0; k < list.size(); k++) {
                    if (list.get(k).getTextProperty().equals(prop) && list.get(k).getSpeechType().equals(SpeechType.GOODBYE)) {
                        text = list.get(k).getText();
                    }
                }
                temp = new SpeechAct(text, talker.getName(), getListener().getName(), SpeechType.GOODBYE, prop,IThoughtList);
            }
            else{
                System.err.println("SpeechActFactory: What i want to say is not yet implemented");
            }
        }else if(i instanceof Whereabouts){
            temp = new SpeechAct();
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
                    temp = new SpeechAct(text,talker.getName(),getListener().getName(),SpeechType.WHERE_LOCATION,prop,IThoughtList);
                }else if(((Whereabouts) i).getRoom().equals("")){
                    // where is getCharacter()
                    for(int k=0;k<list.size();k++){
                        SpeechInfo si = list.get(k);
                        if(si.getSpeechType().equals(SpeechType.WHERE_PERSON)&&si.getTextProperty().equals(prop)){
                            text=si.getText();
                            break;
                        }
                    }
                    temp = new SpeechAct(text,talker.getName(),getListener().getName(),SpeechType.WHERE_PERSON,prop,IThoughtList);
                }
            }else{ // not a question!
                String text = i.toString();
                ArrayList<SpeechInfo> list = holder.getInfoFrase();
                for(int k = 0;k<list.size();k++){
                    SpeechInfo si = list.get(k);
                    if(si.getSpeechType().equals(SpeechType.INFO_P_LOCATION)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }
                temp = new SpeechAct(text,talker.getName(),getListener().getName(),SpeechType.INFO_P_LOCATION,prop,IThoughtList);
            }
        }else if(i instanceof Opinion){
            String text=i.toString();
            if(i.isQuestion()){
                ArrayList<SpeechInfo> list =holder.getQuestionFrase();
                for(int k=0;k<list.size();k++){
                    SpeechInfo si = list.get(k);
                    if(si.getSpeechType().equals(SpeechType.OPINION_QUESTION)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }
                temp = new SpeechAct(text,talker.getName(),getListener().getName(),SpeechType.OPINION_QUESTION,prop,IThoughtList);
            }else{
                //info on opinion on someone.
            }
        }


        if(temp.isDead()){
            //Something terrible has happend.
            System.err.println("Could not create SpeeechAct from IThought [SpeechActFactory]");
            System.exit(0);
        }
        return temp;

    }


    /*
    NOT DONE!
    todo: Check ConversationModel Diagram.
     */
    public ArrayList<SpeechAct> getDialogueOptions(TextPropertyEnum prop){
        ArrayList<SpeechAct> temp = new ArrayList<>();

        //add first thing
        IThought i = talker.getMemory().information.first();
        Whereabouts w = new Whereabouts("Tom","hell");
        Opinion o = new Opinion("Tom",new PAD(0.0,0.0,0.0));

        temp.add(convertIThoughtToSpeechAct(i,prop));
        temp.add(convertIThoughtToSpeechAct(w,prop));
        temp.add(convertIThoughtToSpeechAct(o,prop));
        temp.add(convertIThoughtToSpeechAct(BeingPolite.GREET,prop));
        temp.add(convertIThoughtToSpeechAct(BeingPolite.GOODBYE,prop));

        return temp;
    }

}
