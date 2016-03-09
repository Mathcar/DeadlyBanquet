package deadlybanquet.ai;

import java.util.ArrayList;

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
     * @return 
     */
    public static Brain makeBrain(ArrayList<IThought> information, 
                    PAD emotion, 
                    PAD temperament, 
                    ArrayList<Desire>desires,
                    ArrayList<Desire>goals,
                    ArrayList<PlanElement>plan){
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
       return new Brain (information, emotion, temperament, desires, goals, plan);      
    }
}
