package deadlybanquet.speech;

import deadlybanquet.ai.BeingPolite;
import deadlybanquet.ai.IThought;
import deadlybanquet.ai.Whereabouts;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * Created by Tom on 2016-03-11.
 */
public class SpeechActHolder {
    private ArrayList<SpeechInfo> greetingFrase;
    private ArrayList<SpeechInfo> questionFrase;


    private static SpeechActHolder instance = null;
    protected SpeechActHolder() {
        // Exists only to defeat instantiation.
    }
    public static SpeechActHolder getInstance() {
        if(instance == null) {
            instance = new SpeechActHolder();
        }
        return instance;
    }

    public void readAllFile(){
        //try{
            /*Files.walk(Paths.get("res/speech/")).forEach(filePath ->{
                if(Files.isRegularFile(filePath)){
                    String fileName = filePath.getFileName().toString();
                    if(fileName.equals("greetingFrase")){
                        greetingFrase=readFile(filePath.toFile());
                    }else if(fileName.equals("questionFrase")){
                        questionFrase=readFile(filePath.toFile());
                    }else{
                        System.err.println(fileName+" Should not exist, if it should it has " +
                                "to be added in [readAllFile in SpeechActHolder]");
                    }
                }
            });
        }catch(IOException e){
            System.err.println("ERROR, could not find the speech map");
            e.printStackTrace();
            System.exit(0);
        }*/
    }

    private ArrayList<SpeechInfo> readFile(File file) {
        BufferedReader br = null;
        String line = "";
        ArrayList<SpeechInfo> temp = new ArrayList<SpeechInfo>();
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));//todo this is NOT! teseted
            while ((line = br.readLine()) != null) {
                String[] div = line.split(";");
                ArrayList<String> list = new ArrayList<String>();

                for(int i=0;i<div.length;i++){ //todo is this rly necessary?
                    list.add(div[i]);
                }

                temp.add(new SpeechInfo(list.get(0), TextPropertyEnum.valueOf(list.get(1)),
                        SpeechType.valueOf(list.get(2))));
            }
        } catch (FileNotFoundException e) {
            System.err.println("COULD NOT FIND FILE");
            e.printStackTrace();
            System.exit(3);
        } catch (IOException e) {
            System.err.println("Could not read file");
            e.printStackTrace();
            System.exit(3);
        }
        return temp;
    }

    public ArrayList<SpeechInfo> getQuestionFrase() {
        return questionFrase;
    }

    public void setQuestionFrase(ArrayList<SpeechInfo> questionFrase) {
        this.questionFrase = questionFrase;
    }

    public ArrayList<SpeechInfo> getGreetingFrase() {
        return greetingFrase;
    }

    public void setGreetingFrase(ArrayList<SpeechInfo> greetingFrase) {
        this.greetingFrase = greetingFrase;
    }
}
