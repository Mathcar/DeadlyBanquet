package deadlybanquet.speech;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Tom on 2016-03-11.
 */
public class ReadFile {
    private static ReadFile instance = null;
    public ReadFile(){
        //here the reading of the files

    }

    public void readFile(File file){
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

                //todo
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
