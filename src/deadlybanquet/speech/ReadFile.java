package deadlybanquet.speech;

import deadlybanquet.ai.Action;
import deadlybanquet.ai.IThought;
import deadlybanquet.ai.PlanElement;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tom on 2016-03-11.
 */
public class ReadFile {
    private static ReadFile instance = null;

    private HashMap<String,Pair<TextPropertyEnum,ArrayList<IThought>>> greetingsList;

    public ReadFile(){

    }

    public HashMap<String,Pair<TextPropertyEnum,ArrayList<IThought>>> readFile(File file){
        HashMap temp = new HashMap<String,Pair<TextPropertyEnum,ArrayList<IThought>>>();
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
                TextPropertyEnum textEnum=TextPropertyEnum.NUTRAL;
                try{
                    textEnum = TextPropertyEnum.valueOf(list.get(1));
                }catch(Exception e){
                    System.err.println("Something is wrong: could not make string to TextPropery enum[ReadFile class]" +
                            "\nThe enum is set to NEUTRAL");
                }

                ArrayList<IThought> iThoughts= new ArrayList<IThought>();
                for(int i = 2;i<list.size();i++){
                    IThought iThought;
                    try{
                        iThought=new PlanElement(null, Action.valueOf(list.get(i)),null);
                        iThoughts.add(iThought);
                    }catch(Exception e){
                        System.err.println("Something is wrong: could not make string to Action enum[ReadFile class]" +
                                "\nThe program has to close here");
                        System.exit(3);
                    }
                }


                temp.put(list.get(0),new Pair(textEnum,iThoughts));
                // This will make each line in the text files will be matched to one "thing" i the HashMap
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

        return temp;
    }

    public static ReadFile getInstance(){
        if(instance==null){
            instance=new ReadFile();
        }
        return instance;
    }

    /*
    here follows a lot of lists...
     */

}
