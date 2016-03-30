package deadlybanquet.speech;

import deadlybanquet.ai.Action;
import deadlybanquet.ai.IThought;
import deadlybanquet.ai.PlanElement;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Tom on 2016-03-11.
 */
public class SpeechActHolder {
    private static SpeechActHolder instance = null;

    //private HashMap<TextPropertyEnum,Pair<String,ArrayList<IThought>>> greetingsList;
    private ArrayList<SpeechAct> greetingsList;
    private ArrayList<SpeechAct> valedictionList;

    public SpeechActHolder(){

    }

    public static SpeechActHolder getInstance(){
        if(instance==null){
            instance=new SpeechActHolder();
        }
        return instance;
    }

    public void createListFromFiles(){

        File directory = new File("res/speech");
        //get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList){
            if (file.isFile()){
                if(file.getName().equals("greetingFrase")){
                    GreetingPhrase temp = new GreetingPhrase();
                    greetingsList=readFile(file,temp);
                }
                else if(file.getName().equals("something else")){
                    //greetingsList=readFile(file);
                }
                // and so on...;
            }
        }

        /*Pair test = greetingsList.get(TextPropertyEnum.PROPER);
        String text = (String)test.getKey();
        System.err.println(text);*/
    }

    private ArrayList<SpeechAct> readFile(File file, SpeechAct act){
        //HashMap temp = new HashMap<TextPropertyEnum,Pair<String,ArrayList<IThought>>>();
        ArrayList<SpeechAct> tempList = new ArrayList<>();
        BufferedReader br = null;
        String line = "";
        try{
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));//todo this is NOT! teseted
            while ((line = br.readLine()) != null){
                String[] div = line.split(";");
                ArrayList<String> list = new ArrayList<String>();
                for(int i = 0;i<div.length;i++){
                    list.add(i,div[i]);
                }
                // Now list is a list of each "thing" in each line.
                TextPropertyEnum textEnum=TextPropertyEnum.NEUTRAL;
                try{
                    textEnum = TextPropertyEnum.valueOf(list.get(1));
                }catch(Exception e){
                    System.err.println("Something is wrong: could not make string to TextPropery enum[SpeechActHolder class]" +
                            "\nThe enum is set to NEUTRAL");
                }

                ArrayList<IThought> iThoughts= new ArrayList<IThought>();
                for(int i = 2;i<list.size();i++){
                    IThought iThought;
                    try{
                        /*
                        the prereq and result, will probably be needed later for more other stuff,
                        how will they be used?
                        For now this ONLY(maybe) work for greetings!
                         */
                        iThought=new PlanElement(null, Action.valueOf(list.get(i)),null);
                        iThoughts.add(iThought);
                    }catch(Exception e){
                        System.err.println("Something is wrong: could not make string to Action enum[SpeechActHolder class]" +
                                "\nThe program has to close here");
                        System.exit(3);
                    }
                }

                if(act.getClass().equals(GreetingPhrase.class)){
                    tempList.add(new GreetingPhrase(list.get(0),textEnum,iThoughts));
                }/*else if(act.getClass().equals(QuestionAboutSomethingFrase.class)){
                    tempList.add(new whatever(list.get(0),textEnum,iThoughts));
                }*/
            }
        }catch (IOException e){
            System.err.println("IOException while reading file");
            e.printStackTrace();
            System.exit(3);
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.err.println("IOException while closing file");
                    e.printStackTrace();
                    System.exit(3);
                }
            }
        }
        testConversation(tempList);
        return tempList;
    }

    private void testConversation(ArrayList<SpeechAct> tempList){

        //TEST FOR CONVERSATION
        //the NPC is...
        System.err.println("lets asume that you meet a NPC here");
        TextPropertyEnum prop=TextPropertyEnum.NEUTRAL;  // <--------Set the way the NPC talks
        SpeechAct npcSay=null;
        for(int i=0;i<tempList.size();i++){
            PlanElement a=(PlanElement) tempList.get(i).getContent().get(0);
            if(tempList.get(i).getTextProperty()==prop && a.action==Action.GREET){
                npcSay=tempList.get(i);
                break;
            }
        }
        System.err.println("NPC: "+npcSay.getText());
        System.err.println("the player will recive posible answers");
        for(int i=0;i<tempList.size();i++){
            PlanElement a=(PlanElement) tempList.get(i).getContent().get(0);
            if(a.action==Action.GREET){
                System.err.println(i+1+":"+tempList.get(i).getText());
            }
        }
        int playerChoise=2; //<------------Set player choice (1-3)
        System.err.println("player choose option number "+playerChoise);
        System.err.println("player: "+tempList.get(playerChoise-1).getText());
        for(int i=0;i<tempList.size();i++){
            PlanElement a=(PlanElement) tempList.get(i).getContent().get(0);
            if(tempList.get(i).getTextProperty()==prop && a.action==Action.GOODBYE){
                npcSay=tempList.get(i);
                break;
            }
        }
        System.err.println("NPC says: "+npcSay.getText());
    }

}