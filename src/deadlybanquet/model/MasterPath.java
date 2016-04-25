package deadlybanquet.model;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Hampus on 2016-04-04.
 */
public class MasterPath {
    LinkedList<String> steps;

    public MasterPath(){
        steps = new LinkedList<>();
    }

    public void addStep(String s){
        steps.add(s);
    }

    public String getNext(){
        return steps.getFirst();
    }

    public String takeNext(){
        String s = steps.getFirst();
        steps.removeFirst();
        return s;
    }

    public void removeNext(){
        steps.removeFirst();
    }
    
    public boolean isEmpty(){
    	if(steps.isEmpty()){
    		return true;
    	}else{
    		return false;
    	}
    }


}
