package deadlybanquet.ai;

/**
 *
 * @author omega
 * //This is the brain of an NPC. It contains information about the NPC which the AI needs
 * to operate on frequently in a more easily accessible form.
 * There should be nothing in this file which could not technically be expressed as a list
 * of IThought objects.
 */
import static deadlybanquet.AI.speak;
import static deadlybanquet.ai.BeingPolite.*;
import static deadlybanquet.ai.PAD.placeholderPAD;
import static deadlybanquet.ai.Say.How.AGREE;
import static deadlybanquet.ai.Say.How.GETCONFIRMATION;
import static deadlybanquet.ai.Say.How.SAY;
import static deadlybanquet.model.World.getTime;
import deadlybanquet.speech.SpeechAct;
import static deadlybanquet.speech.SpeechActFactory.makeSpeechAct;
import java.util.*;
public class Brain extends Memory {
    //Current emotion - may be changed by events.
    private String me;
    private String currentRoom;
    private PAD emotion = new PAD(0,0,0);
    //Personality. May not change. Emotion object regresses
    //to this in the absence of new stimuli.
    private PAD temperament= new PAD(0,0,0);   
    //Things the NPC wishes were true
    private ArrayList<Desire> desires = new ArrayList<>();
    //The NPC's goals. This list should remain sorted.
    private ArrayList<Desire> goals = new ArrayList<>();
    //This uses plan elements rather than actions in order to facilitate reevaluation of the plan.
    private ArrayList<IThought> plan = new ArrayList<>();
    private ArrayList<Opinion> opinions = new ArrayList<>(); //TODO add this to constructor
    private ArrayList<Whereabouts> whereabouts = new ArrayList<>();
    private ArrayList<Principle> principles = new ArrayList<>();
    //this constructor replaces any bad values by defaults.
    public Brain(   ArrayList<IThought> information, 
                    PAD emotion, 
                    PAD temperament, 
                    ArrayList<Desire>desires,
                    ArrayList<Desire>goals,
                    ArrayList<IThought>plan,
                    String currentRoom,
                    String name){
        super(information, currentRoom);
        if (emotion!=null) this.emotion=emotion;
        if (temperament!=null) this.temperament=temperament;
        if (desires !=null) this.desires = desires;
        if (goals!=null) this.goals = goals;
        if (plan!=null) this.plan = plan;
        me = name;
    }
    
    @Override
    //This class evaluates the content, changing both opinions and information
    //This method is also responsible for sending answers.
    public void hear(SpeechAct act){
        ArrayList<IThought> content = act.getContent();
        ArrayList<IThought> possibleAnswers = new ArrayList<>();
        for (IThought t : content){
            ArrayList<IThought> foundData = new ArrayList<>(); 
            //A new list for places where that is useful.
            ArrayList<IThought> r = new ArrayList<>();
            //A say object for responses
            Say c;
            switch(t.getClass().getSimpleName()){
                case "Opinion": Opinion inOpinion = (Opinion) t;
                                Opinion o = new Opinion(inOpinion.aboutPersonRoomObject, null);
                                SomebodyElse previnfo = new SomebodyElse (o, act.getSpeaker(), null, 0.0);
                                foundData = find(previnfo);
                                //TODO do something smarter here
                                if (foundData.isEmpty()){
                                    //If there is none, take this data at face value.
                                    //TODO instead of null and 1.0 get sensible values
                                    SomebodyElse e = new SomebodyElse(o, act.getSpeaker(), null, 1.0);
                                    //TODO time
                                    information.add(e);
                                    Opinion response = new Opinion (inOpinion.aboutPersonRoomObject, null);
                                    for (Opinion i : opinions){
                                        if (i.aboutPersonRoomObject==inOpinion.aboutPersonRoomObject)
                                            response.pad=i.pad;
                                    }
                                    possibleAnswers.add(response);
                                } else {
                                    //Way too simple
                                    information.remove(foundData.get(0));
                                    Opinion previous = (Opinion) ((SomebodyElse)foundData.get(0)).what;
                                    inOpinion.previous = previous;
                                    information.add(new SomebodyElse(o, act.getSpeaker(), null, 1.0));
                                    possibleAnswers.add(foundData.get(0));
                                }
                                break;
                    
                case "SomebodyElse":SomebodyElse inElse = (SomebodyElse) t;
                                    if (inElse.aboutPerson==me){
                                        //find out what they are saying (new method)
                                        //react
                                    }
                                    else {
                                        //This should get a match if there - there should be at most
                                        //one of these, probably.
                                        foundData = find(inElse);
                                        possibleAnswers.add(foundData.get(0));
                                    }
                                    break;
                    
                case "BackStory": BackStory back = (BackStory) t;
                                  Say.How a;
                                  if (find(back).isEmpty()) {
                                      a =GETCONFIRMATION;
                                      information.add(t);
                                  }
                                  else {
                                      a=AGREE;
                                  }
                                  c = new Say(me, act.getSpeaker(), back, a, null);
                                  possibleAnswers.add(c);
                                  break;
                    
                case "Whereabouts": Whereabouts w = (Whereabouts) t;
                                    for (Whereabouts b:whereabouts){
                                        if (b.whoorwhat==w.whoorwhat)
                                                foundData.add(b);
                                    }
                                    Whereabouts tofind = new Whereabouts(w.whoorwhat,"",null,0.0);
                                    if(foundData.isEmpty()) foundData=find(tofind);
                                    //if I have no idea whatsoever about where the person is
                                    if(foundData.isEmpty()){
                                        foundData.add(new Say(me, act.getSpeaker(),w, GETCONFIRMATION,null));
                                    }
                                        possibleAnswers.add(foundData.get(0));
                                    break;
                    
                case "BeingPolite": c= new Say(me, act.getSpeaker(),t,AGREE,null);
                                    possibleAnswers.add(c);
                                    break;
                
                case "Say": //TODO
                            break;
                    
                case "Do": //Conveys that somebody does or did something.
                            break;
                    
                case "EmotionThought":  EmotionThought e = (EmotionThought) t;
                                        SomebodyElse s = new SomebodyElse (e, act.getSpeaker(), null, 1.0);
                                        foundData = find(s);
                                        if (foundData.isEmpty()){
                                            //If no previous information is available, return
                                            //I am happy/sad for you.
                                            s.time=getTime();
                                            information.add(s);
                                            PAD opinion = new PAD(e.pad.getP(), 0, 0);
                                            s.opinion = opinion;
                                            possibleAnswers.add(s);
                                            information.add(s);
                                        }
                                        else {
                                            //If we already have information, return previous info.
                                            SomebodyElse b = (SomebodyElse) foundData.get(0);
                                            information.remove(b);
                                            s.previous=b;
                                            information.add(s);
                                            possibleAnswers.add(b);
                                        }
                                        break;
                    
                case "Desire":  Desire d = (Desire) t;
                                Desire ownd = null;
                                //Find any goal
                                for (Desire i : goals){
                                    if(d.what.contains(i.what))
                                        ownd=i;
                                }
                                //If this is a goal of yours, say so.
                                if (ownd!=null){
                                    possibleAnswers.add(ownd);
                                    break;
                                }
                                //Find any own desire
                                for (Desire i : desires){
                                    if(d.what.contains(i.what))
                                        ownd=i;
                                }
                                //If I do not have a desire, or if my desire
                                //is positive when opponent's is positive (or the reverse)
                                if (ownd==null || ownd.strength*d.strength>=0){
                                    SomebodyElse offer = new SomebodyElse(d.what, me, placeholderPAD(),1.0);
                                    r.add(offer);
                                    possibleAnswers.add(new Say(me, act.getSpeaker(), offer, SAY, null));
                                }
                                else {
                                    possibleAnswers.add(ownd);
                                }
                                break;
                                
                default: System.out.println("You missed a case.");
            }
            selectResponse(possibleAnswers);
        }
    }
    
    //selects which statement to respond to in the case that an inOpinion speech act
    //contains several pieces of information
    //responsible for sending the response and updating one's own opinion of the
    //world in accordance with how one believes the statement to change the world/
    private void selectResponse(ArrayList<IThought> possibleResponses){
        //This is the real thing
        //speak(makeSpeechAct(possibleResponses, me));
        makeSpeechAct(possibleResponses, me);
    }
    
    
    @Override
    public void observeRoomChange(String person, String origin, String destination){
        
    }
    
    @Override
    public void observeInteraction(String who, String with){
        
    }
    
    @Override
    public void observePickUp(String who, String what){
        
    }
    
     @Override
    public void observePutDown(String who, String what){
        
    }
    
    //Character does some thinking, cooling down, executing plan etc.
    public void update(){
        
    }
    //this method creates plans for any goals and puts
    private void plan(){
        
    }
    
   
}
