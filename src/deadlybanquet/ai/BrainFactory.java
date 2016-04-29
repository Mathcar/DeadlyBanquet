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
                    String name,
                    AIControler aic,
                    ArrayList<Whereabouts> w){
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
       return new NPCBrain (information, emotion, temperament, desires, goals, plan, room, name, aic, w); //needs AIController
    }

    public static NPCBrain makeBrain(String room, String name, AIControler aic){
        return makeBrain(null,null,null,null,null,null,room,name, aic, null);
    }
    
    public static NPCBrain makeBrain(BrainInitInformation i){
        return makeBrain(   i.information, 
                            i.emotion, 
                            i.temperament, 
                            i.desires,
                            i.goals,
                            i.plan,
                            i.room,
                            i.name,
                            i.aic,
                            i.whereabouts);
    }
    /**
     * 
     * @param list of BrainInitInformation, one for each NPC
     * @return list of brains, already initialized with position.
     */
    public static ArrayList<NPCBrain> makeBrains(ArrayList<BrainInitInformation> list){
        for (BrainInitInformation i : list){
            Whereabouts w = new Whereabouts(i.name, i.room);
            for (BrainInitInformation j : list){
                if(w.getRoom()==j.room)
                    j.whereabouts.add(w.copy());
            }
        }
        ArrayList<NPCBrain> ans = new ArrayList<>();
        for (BrainInitInformation i:list){
            ans.add(makeBrain(i));
        }
        return ans;
    }
}
