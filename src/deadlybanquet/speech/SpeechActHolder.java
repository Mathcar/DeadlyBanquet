package deadlybanquet.speech;

import deadlybanquet.ai.BeingPolite;
import deadlybanquet.ai.IThought;
import deadlybanquet.ai.Whereabouts;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Tom on 2016-03-11.
 */
public class SpeechActHolder {
    private ArrayList<SpeechInfo> greetingFrase;


    public void readFile(File file) {
        BufferedReader br = null;
        String line = "";
        ArrayList<SpeechInfo> temp = new ArrayList<SpeechInfo>();
        try {
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));//todo this is NOT! teseted
            while ((line = br.readLine()) != null) {
                String[] div = line.split(";");
                ArrayList<String> list = new ArrayList<String>();

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
    }
}
