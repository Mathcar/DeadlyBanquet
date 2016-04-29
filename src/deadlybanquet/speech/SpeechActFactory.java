package deadlybanquet.speech;


import deadlybanquet.ai.*;
import deadlybanquet.model.Debug;
import deadlybanquet.model.ItemsAndNameSingelton;
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
        if(w.equals(null)){
            System.err.println("Whereabout cobject with text: "+text+"is NULL");
        }
        if(temp.contains("#name")){
            temp = temp.replace("#name",listener.getName());
        }
        if(temp.contains("#location")){
            System.out.println(w);
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
        if(temp.contains("#receiver")){
            temp = temp.replace("#receiver",d.getWithWhat());
        }
        if(temp.contains("#item")){
            temp=temp.replace("#item",d.getWithWhat());
        }
        if(temp.contains("#time")){
            temp=temp.replace("#time",d.getWhen().toString());
        }
        if(temp.contains("#what")){
            temp=temp.replace("#what",d.whatToString());
        }
        return temp;
    }

    private static String parseSpeechAct(String text, Opinion o,IPerceiver speaker,IPerceiver listener){
        if(o.equals(null)){
            System.err.println("Opinion cobject with text: "+text+"is NULL");
        }
        String temp = text;
        if(temp.contains("#name")){
            temp = temp.replace("#name",listener.getName());
        }
        if(temp.contains("#opinion")){
            temp = temp.replace("#opinion",o.getPAD().getOpinion());
        }
        if(temp.contains("#person")){
            temp=temp.replace("#person",o.getPerson());
        }
        return temp;
    }


    public static SpeechAct convertIThoughtToSpeechAct(ArrayList<IThought> iList, TextPropertyEnum prop,String speaker,String listener){
        //magic
        return convertIThoughtToSpeechAct(iList,prop, World.stringToIPerceiver(speaker),World.stringToIPerceiver(listener));
    }

    public static SpeechAct convertIThoughtToSpeechAct(IThought t,TextPropertyEnum prop, IPerceiver speaker,IPerceiver listener){
        ArrayList<IThought> list = new ArrayList<>();
        list.add(t);
        return convertIThoughtToSpeechAct(list,prop,speaker,listener);
    }

    public static SpeechAct convertIThoughtToSpeechAct(IThought t,TextPropertyEnum prop, String speaker,String listener){
        ArrayList<IThought> list = new ArrayList<>();
        list.add(t);
        return convertIThoughtToSpeechAct(list,prop,speaker,listener);
    }

    public static SpeechAct convertIThoughtToSpeechAct(ArrayList<IThought> iList, TextPropertyEnum prop,IPerceiver speaker,IPerceiver listener){
        IThought i;
        if(iList.size()<1){
            i = BeingPolite.GOODBYE;
        }else{
            i = iList.get(0);
        }
        ArrayList<IThought> IThoughtList = new ArrayList<>();
        IThoughtList.add(i);
        SpeechAct temp = new SpeechAct();
        SpeechActHolder holder = SpeechActHolder.getInstance();

        if(i.dontKnow()|| i.getCertainty()<0.0){
            ArrayList<SpeechInfo> list = holder.getInfoFrase();
            String text=i.toString();
            /*for(int k=0;k<list.size();k++){
                SpeechInfo si=list.get(k);
                if(si.getSpeechType().equals(SpeechType.DONT_KNOW)&&si.getTextProperty().equals(prop)){
                    text=si.getText();
                    break;
                }
            }*/
            text=createText(text,list,SpeechType.DONT_KNOW,prop);
            //text=parseSpeechAct(text,speaker,listener);
            return new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.DONT_KNOW,prop,IThoughtList);
        }

        /*Change all the say objects to the actual object*/
        if(i instanceof Say){
            if(((Say)i).content instanceof BeingPolite){
                i=((Say) i).content;
            }else{
                return new SpeechAct("ok.",speaker.getName(),listener.getName(),SpeechType.OK,prop,IThoughtList);
            }
        }

        if(i instanceof BeingPolite){
            /*
            BEINGPOLTE
             */
            if(i.equals(BeingPolite.GREET)){
                ArrayList<SpeechInfo> list = holder.getGreetingFrase();
                String text = i.toString(); // In case something dose not exist, give it the toString value
                /*for(int k=0;k<list.size();k++){
                    if(list.get(k).getTextProperty().equals(prop)&&list.get(k).getSpeechType().equals(SpeechType.GREET)){
                        text=list.get(k).getText();
                    }
                }*/
                text=createText(text,list,SpeechType.GREET,prop);
                text=parseSpeechAct(text,speaker,listener);
                temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.GREET,prop,IThoughtList);
            }
            else if(i.equals(BeingPolite.GOODBYE)) {
                ArrayList<SpeechInfo> list = holder.getGreetingFrase();
                String text = i.toString(); // In case something dose not exist, give it the toString value
                /*for (int k = 0; k < list.size(); k++) {
                    if (list.get(k).getTextProperty().equals(prop) && list.get(k).getSpeechType().equals(SpeechType.GOODBYE)) {
                        text = list.get(k).getText();
                    }
                }*/
                text=createText(text,list,SpeechType.GOODBYE,prop);
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
                    /*for(int k=0;k<list.size();k++){
                        SpeechInfo si=list.get(k);
                        if(si.getSpeechType().equals(SpeechType.WHERE_LOCATION)&&si.getTextProperty().equals(prop)){
                            text=si.getText();
                            break;
                        }
                    }*/
                    text=createText(text,list,SpeechType.WHERE_LOCATION,prop);
                    text=parseSpeechAct(text,(Whereabouts) i,speaker,listener);
                    temp = new SpeechAct(text,listener.getName(),listener.getName(),SpeechType.WHERE_LOCATION,prop,IThoughtList);
                }else if(((Whereabouts) i).getRoom().equals("")){
                    // where is getCharacter()
                    /*for(int k=0;k<list.size();k++){
                        SpeechInfo si = list.get(k);
                        if(si.getSpeechType().equals(SpeechType.WHERE_PERSON)&&si.getTextProperty().equals(prop)){
                            text=si.getText();
                            break;
                        }
                    }*/
                    text=createText(text,list,SpeechType.WHERE_PERSON,prop);
                    text=parseSpeechAct(text,(Whereabouts) i,speaker,listener);
                    temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.WHERE_PERSON,prop,IThoughtList);
                }
            }else{ // not a question!
                String text = i.toString();
                ArrayList<SpeechInfo> list = holder.getInfoFrase();
                /*for(int k = 0;k<list.size();k++){
                    SpeechInfo si = list.get(k);
                    if(si.getSpeechType().equals(SpeechType.INFO_P_LOCATION)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }*/
                text=createText(text,list,SpeechType.INFO_P_LOCATION,prop);
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
                /*for(int k=0;k<list.size();k++){
                    SpeechInfo si = list.get(k);
                    if(si.getSpeechType().equals(SpeechType.OPINION_QUESTION)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }*/
                text=createText(text,list,SpeechType.OPINION_QUESTION,prop);
                text=parseSpeechAct(text,(Opinion) i,speaker,listener);
                temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.OPINION_QUESTION,prop,IThoughtList);
            }else{
                //info on opinion on someone.
                ArrayList<SpeechInfo> list = holder.getInfoFrase();
                /*for(int k=0;k<list.size();k++){
                    SpeechInfo si = list.get(k);
                    if(si.getSpeechType().equals(SpeechType.INFO_P_OPINION)&&si.getTextProperty().equals(prop)){
                        text=si.getText();
                        break;
                    }
                }*/
                text=createText(text,list,SpeechType.INFO_P_OPINION,prop);
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
                    if(ItemsAndNameSingelton.getInstance().getItems().contains(((Do) i).getWithWhat())){
                        //question about who did WHAT with ITEM
                        /*for(int k=0;k<list.size();k++){
                            SpeechInfo si = list.get(k);
                            if(si.getSpeechType().equals(SpeechType.EVENT_QUESTION_WHAT_ITEM)&&si.getTextProperty().equals(prop)){
                                text=si.getText();
                                break;
                            }
                        }*/
                        text=createText(text,list,SpeechType.EVENT_QUESTION_WHAT_ITEM,prop);
                        text=parseSpeechAct(text,(Do) i,speaker,listener);
                        temp=new SpeechAct(text,speaker.getName(),listener.getName(),
                                SpeechType.EVENT_QUESTION_WHAT_ITEM,prop,IThoughtList);
                    }else{
                        //question about who did WHAT to WHOM;
                        /*for(int k=0;k<list.size();k++){
                            SpeechInfo si = list.get(k);
                            if(si.getSpeechType().equals(SpeechType.EVENT_QUESTION_WHAT_RECEIVER)&&si.getTextProperty().equals(prop)){
                                text=si.getText();
                                break;
                            }
                        }*/
                        text=createText(text,list,SpeechType.EVENT_QUESTION_WHAT_RECEIVER,prop);
                        text=parseSpeechAct(text,(Do) i,speaker,listener);
                        temp=new SpeechAct(text,speaker.getName(),listener.getName(),
                                SpeechType.EVENT_QUESTION_WHAT_RECEIVER,prop,IThoughtList);
                    }
                }else if(((Do) i).getWithWhat().equals("")){
                    if(((Do) i).whatWhatisAbout().equals("person")){
                        /*for(int k=0;k<list.size();k++){
                            SpeechInfo si = list.get(k);
                            if(si.getSpeechType().equals(SpeechType.EVENT_QUESTION_WHAT_DOER)&&si.getTextProperty().equals(prop)){
                                text=si.getText();
                                break;
                            }
                        }*/
                        text=createText(text,list,SpeechType.EVENT_QUESTION_WHAT_DOER,prop);
                        text=parseSpeechAct(text,(Do)i,speaker,listener);
                        temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.EVENT_QUESTION_WHAT_DOER,prop,IThoughtList);
                    }else{
                        /*for(int k=0;k<list.size();k++){
                            SpeechInfo si = list.get(k);
                            if(si.getSpeechType().equals(SpeechType.EVENT_QUESTION_DOER_WHAT)&&si.getTextProperty().equals(prop)){
                                text=si.getText();
                                break;
                            }
                        }*/
                        text=createText(text,list,SpeechType.EVENT_QUESTION_DOER_WHAT,prop);
                        text=parseSpeechAct(text,(Do)i,speaker,listener);
                        temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.EVENT_QUESTION_DOER_WHAT,prop,IThoughtList);
                    }
                }
            }else{
                ArrayList<SpeechInfo> list = holder.getInfoFrase();
                //THIS IS AN INFO ABOUT AN EVENT!
                //System.err.println("THIS IS AN INFO EVENT!!!!: "+i.toString());
                if(((Do) i).getWhen()==null){
                    //dont know the time
                    if(((Do) i).whatWhatisAbout().equals("person")){
                        //I dont know the time and the statement is about a PERSON
                        text=createText(text,list,SpeechType.EVENT_INFO_RECEIVER_WHAT_DOER,prop);
                        text=parseSpeechAct(text,((Do)i),speaker,listener);
                        temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.EVENT_INFO_RECEIVER_WHAT_DOER,prop,IThoughtList);
                    }else{
                        //I dont know the time and the statement  is about an ITEM
                        text=createText(text,list,SpeechType.EVENT_INFO_DOER_WHAT_ITEM,prop);
                        text=parseSpeechAct(text,((Do)i),speaker,listener);
                        temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.EVENT_INFO_DOER_WHAT_ITEM,prop,IThoughtList);
                    }
                }else{
                    //know the time
                    if(((Do) i).whatWhatisAbout().equals("person")){
                        //know the time and its about PERSON
                        text=createText(text,list,SpeechType.EVENT_INFO_DOER_WHAT_TIME_RECEIVER,prop);
                        text=parseSpeechAct(text,((Do)i),speaker,listener);
                        temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.EVENT_INFO_DOER_WHAT_TIME_RECEIVER,prop,IThoughtList);
                    }else{
                        //know the time and its about ITEM
                        text=createText(text,list,SpeechType.EVENT_INFO_DOER_WHAT_TIME_ITEM,prop);
                        text=parseSpeechAct(text,((Do)i),speaker,listener);
                        temp = new SpeechAct(text,speaker.getName(),listener.getName(),SpeechType.EVENT_INFO_DOER_WHAT_TIME_ITEM,prop,IThoughtList);
                    }
                }
            }
        }


        if(temp.isDead()){
            //Something terrible has happend.
            //System.err.println("Could not create SpeeechAct from IThought [SpeechActFactory]");
            //System.exit(0);
            i.toString();
            speaker.getName();
            listener.getName();
            IThoughtList.size();
            temp = new SpeechAct(i.toString(),speaker.getName(),listener.getName(),SpeechType.DONT_KNOW,prop,IThoughtList);
        }
        //Debug.printDebugMessage("SPEEACHACTFACTORY: Right before the retrun statement, will run the speachActs debug method",
        //                        Debug.Channel.SPEECH_ACTS);
        //temp.deBugString();
        return temp;

    }

    private static String createText(String text,ArrayList<SpeechInfo> list,SpeechType sp,TextPropertyEnum prop){
        for(int i=0;i<list.size();i++){
            SpeechInfo si = list.get(i);
            if(si.getSpeechType().equals(sp)&&si.getTextProperty().equals(prop)){
                return si.getText();
            }
        }
        return text;
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
