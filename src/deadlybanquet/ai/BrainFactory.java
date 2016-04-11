package deadlybanquet.ai;

import java.util.ArrayList;
import java.util.SortedSet;

/**
 *
 * @author omega
 */
public class BrainFactory {
    /**
     * 
     * @param information - if null will create empty list
     * @param emotion - if null will randomize
     * @param temperament - if null will randomize
     * @param desires - if null will create empty list
     * @param goals - if null will create empty list
     * @param plan - if null will create empty list
     * @param name - character identifier
     * @return 
     */
    public static NPCBrain makeBrain(SortedList information, 
                    PAD emotion, 
                    PAD temperament, 
                    ArrayList<Desire>desires,
                    ArrayList<Desire>goals,
                    ArrayList<IThought>plan,
                    String room,
                    String name){
        if (emotion==null){
            double p = Math.random()*2-1;
            double a = Math.random()*2-1;
            double d = Math.random()*2-1;
            emotion = new PAD(p,a,d);
        }
        if (temperament==null){
            double p = Math.random()*2-1;
            double a = Math.random()*2-1;
            double d = Math.random()*2-1;
            temperament = new PAD(p,a,d);
        }
        
       if(name==null) throw new NullPointerException("No name supplied for new NPC.");
       return new NPCBrain (information, emotion, temperament, desires, goals, plan, room, name);      
    }

    public static NPCBrain makeBrain(String room, String name){
        return makeBrain(null,null,null,null,null,null,room,name);
    }
}
