package deadlybanquet.speech;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Tom on 2016-03-04.
 */
public class GreetingFrase implements SpeechAct {
    private String text;
    private boolean endConversation;

    private Map<String,Integer> opinion;

    private List<String> properties;

    public GreetingFrase(String property){

    }

    private void readFile(){
        BufferedReader br = null;
        String line = "";
        try{
            //everything here
            br = new BufferedReader(new FileReader("/res/sppch/greetingFrase.txt"));
            while ((line = br.readLine()) != null){
                System.out.println("test");
            }
        }catch (IOException e){
            //something
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public boolean isEndConversation() {
        return this.endConversation;
    }

    @Override
    public Map<String, Integer> getOpinionChanges() {
        return null;
    }

    @Override
    public List<String> getProperties(){
        return this.properties;
    }
}
