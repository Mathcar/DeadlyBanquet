package deadlybanquet.speech;


import deadlybanquet.ai.*;
import deadlybanquet.model.World;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 *
 * @author omega
 */
public class SpeechActFactory {

    private static String parseSpeechAct(String text,IPerceiver speaker,IPerceiver listener){
        String temp = text;
        if(temp.contains("#name")){
            temp = temp.replace("#name",listener.getName());
        }
        return temp;
    }

    private static String parseSpeechAct(String text, Whereabouts w,IPerceiver speaker,IPerceiver listener){
        String temp=text;
        if(temp.contains("#name")){
            temp = temp.replace("#name",listener.getName());
        }
        if(temp.contains("#location")){
            temp = temp.replace("#location",w.getRoom());
        }
        if(temp.contains("#person")){
            temp = temp.replace("#person",w.getCharacter());
        }
        return temp;
    }

    private static String parseSpeechAct(String text, Do d,IPerceiver speaker,IPerceiver listener){
        String temp = text;
        if(temp.contains("#name")){
            temp = temp.replace("#name",listener.getName());
        }
        if(temp.contains("#doer")){
            temp = temp.replace("#doer",d.getDoer());
        }
        /*if(temp.contains("#reciver")){
            temp = temp.replace("#reciver",d.get);
        }*/
        return temp;
    }

    private static String parseSpeechAct(String text, Opinion o,IPerceiver speaker,IPerceiver listener){
        return "NOT DONEEEEEEE GTFO";
    }


    public static SpeechAct convertIThoughtToSpeechAct(ArrayList<IThought> iList, TextPropertyEnum prop,String speaker,String listener){
        //magic
        return convertIThoughtToSpeechAct(iList,prop, World.stringToIPreciver(speaker),World.stringToIPreciver(listener));
    }

    public static SpeechAct convertIThoughtToSpeechAct(ArrayList<IThought> iList, TextPropertyEnum prop,IPerceiver speaker,IPerceiver listener){
        IThought i = iList.get(0);
        ArrayList<IThought> IThoughtList = new ArrayList<>();
        IThoughtList.add(i);
        SpeechAct temp = new SpeechAct();
        SpeechActHolder holder = SpeechActHolder.getInstance();
        if(i instanceof BeingPolite){
            /*
            BEINGPOLTE
             */
            if(i.equals(BeingPolite.GREET)){
                ArrayList<SpeechInfo> list = holder.getGreetingFrase();
                String text = i.toString(); // In case something dose not exist, give it the toString value
                for(int k=0;k<list.size();k++){
                    if(list.get(k).getTextProperty().equals(prop)&&list.get(k).getSpeechType().equals(SpeechType.GREET)){
                        text=list.get(k).getText();
                    }
                }
                text=parseSpeechAct(text,speaker,listener);
                temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.GREET,prop,IThoughtList);
            }
            else if(i.equals(BeingPolite.GOODBYE)) {
                ArrayList<SpeechInfo> list = holder.getGreetingFrase();
                String text = i.toString(); // In case something dose not exist, give it the toString value
                for (int k = 0; k < list.size(); k++) {
                    if (list.get(k).getTextProperty().equals(prop) && list.get(k).getSpeechType().equals(SpeechType.GOODBYE)) {
                        text = list.get(k).getText();
                    }
                }
                text=parseSpeechAct(text,speaker,listener);
                temp = new SpeechAct(text, speaker.getName(), listener.getName(), SpeechType.GOODBYE, prop,IThoughtList);
            }
            else{
                System.err.println("SpeechActFactory: What i want to say is not yet implemented");
            }
        }else if(i instanceof Whereabouts){
            /*
            WHEREABOUTS
             */
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
                    text=parseSpeechAct(text,(Whereabouts) i,speaker,listener);
                    temp = new SpeechAct(text,listener.getName(),listener.getName(),SpeechType.WHERE_LOCATION,prop,IThoughtList);
                }else if(((Whereabouts) i).getRoom().equals("")){
                    // where is getCharacter()
                    for(int k=0;k<list.size();k++){
                        SpeechInfo si = list.get(k);
                        if(si.getSpeechType().equals(SpeechType.WHERE_PERSON)&&si.getTextProperty().equals(prop)){
                            text=si.getText();
                            break;
                        }
                    }
                    text=parseSpeechAct(text,(Whereabouts) i,speaker,listener);
                    temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.WHERE_PERSON,prop,IThoughtList);
                }
            }else{ // not a question!
                String text = i.toString();
                ArrayList<SpeechInfo> list = holder.getInfoFrase();
                for(int k = 0;k<list.size();k++){
                    SpeechInfo si = list.get(k);
                    if(i.getCertainty()<0.5){
                        if(si.getSpeechType().equals(SpeechType.DONT_KNOW)&&si.getTextProperty().equals(prop)){
                            text=si.getText();
                        }
                    }
                    else if(si.getSpeechType().equals(SpeechType.INFO_P_LOCATION)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }
                text=parseSpeechAct(text,(Whereabouts) i,speaker,listener);
                temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.INFO_P_LOCATION,prop,IThoughtList);
            }
        }else if(i instanceof Opinion){
            /*
            OPINION
             */
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
                text=parseSpeechAct(text,(Opinion) i,speaker,listener);
                temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.OPINION_QUESTION,prop,IThoughtList);
            }else{
                //info on opinion on someone.
                ArrayList<SpeechInfo> list = holder.getInfoFrase();
                for(int k=0;k<list.size();k++){
                    SpeechInfo si = list.get(k);
                    if(si.getSpeechType().equals(SpeechType.INFO_P_OPINION)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }
                text=parseSpeechAct(text,(Opinion) i,speaker,listener);
                temp=new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.INFO_P_OPINION,prop,IThoughtList);
            }
        }else if(i instanceof Do){
            String text=i.toString();
            if(i.isQuestion()){
                ArrayList<SpeechInfo> list = holder.getQuestionFrase();
                if(((Do) i).getDoer().equals("")){
                    //question about who did something with something at some time
                    //and
                    //question about who did something to someone at some time
                }else if(((Do) i).getWithWhat().equals("")){

                    //question about with what someone did to something at sometime.
                    //and
                    //question about to whom someone did something at sometime
                }else if(((Do) i).getWhen().isPlaceHolder()){
                    //question about when someone did something with something.
                    //and
                    //questiona about when someone did something with someone.

                    //OBS. alla dessa 6 frågot kommer behöva en egen distinkt SPEACHTYPE
                }
            }else{
                ArrayList<SpeechInfo> list = holder.getInfoFrase();
                for(int k = 0;k<list.size();k++){
                    SpeechInfo si=list.get(k);
                    if(si.getSpeechType().equals(SpeechType.EVENT_INFO)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }
                text=parseSpeechAct(text,(Do) i,speaker,listener);
                temp=new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.EVENT_INFO,prop,IThoughtList);
            }
        }


        if(temp.isDead()){
            //Something terrible has happend.
            //System.err.println("Could not create SpeeechAct from IThought [SpeechActFactory]");
            //System.exit(0);
            temp = new SpeechAct(i.toString(),speaker.getName(),listener.getName(),SpeechType.DONT_KNOW,prop,IThoughtList);
        }
        return temp;

    }


    /*
    NOT DONE!
    todo: Check ConversationModel Diagram.
     */
    public static ArrayList<SpeechAct> getDialogueOptions(IPerceiver speaker, IPerceiver listener,Boolean first){
        ArrayList<SpeechAct> temp = new ArrayList<>();

        //add first thing
        IThought i = speaker.getMemory().information.first();
        Whereabouts w = new Whereabouts("Tom","hell");
        Opinion o = new Opinion("Tom",new PAD(0.0,0.0,0.0));
        //TODO FUL KOD, DETTA MÅSTE FIXAS, TOG BORT prop SOM PARAMETER
        TextPropertyEnum prop = TextPropertyEnum.NEUTRAL;
        ArrayList<IThought> list = new  ArrayList<>();
        if(first==true){
            list.add(BeingPolite.GREET);
            temp.add(convertIThoughtToSpeechAct(list,prop,speaker,listener));
        }else{
            list.add(i);
            temp.add(convertIThoughtToSpeechAct(list,prop,speaker,listener));
            list.clear();
            list.add(w);
            temp.add(convertIThoughtToSpeechAct(list,prop,speaker,listener));
            list.clear();
            list.add(o);
            temp.add(convertIThoughtToSpeechAct(list,prop,speaker,listener));
            list.clear();
            list.add(BeingPolite.GOODBYE);
            temp.add(convertIThoughtToSpeechAct(list,prop,speaker,listener));
        }



        return temp;
    }

}
