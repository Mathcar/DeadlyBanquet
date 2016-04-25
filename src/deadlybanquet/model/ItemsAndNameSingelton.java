package deadlybanquet.model;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Tom on 2016-04-25.
 */
public class ItemsAndNameSingelton {
    private ArrayList<String> allNPCNames;
    private ArrayList<String> allNames;
    private ArrayList<String> items;

    private static ItemsAndNameSingelton instance = null;
    protected ItemsAndNameSingelton() {
        // Exists only to defeat instantiation.
    }
    public static ItemsAndNameSingelton getInstance() {
        if(instance == null) {
            instance = new ItemsAndNameSingelton();
        }
        return instance;
    }

    public ArrayList<String> readFile(){
        ArrayList<String> temp=new ArrayList<String>();
        BufferedReader br = null;
        String line = "";
        File file = new File("res/items/items");

        try{
            br = new BufferedReader(new FileReader(file.getAbsolutePath()));
            while((line=br.readLine()) != null){
                temp.add(line);
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(0);
        }catch(IOException e){
            e.printStackTrace();
            System.exit(0);
        }
        return temp;
    }

    public ArrayList<String> getAllNPCNames() {
        return allNPCNames;
    }

    public void setAllNPCNames(ArrayList<String> allNPCNames) {
        this.allNPCNames = allNPCNames;
    }

    public ArrayList<String> getAllNames() {
        return allNames;
    }

    public void setAllNames(ArrayList<String> allNames) {
        this.allNames = allNames;
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public void setItems(ArrayList<String> items) {
        this.items = items;
    }


}
